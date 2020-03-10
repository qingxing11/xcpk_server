package com.wt.naval.api;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.safebox.AskPlayerInGameRequest;
import com.gjc.cmd.safebox.AskSafeBoxRecordRequest;
import com.gjc.cmd.safebox.AskSafeBoxRecordResponse;
import com.gjc.cmd.safebox.DepositSafeBoxRequest;
import com.gjc.cmd.safebox.DepositSafeBoxResponse;
import com.gjc.cmd.safebox.PushSafeBoxToOtherResponse;
import com.gjc.cmd.safebox.SafeBoxTransferRequest;
import com.gjc.cmd.safebox.SafeBoxTransferResponse;
import com.gjc.cmd.safebox.SafeBoxTransferSureRequest;
import com.gjc.cmd.safebox.SafeBoxTransferSureResponse;
import com.gjc.cmd.safebox.TakeOutSafeBoxRequest;
import com.gjc.cmd.safebox.TakeOutSafeBoxResponse;
import com.gjc.naval.safebox.SafeBoxService;
import com.gjc.naval.vip.VipService;
import com.gjc.naval.vo.safebox.SafeBoxRecordVO;
import com.gjc.naval.vo.safebox.SafeBoxVO;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.naval.dao.impl.SafeBoxDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.zjh.killroom.KillRoomService;

import data.data.Configs;
import io.netty.channel.ChannelHandlerContext;
import model.SafeboxRecordModel;

@RegisterApi(packagePath = "com.gjc.cmd.safebox") @Service public class ApiSafeBox
{
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Autowired
	private PlayerService playerService;

	@Autowired
	private Configs configs;

	@Autowired
	private SafeBoxService safeBoxService;

	@Autowired
	private VipService vipService;
	
	@Autowired
	private KillRoomService killRoomService;

	private ConcurrentHashMap<Integer, SafeBoxVO> curList = new ConcurrentHashMap<Integer, SafeBoxVO>();

	@Protocol(msgType = MsgTypeEnum.Player_进入游戏主界面)
	/** 玩家进入game场景 */
	public void askPlayerInGame(ChannelHandlerContext ctx, AskPlayerInGameRequest obj, MySession session)
	{
		playerService.playerInGame(session.getUserId());
	}

	@Protocol(msgType = MsgTypeEnum.SafeBox_存入银行)
	/** 存入银行 */
	public void depositSafeBox(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		DepositSafeBoxRequest request = (DepositSafeBoxRequest) obj;
		int userId = request.userId;
		long money = request.money;
		Player play = playerService.getPlayer(userId);
		int vipLv = play.getVipLv();
		if (!safeBoxService.haveSafeBox(vipLv))
		{
			DepositSafeBoxResponse response = new DepositSafeBoxResponse(DepositSafeBoxResponse.Error_暂无银行功能);
			playerService.sendMsg(response, userId);
			return;
		}

		if (money < 10000 || play.getCoins() < money)
		{
			DepositSafeBoxResponse response = new DepositSafeBoxResponse(DepositSafeBoxResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
			return;
		}

		long max = safeBoxService.safeBoxSaveMoneyMax(vipLv);
		if (play.getBankCoins() + money > max)
		{
			DepositSafeBoxResponse response = new DepositSafeBoxResponse(DepositSafeBoxResponse.Error_存入超过上线);
			playerService.sendMsg(response, userId);
			return;
		}

		boolean b = SafeBoxDaoImpl.updataUserSafeBox(userId, money);
		if (b)
		{
			Tool.print_subCoins(play.getNickName(),money,"存银行",play.getCoins());
			play.subCoinse(money);
			play.addBankCoinse(money);
			DepositSafeBoxResponse response = new DepositSafeBoxResponse(DepositSafeBoxResponse.SUCCESS, money);
			playerService.sendMsg(response, userId);
		}
		else
		{
			DepositSafeBoxResponse response = new DepositSafeBoxResponse(DepositSafeBoxResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
		}

	}

	@Protocol(msgType = MsgTypeEnum.SafeBox_取出银行)
	/** 取出银行 */
	public void takeOutSafeBox(ChannelHandlerContext ctx, Request obj, MySession session)
	{
		TakeOutSafeBoxRequest request = (TakeOutSafeBoxRequest) obj;
		int userId = request.userId;
		long money = request.money;

		Player play = playerService.getPlayer(userId);
		if (money < 10000 || play.getBankCoins() < money)
		{
			TakeOutSafeBoxResponse response = new TakeOutSafeBoxResponse(TakeOutSafeBoxResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
			return;
		}

		boolean b = SafeBoxDaoImpl.updataUserSafeBoxTakeOut(userId, money);
		if (b)
		{
			play.subBankCoinse(money);
			Tool.print_coins(play.getNickName(),money,play.getNickName()+"银行取款，银行剩余:"+play.getBankCoins(),play.getCoins());
			play.addCoins(money);
			TakeOutSafeBoxResponse response = new TakeOutSafeBoxResponse(TakeOutSafeBoxResponse.SUCCESS, money);
			playerService.sendMsg(response, userId);
			
			killRoomService.bankerWithdrawalSafebox(play);
		}
		else
		{
			TakeOutSafeBoxResponse response = new TakeOutSafeBoxResponse(TakeOutSafeBoxResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
		}

	}

	@Protocol(msgType = MsgTypeEnum.SafeBox_游戏币转账)
	/** 游戏币转账 */
	public void safeBoxTransfer(ChannelHandlerContext ctx, SafeBoxTransferRequest request, MySession session)
	{
		int userId = request.userId;
		int otherId = request.otherId;
		long money = request.money;

		Player player = playerService.getPlayer(userId);
		int vipLv = player.getVipLv();

		if (otherId == userId)
		{
			SafeBoxTransferResponse response = new SafeBoxTransferResponse(SafeBoxTransferResponse.Error_转给自己);
			playerService.sendMsg(response, userId);
			return;
		}

		if (!vipService.haveTransferAccounts(vipLv))
		{
			SafeBoxTransferResponse response = new SafeBoxTransferResponse(SafeBoxTransferResponse.Error_暂无转账功能);
			playerService.sendMsg(response, userId);
			return;
		}

		if (money < 10000)
		{
			SafeBoxTransferResponse response = new SafeBoxTransferResponse(SafeBoxTransferResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
			return;
		}

		PlayerSimpleData play = null;
		try
		{
			play = playerService.getPlayerSimpleData(otherId);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		if (play != null)
		{
			Tool.print_debug_level0("转账: 添加,userId="+userId);
			SafeBoxVO vo = new SafeBoxVO(userId, otherId, Math.abs(money));
			curList.put(userId, vo);
			Tool.print_debug_level0(MsgTypeEnum.SafeBox_游戏币转账,"申请人:"+play.getNickName()+",vip等级:"+play.vipLv);

			String nike = play.getNickName();
			SafeBoxTransferResponse response = new SafeBoxTransferResponse(SafeBoxTransferResponse.SUCCESS, nike, otherId,money);
			playerService.sendMsg(response, userId);
		}
		else
		{
			SafeBoxTransferResponse response = new SafeBoxTransferResponse(SafeBoxTransferResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
		}
	}

	@Protocol(msgType = MsgTypeEnum.SafeBox_游戏币转账确认)
	/** 游戏币转账确认 */
	public void safeBoxTransferSure(ChannelHandlerContext ctx, SafeBoxTransferSureRequest request, MySession session)
	{
		int userId = session.getUserId();
		// int otherId=request.otherId;
		Tool.print_debug_level0("转账: userId="+userId);
		SafeBoxVO vo = curList.get(userId);
		if (vo == null)
		{
			SafeBoxTransferSureResponse response = new SafeBoxTransferSureResponse(SafeBoxTransferSureResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
			Tool.print_debug_level0("转账确认: vo==null" + vo);
			return;
		}
		long money = vo.money;
		int otherId = vo.otherId;
		curList.remove(userId);

		Player otherPlay = playerService.getPlayer(otherId);
		Player play = playerService.getPlayer(userId);
		
		float pre = vipService.transferAccountsPer(play.getVipLv());//利率
		if (play.getCoins() < money * (1 + pre))
		{
			SafeBoxTransferSureResponse response = new SafeBoxTransferSureResponse(SafeBoxTransferSureResponse.Error_金额不足);
			playerService.sendMsg(response, userId);
			return;
		}

		long realMoney = (long) ((1 + pre) * money);
		Tool.print_debug_level0("手续费率：" + pre + ",真实扣除转账金币：" + realMoney + "，转账金币：" + money);

		play.subCoinse(Math.abs(realMoney));
		boolean b = SafeBoxDaoImpl.updataUserChangeSafeBox(userId, Math.abs(realMoney), otherId, Math.abs(money));
		
		if (b)
		{
			long times = Tool.getCurrTimeMM();
			Timestamp time = new Timestamp(times);
			// 操作记录存入
			boolean sign = false;

			int bRe = SafeBoxDaoImpl.insetSafeBoxRecord(userId, time, 0, Math.abs(money), otherId, sign, pre);// 0:转账
			// SafeBoxDaoImpl.insetSafeBoxRecord(userId, time, 0,
			// Math.abs(money), otherId,sign,pre);//0:转账
			Tool.print_debug_level0("操作记录存入 自增id:" + bRe);
			if (bRe < 0)
			{
				SafeBoxTransferSureResponse response = new SafeBoxTransferSureResponse(SafeBoxTransferSureResponse.ERROR_UNKNOWN);
				playerService.sendMsg(response, userId);
				Tool.print_error("操作记录存入 自增id:" + bRe);
				return;
			}
			SafeBoxRecordVO svo = new SafeBoxRecordVO(userId, otherId, Math.abs(money), times, 0, pre);
			play.addSafeBoxList(svo);

			if (otherPlay != null)
			{
				Tool.print_coins(otherPlay.getNickName(),money,play.getNickName()+"的转账",otherPlay.getCoins());
				otherPlay.addCoins(Math.abs(money));
				// 推送消息：记录
				PushSafeBoxToOtherResponse response2 = new PushSafeBoxToOtherResponse(svo, false);
				playerService.sendMsg(response2, otherId);
				sign = true;
				SafeBoxDaoImpl.updataUserSafeBoxSign(userId, sign);
			}

			SafeBoxTransferSureResponse response = new SafeBoxTransferSureResponse(SafeBoxTransferSureResponse.SUCCESS, Math.abs(money), otherId, 0, times, pre);
			playerService.sendMsg(response, userId);
		}
		else
		{
			SafeBoxTransferSureResponse response = new SafeBoxTransferSureResponse(SafeBoxTransferSureResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
		}

	}

	@Protocol(msgType = MsgTypeEnum.SafeBox_银行记录)
	/** 银行记录 */
	public void askSafeBoxRecord(ChannelHandlerContext ctx, AskSafeBoxRecordRequest request, MySession session)
	{
		int userId = request.userId;
		Tool.print_debug_level0(MsgTypeEnum.SafeBox_银行记录, "userid:" + userId);
		Player player = playerService.getPlayer(userId);
		// if (player.getSafeListVO()!=null)
		// {
		// Tool.print_debug_level0(MsgTypeEnum.SafeBox_银行记录,"userid:"+userId+",getSafeListVO已有记录");
		// AskSafeBoxRecordResponse response=new
		// AskSafeBoxRecordResponse(AskSafeBoxRecordResponse.SUCCESS,player.getSafeListVO());
		// playerService.sendMsg(response, userId);
		// return;
		// }
		List<SafeboxRecordModel> list = SafeBoxDaoImpl.getSafeBoxRecord(userId);
		if (list == null)
		{
			AskSafeBoxRecordResponse response = new AskSafeBoxRecordResponse(AskSafeBoxRecordResponse.ERROR_UNKNOWN);
			playerService.sendMsg(response, userId);
			return;
		}
		ArrayList<SafeBoxRecordVO> listVO = new ArrayList<SafeBoxRecordVO>();
		Tool.print_debug_level0(MsgTypeEnum.SafeBox_银行记录, "记录条数:" + list.size());
		for (SafeboxRecordModel s : list)
		{
			SafeBoxRecordVO vo = new SafeBoxRecordVO(s.getUserId(), s.getOtherId(), s.getMoney(), s.getTime().getTime(), s.getType(), s.getPre());
			listVO.add(vo);
		}
		player.setSafeListVO(listVO);
		AskSafeBoxRecordResponse response = new AskSafeBoxRecordResponse(AskSafeBoxRecordResponse.SUCCESS, listVO);
		playerService.sendMsg(response, userId);
	}
}

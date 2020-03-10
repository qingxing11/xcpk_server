package com.gjc.naval.vip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.cmd.user.push.Push_vipLevel;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.dao.impl.VIPDaoImpl;
import com.wt.naval.dao.model.user.GameDataModel;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;

import model.UserVip;


@Service
public class VipImpl implements VipService
{
	//TODO 待做宝箱功能 
	
	
	@Autowired
	private PlayerService playerService;
	
	private ArrayList<UserVip> listVIP=new ArrayList<UserVip>();
	private HashMap<Integer, UserVip> mapVIP=new HashMap<Integer, UserVip>();
	
	private ArrayList<String> lvName=new ArrayList<String>();
	
	
	private void initLvName()
	{
		lvName.add("游民");
		lvName.add("村长");
		lvName.add("知县");
		lvName.add("知府");
		lvName.add("巡抚");
		lvName.add("总督");
		lvName.add("将军");
		lvName.add("侯爷");
		lvName.add("藩王");
		lvName.add("亲王");
		lvName.add("皇帝");
		lvName.add("天神");
	}
	
	
	/**获得称谓*/
	@Override
	public String GetLvName(int lv)
	{
		if (lv<=0) 
		{
			Tool.print_error("玩家等级出错："+lv);
			return"";
		}
		int index=(lv-1)/5;
		if (lvName==null||lvName.size()==0) 
		{
			Tool.print_error("玩家等级名字配置表数据出错");
			return "";
		}
		index=index>lvName.size()-1?lvName.size():index;
		return lvName.get(index);
	}
	
	/**VIP配置表数据*/
	@Override
	public HashMap<Integer, UserVip> getMapVIP() {
		return mapVIP;
	}
	
	@PostConstruct
	private void ini()
	{
		InitVipData();
		 initLvName();
	}

	/**初始化VIP配置表数据*/
	private void InitVipData() 
	{
		List<UserVip> list=VIPDaoImpl.getUserVIPModel();
		if (list==null||list.size()==0) 
		{
			Tool.print_error("VIP没有配置表数据");
			return;
		}
		listVIP.addAll(list);
		for (UserVip userVip : list)
		{
			mapVIP.put(userVip.getId(), userVip);
		}
	}
	
	/**玩家VIP等级是否升级*/
	private int improveVIPLv(int userId,int topUp)
	{
		Player player = playerService.getPlayer(userId);
		int vipLv = 0;
		if (player == null)
		{
			GameDataModel game = UserDaoImpl.getUserData(userId);
			vipLv = game.getVipLv();
		}
		else
		{
			vipLv = player.getVipLv();
		}
		int lv = vipLv;
		boolean upVipLv = true;

		while (upVipLv)
		{
			lv++;
			if (mapVIP != null && mapVIP.containsKey(lv)) // 确保只到最大值
			{
				int needMoney = mapVIP.get(lv).getTopUp();
				if (topUp >= needMoney)
				{
					upVipLv = true;
				}
				else
				{
					lv--;
					break;
				}
			}
			else
			{
				lv--;
				break;
			}
		}

		if (lv > vipLv)
		{
			if (player != null)
			{
				player.setVipLv(lv);
				player.sendResponse(new Push_vipLevel(lv));
				player.setVipTime(MyTimeUtil.getCurrTimeMM());
			}
			UserDaoImpl.updateUserVIPLv(userId, lv);
			return lv; // 推送玩家vip等级
		}
		return vipLv;// 推送玩家vip等级
	}

	/**玩家充值处理*/
	@Override
	public void TopUp(int userId,int topUpmoney)
	{
		Tool.print_debug_level0("玩家：userId="+userId+"，本次充值：topUpmoney="+topUpmoney);
		if (topUpmoney<=0)
		{
			return;
		}
		int topUp=0;
		Player player=playerService.getPlayer(userId);
		UserDaoImpl.updateUserVipPayNum(userId, topUpmoney);
		if (player!=null) 
		{
			player.addVipPayNum(topUpmoney);
			topUp=player.getVipPayNum();
		}
		else
		{
			GameDataModel game=UserDaoImpl.getUserData(userId);
			topUp=game.getVipPayNum();
		}
//		int lastVipLv = player.getVipLv();
		//玩家VIP等级是否升级
		int lv=improveVIPLv(userId,topUp);
		
		
		Tool.print_debug_level0("玩家VIPLV等级："+lv+"，玩家充值累计总金额："+topUp);
	}
	
	@Override
	public UserVip getVipData(int lv)
	{
		if (mapVIP==null||mapVIP.size()==0) 
		{
			return null;
		}
		return mapVIP.get(lv);
	}
	
	/**是否有转账功能*/
	@Override
	public boolean haveTransferAccounts(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("haveTransferAccounts 玩家vip等级出错：vipLv="+vipLv);
			return false;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("haveTransferAccounts 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return false;
		}
		return data.isTransferAccounts();
	}
	
	/**转账手续费*/
	@Override
	public float transferAccountsPer(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("transferAccountsPer 玩家vip等级出错：vipLv="+vipLv);
			return 1;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("transferAccountsPer 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return 1;
		}
		return data.getTransferAccountsPer();
	}
	
	/**大喇叭聊天所需金币*/
	@Override
	public int typhonChatMoney(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("typhonChatMoney 玩家vip等级出错：vipLv="+vipLv);
			return 1;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("typhonChatMoney 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return 1;
		}
		for (int i = 0; i < 6; i++) {
			UserVip data2=getVipData(i);
			if (data2!=null) {
				Tool.print_debug_level0("vipLv="+vipLv+",大喇叭聊天扣除金币="+ data2.getTyphonChatMoney());
			}
		}
		return data.getTyphonChatMoney();
	}
	
	/**有无房间聊天功能*/
	@Override
	public boolean haveRoonChat(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("haveRoonChat 玩家vip等级出错：vipLv="+vipLv);
			return false;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("haveRoonChat 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return false;
		}
//		Tool.print_error("聊天功能：vipLv="+vipLv+",data.isRoomChat()="+data.isRoomChat());
		return data.isRoomChat();
	}
	
	/**每日登录奖励金币*/
	@Override
	public int loginAwardMoney(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("loginAwardMoney 玩家vip等级出错：vipLv="+vipLv);
			return 0;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("loginAwardMoney 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return 0;
		}
		return data.getLoginAwardMoney();
	}
	
	/**是否有宝箱功能*/
	@Override
	public boolean haveTreasureBox(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("typhonChatMoney 玩家vip等级出错：vipLv="+vipLv);
			return false;
		}
		UserVip data=getVipData(vipLv);
		if (data==null) 
		{
			Tool.print_error("typhonChatMoney 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return false;
		}
		return data.isTreasureBox();
	}
	
	/**开启几级宝箱*/
	public int treasureBoxLv(int vipLv)
	{
		if (!haveTreasureBox(vipLv)) 
		{
			return 0;
		}
		return vipLv;
	}
}

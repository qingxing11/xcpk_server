package com.gjc.naval.safebox;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.safebox.AskSafeBoxRecordResponse;
import com.gjc.cmd.safebox.PushSafeBoxToOtherResponse;
import com.gjc.naval.vip.VipService;
import com.gjc.naval.vo.safebox.SafeBoxRecordVO;
import com.wt.naval.dao.impl.SafeBoxDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;

import model.SafeboxRecordModel;
import model.UserVip;

@Service
public class SafeBoxImpl implements SafeBoxService
{
	@PostConstruct
	private void ini()
	{
	}
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private VipService vipService;
	
	
	
	
	
	/**是否有银行功能*/
	@Override
	public boolean haveSafeBox(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("haveSafeBox 玩家vip等级出错：vipLv="+vipLv);
			return false;
		}
		UserVip data=vipService.getVipData(vipLv);
		
		if (data==null) 
		{
			Tool.print_error("haveSafeBox 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return false;
		}
		
		return data.isBank();
	}
	
	/**当前银行存取金额*/
	@Override
	public long safeBoxSaveMoneyMax(int vipLv)
	{
		if (vipLv<0) 
		{
			Tool.print_error("safeBoxSaveMoneyMax 玩家vip等级出错：vipLv="+vipLv);
			return 0;
		}
		UserVip data=vipService.getVipData(vipLv);
		
		if (data==null) 
		{
			Tool.print_error("safeBoxSaveMoneyMax 玩家vip等级配置表数据没有：vipLv="+vipLv);
			return 0;
		}
		
		long num= data.getBankSaveNum();
		//Tool.print_debug_level0("限制："+num);
		if (num==-1) 
		{
			//Tool.print_debug_level0("无限制：");
			num=Long.MAX_VALUE;
			//Tool.print_debug_level0("无限制："+Long.MAX_VALUE);
		}
		return num;
	}
	
	@Override
	public void onPlayerOnLine(int otherId)
	{
		List<SafeboxRecordModel> listAccount=SafeBoxDaoImpl.getSafeBoxRecordOtherId(otherId);//转账方
		List<SafeboxRecordModel> listReceive =SafeBoxDaoImpl.getSafeBoxRecord(otherId);//接受方
		Player player=playerService.getPlayer(otherId);
		
		UserVip userVip = vipService.getVipData(player.getVipLv());
		
		float transferAccountsPer= (userVip == null ? 1 : userVip.getTransferAccountsPer());
		if (listAccount==null&&listReceive==null) 
		{
			PushSafeBoxToOtherResponse response=new PushSafeBoxToOtherResponse(null,true,transferAccountsPer);
			playerService.sendMsg(response, otherId);
			return;
		}
		ArrayList<SafeBoxRecordVO> listVO=new ArrayList<SafeBoxRecordVO>();
		
		if (listAccount!=null) {
			for (SafeboxRecordModel s : listAccount) 
			{
				SafeBoxRecordVO vo=new SafeBoxRecordVO(s.getUserId(),s.getOtherId(),s.getMoney(),s.getTime().getTime(),s.getType(),s.getPre());
				if (!s.isSign()) 
				{
					listVO.add(vo);
					SafeBoxDaoImpl.updataUserSafeBoxSign(s.getKey(), true);
				}
			}	
		}
		
		if (listReceive!=null) {
			for (SafeboxRecordModel s : listReceive) 
			{
				SafeBoxRecordVO vo=new SafeBoxRecordVO(s.getUserId(),s.getOtherId(),s.getMoney(),s.getTime().getTime(),s.getType(),s.getPre());
				if (!s.isSign()) 
				{
					listVO.add(vo);
					SafeBoxDaoImpl.updataUserSafeBoxSign(s.getKey(), true);
				}
			}
		}
		
		if (listVO==null||listVO.size()==0) 
		{
			PushSafeBoxToOtherResponse response=new PushSafeBoxToOtherResponse(null,true,transferAccountsPer);
			playerService.sendMsg(response, otherId);
			return;
		}
		PushSafeBoxToOtherResponse response=new PushSafeBoxToOtherResponse(listVO,true,transferAccountsPer);
		playerService.sendMsg(response, otherId);
	}
	
}

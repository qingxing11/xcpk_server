package com.wt.backend.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gjc.cmd.horn.SendHornInfoResponse;
import com.gjc.naval.horn.HornInfoVO;
import com.wt.cmd.user.push.Push_coinsChange;
import com.wt.cmd.user.push.Push_vipLevel;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.HttpUtil;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;
import com.wt.xcpk.manypepol.ManyPepolService;
import com.wt.xcpk.zjh.killroom.KillRoomService;
import com.yt.xcpk.fruitMachine.FruitMachineService;

@Controller
public class BackendController
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private KillRoomService killroomService;
	
	@Autowired
	private ManyPepolService manyPepolService;
	
	@Autowired
	private FruitMachineService fruitService;
	
	@RequestMapping("/tongsha")
	@ResponseBody
	public String tongsha()
	{
		killroomService.refreshKillroomConfig();
		Tool.print_debug_level0("tongsha");
		return "success";
	}
	
	@RequestMapping("/jingdian")
	@ResponseBody
	public String jingdian()
	{
		Tool.print_debug_level0("jingdian");
		return "success";
	}
	
	@RequestMapping("/wanren")
	@ResponseBody
	public String wanren()
	{
		manyPepolService.refreshKillroomConfig();
		Tool.print_debug_level0("wanren");
		return "success";
	}
	
	@RequestMapping("/shuiguo")
	@ResponseBody
	public String shuiguo()
	{
		Tool.print_debug_level0("shuiguo刷新配置");
		fruitService.refreshConfig();
		return "success";
	}
	
	
	@RequestMapping("/addCoins")
	@ResponseBody
	public String addCoins(int userId,int coins,HttpServletRequest request)
	{
		boolean isAccess = IPUtils.isAccess(request);
		Tool.print_debug_level0("addCoins-->isAccess:"+isAccess);
		
		Player player = playerService.getPlayer(userId);
		if(player != null)
		{
			player.sendResponse(new Push_coinsChange(Math.abs(coins)));
			Tool.print_coins(player.getNickName(),coins,"后台",player.getCoins());
			player.addCoins(Math.abs(coins));
			
			Tool.print_debug_level0("玩家增加金币,在线,userId:"+userId+",coins:"+coins);
			
			PlayerDaoImpl.updatePlayerCoins(player.getCoins(), userId);
		}
		else
		{
			Tool.print_debug_level0("玩家增加金币,不在线,userId:"+userId+",coins:"+coins);
			PlayerDaoImpl.addPlayerCoins(Math.abs(coins), userId);
		}
		
		return "success";
	}
	
	@CrossOrigin(origins = "http:/192.168.0.106:2015")
	@RequestMapping(value  = "/addVip",method = RequestMethod.GET)
	@ResponseBody
	public String addVip(int userId,int vipLv)
	{
		Player player = playerService.getPlayer(userId);
		if(player != null)
		{
			Tool.print_debug_level0("玩家赠送vip,在线,userId:"+userId+",vipLv:"+vipLv);
			player.setVipLv(vipLv);
			player.setVipTime(MyTimeUtil.getCurrTimeMM());
			player.sendResponse(new Push_vipLevel(vipLv));
		}
		else
		{
			Tool.print_debug_level0("玩家赠送vip,不在线,userId:"+userId+",vipLv:"+vipLv);
		}
		UserDaoImpl.updateUserVIPLv(userId, vipLv);
		return "success";
	}
	
	@RequestMapping("/subCoins")
	@ResponseBody
	public String subCoins(int userId,int coins)
	{
		Player player = playerService.getPlayer(userId);
		if(player != null)
		{
			player.sendResponse(new Push_coinsChange(-Math.abs(coins)));
			player.addCoins(-Math.abs(coins));
			
			Tool.print_debug_level0("玩家减少金币,在线,userId:"+userId+",coins:"+coins);
			PlayerDaoImpl.updatePlayerCoins(player.getCoins(), userId);
		}
		else
		{
			Tool.print_debug_level0("玩家减少金币,不在线,userId:"+userId+",coins:"+coins);
			
			PlayerDaoImpl.subPlayerCoins(Math.abs(coins), userId);
		}
		
		return "success";
	}
	
	@RequestMapping("/banId")
	@ResponseBody
	public String banId(int userId)
	{
		boolean isSuccess = PlayerDaoImpl.banId(userId);
		Tool.print_debug_level0("冻结玩家,userId:"+userId+",isSuccess:"+isSuccess);
		if(!isSuccess)
		{
			return "failed";
		}
		playerService.bannid(userId);
		return "success";
	}
	
	@RequestMapping("/unbanId")
	@ResponseBody
	public String unbanId(int userId)
	{
//		Player player = playerService.getPlayer(userId);
		boolean isSuccess = PlayerDaoImpl.deleteBanId(userId);
		Tool.print_debug_level0("启用玩家,userId:"+userId+",isSuccess:"+isSuccess);
		if(isSuccess)
		{
			return "success";
		}
		return "failed";
	}
	
	@RequestMapping("/serverNotice")
	@ResponseBody
	public String serverNotice(String notice)
	{
//		Player player = playerService.getPlayer(userId);
		
		HornInfoVO vo=new HornInfoVO(0,"系统",0,notice,0);
		
		Tool.print_debug_level0("发送系统广播给所有玩家:"+vo);
		
		SendHornInfoResponse response=new SendHornInfoResponse(SendHornInfoResponse.SUCCESS,vo,0);
		for (Player play : playerService.getAllPlayer()) 
		{
			play.sendResponse(response);
		}
		
		return "success";
	}
	
	private String handlerHttpRequest(HttpServletRequest request)
	{
		String str = null;
		Map<?, ?> map = HttpUtil.handlerHttpRequestByParameterMap(request);
		Iterator<?> iterator = map.entrySet().iterator();
		if(iterator.hasNext())
		{
			Entry<String,Object> enter = (Entry<String, Object>) iterator.next();
			str = enter.getKey();
		}
		
		return str;
	}
}

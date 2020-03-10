package com.wt.pay.wxpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.brc.naval.lucky.LuckyService;
import com.gjc.naval.moneytree.MoneyTreeService;
import com.gjc.naval.vip.VipService;
import com.wt.cmd.user.push.Push_GameMoneyChange;
import com.wt.naval.dao.impl.PayDaoImpl;
import com.wt.naval.dao.impl.PlayerDaoImpl;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.wt.xcpk.PlayerSimpleData;
import com.wt.xcpk.rank.RankService;

@Controller
public class WxPayCallBackController
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private VipService vipService;
	
	@Autowired
	private MoneyTreeService moneyTreeService;
	
	@Autowired
	private LuckyService luckyService;
	
	@Autowired
	private RankService rankService;
	
	@RequestMapping("/wxPayCallBack")
	@PostMapping("/notify")
	public String wxPayCallBack(HttpServletRequest request)
	{
		if(request == null)
		{
			Tool.print_error("wxPayCallBack,request空");
			return "request空";
		}
		
		StringBuilder sb = new StringBuilder();
		InputStream inputStream;
		BufferedReader in;
		Map<String, String> map = null;
		try
		{
			inputStream = request.getInputStream();
			String s;
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null)
			{
				sb.append(s);
			}
			in.close();
			inputStream.close();
			if(sb.length() <= 0)
			{
				Tool.print_error("wxPayCallBack ---->参数空");
				return "wxPayCallBack ---->参数空";
			}
		        map = WXPayUtil.xmlToMap(sb.toString());
		}
		catch (IOException e)
		{
			Tool.print_error("wxPayCallBack ---->IOException", e);
			e.printStackTrace();
			return "wxPayCallBack ---->IOException";
		}
		catch (Exception e)
		{
			Tool.print_error("wxPayCallBack ---->Exception", e);
			e.printStackTrace();
			return "wxPayCallBack ---->Exception";
		}
	 
		if(map == null || map.size() == 0)
		{
			Tool.print_error("wxPayCallBack,params空");
			return "wxPayCallBack,params空";
		}
		
//		System.out.println("微信返回参数:"+map);
		
		String return_code = map.get("return_code");
		if(return_code == null || !return_code.equals("SUCCESS"))
		{
			Tool.print_error("wxPayCallBack,params.return_code错误");
			return "wxPayCallBack,params.return_code错误";
		}
		
		String outTradeNo = map.get("out_trade_no");
		String totalFee = map.get("total_fee");
		String attach = map.get("attach");
		int userId = 0;
		int payId = 0;
		int payNum = 0;
		try
		{
			String[] attachArr = attach.split(":");
			userId = Integer.parseInt(attachArr[0]);
			payId = Integer.parseInt(attachArr[1]);
			
			payNum = Integer.parseInt(totalFee);
		}
		catch (Exception e)
		{
			Tool.print_error("解析微信支付回调时错误",e);
		}
		
		Integer integer = PayDaoImpl.insertPayLog(outTradeNo, "WX", totalFee, userId);
		if(integer != null)
		{
			Player player = playerService.getPlayer(userId);
			int giftNum = getPayCrytstal(payId);
			giftNum *= luckyService.lucky(userId);
			giftNum += getPayGift(payId);
			luckyService.resetLuckyRate(userId);
			if(player != null)
			{
				Tool.print_debug_level0("微信支付后玩家在线，增加钻石:"+giftNum+",userId:"+userId);
				Push_GameMoneyChange push_GameMoneyChange = new Push_GameMoneyChange(Push_GameMoneyChange.MONEYTYPE_CRYTSTAL, Push_GameMoneyChange.STATE_ADD, giftNum, player.getCrytstal());
				player.sendResponse(push_GameMoneyChange);
				PlayerDaoImpl.addPlayerCrytstal(giftNum,userId);
				player.addCrystals(giftNum);
				
				rankService.addPayNum(player.getUserId(), payNum/100, player.getNickName(), player.getLevel());
			}
			else
			{
				Tool.print_debug_level0("微信支付后玩家离线，增加钻石到数据库:"+giftNum+",userId:"+userId);
				PlayerDaoImpl.addPlayerCrytstal(giftNum,userId);
				
				PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(userId);
				rankService.addPayNum(userId, payNum/100, playerSimpleData.getNickName(), playerSimpleData.getLv());
			}
			
			vipService.TopUp(userId, payNum/100);
			moneyTreeService.addTreeLv(userId, payNum/100);
			
		}
		else
		{
			Tool.print_error("wxPayCallBack,已经验证过的订单");
		}
		String returnCode = "<xml>\r\n" + 
				"\r\n" + 
				"  <return_code><![CDATA[SUCCESS]]></return_code>\r\n" + 
				"  <return_msg><![CDATA[OK]]></return_msg>\r\n" + 
				"</xml>";
		return returnCode;
	}
	
	private int getPayCrytstal(int payId)
	{
		switch (payId)
		{
			case 0:
				return 5;
				
			case 1:
				return 10;
				
			case 2:
				return 30;
				
			case 3:
				return 50;
				
			case 4:
				return 100;
				
			case 5:
				return 500;

			default:
				break;
		}
		return 0;
	}
	
	private int getPayGift(int payId)
	{
		switch (payId)
		{
			case 0:
				return 0;
				
			case 1:
				return 1;
				
			case 2:
				return 5;
				
			case 3:
				return 10;
				
			case 4:
				return 30;
				
			case 5:
				return 250;

			default:
				break;
		}
		return 0;
	}
}

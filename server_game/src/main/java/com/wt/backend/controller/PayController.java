package com.wt.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.pay.AliPayOrderInfo;
import com.wt.pay.ali.AliPayService;
import com.wt.util.Tool;

@Controller
public class PayController
{
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private AliPayService aliPayService;
	
	//http://47.100.229.107/webpay/pay.html
	@RequestMapping("/getAliWebOrder")
	@ResponseBody
	public String getAliWebOrder(int userId,int orderId)
	{
		Tool.print_debug_level0("PAY_支付宝网页支付下单,userId:"+userId+",orderId:"+orderId);
		Player player = playerService.getPlayer(userId);
		if (player == null)
		{
			Tool.print_debug_level0("PAY_支付宝网页支付下单", "获取玩家数据错误");
			return "获取玩家数据错误";

//			 player = new Player();
//			 UserData userData = new UserData();
//			 player.userData = userData;
//			 player.userData.userId = userId;
		}

		// 第一步，生成订单
		 String payInfo = aliPayService.getPayInfo(orderId);
		 String cost = aliPayService.getCost(orderId) +"";
		 AliPayOrderInfo aliPayOrderInfo = aliPayService.getAliPayOrder(player.getUserId(), cost,"购买"+payInfo, orderId);
		if (aliPayOrderInfo == null)
		{
			Tool.print_error(player.getNickName()+ ",初始化订单信息出错" + orderId);

			return "初始化订单信息出错:" + orderId;
		}

		String url = "http://47.100.229.107/gm/webpay/alipay.jsp?" + "WIDout_trade_no=" + aliPayOrderInfo.out_trade_no + "&WIDsubject=" + payInfo + "&WIDtotal_amount=" + aliPayOrderInfo.total_fee + "&WIDbody=" + payInfo;

		Tool.print_debug_level0(player.getNickName() + ",支付宝网页支付下单,获取成功,订单:" + url);
		return url;
	}
}

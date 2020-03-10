package com.wt.naval.api;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.brc.naval.lucky.LuckyService;
import com.gjc.naval.moneytree.MoneyTreeService;
import com.gjc.naval.vip.VipService;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgType;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.cmd.pay.BuyGoldRequest;
import com.wt.cmd.pay.BuyGoldRespone;
import com.wt.cmd.pay.BuyItemRequest;
import com.wt.cmd.pay.BuyItemRespone;
import com.wt.cmd.pay.GetAliPayOrderRequest;
import com.wt.cmd.pay.GetAliPayOrderRespone;
import com.wt.cmd.pay.GetAliPayWebOrderRequest;
import com.wt.cmd.pay.GetAliPayWebOrderRespone;
import com.wt.cmd.pay.GetApplePayVerifyReceiptRequest;
import com.wt.cmd.pay.GetApplePayVerifyReceiptRespone;
import com.wt.cmd.pay.GetWxPayOrderRequest;
import com.wt.cmd.pay.GetWxPayOrderRespone;
import com.wt.cmd.pay.GetWxWebPayOrderRequest;
import com.wt.cmd.pay.GetWxWebPayOrderRespone;
import com.wt.cmd.pay.PayTestPayRequest;
import com.wt.cmd.pay.PayTestPayRespone;
import com.wt.naval.biz.UserBiz;
import com.wt.naval.cache.UserCache;
import com.wt.naval.main.GameServerHelper;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.pay.AliPayOrderInfo;
import com.wt.pay.ali.AliPayService;
import com.wt.pay.wxpay.WxPayService;
import com.wt.tool.ClassTool;
import com.wt.util.Tool;
import com.wt.util.security.MySession;
import com.wt.xcpk.rank.RankService;

import io.netty.channel.ChannelHandlerContext;

@RegisterApi(packagePath = "com.wt.cmd.pay") 
@Service
public class ApiPay
{
	@Autowired
	private AliPayService aliPayService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private WxPayService wxPayService;
	
	@Autowired
	private MoneyTreeService moneyTreeService;
	
	@Autowired
	private VipService vipService;
	
	@Autowired
	private LuckyService luckyService;
	
	@Autowired
	private RankService rankService;
	
	@Protocol(msgType = MsgTypeEnum.PAY_BUYITEM) 
	public void buyItem(ChannelHandlerContext ctx,BuyItemRequest request,MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		int buyItemCost = getBuyItemCost(request.payIndex);
		if(player.getCrytstal() >= buyItemCost)
		{
			player.subCrytstal(buyItemCost);
			
			buyItemSuccess(request.payIndex,player);
			
			player.sendResponse(new BuyItemRespone(BuyItemRespone.SUCCESS, request.payIndex));
			Tool.print_debug_level0(MsgTypeEnum.PAY_BUYGOLD,"玩家钻石换金币，payIndex :"+request.payIndex+",buyGoldCost:"+buyItemCost+",获得奖励:"+getGoldReward(request.payIndex));
			return;
		}
		player.sendResponse(new BuyItemRespone(BuyItemRespone.ERROR_金币不足));
	}
	
	private void buyItemSuccess(int payIndex,Player player)
	{
		switch (payIndex)
		{
			case 0:
				player.addRobPosNum(6);
				break;
				
			case 1:
				player.addTimeNum(6);
				break;
				
			case 2:
				player.addModifyNickName(6);
				break;
				
			case 3:
				player.addRobPosNum(70);
				break;
				
			case 4:
				player.addTimeNum(70);
				break;
				
			case 5:
				player.addModifyNickName(70);
				break;

			default:
				break;
		}
	}

	@Protocol(msgType = MsgTypeEnum.PAY_BUYGOLD) 
	public void buyGold(ChannelHandlerContext ctx,BuyGoldRequest request,MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		int buyGoldCost = getBuyGoldCost(request.payIndex);
		if(player.getCrytstal() >= buyGoldCost)
		{
			player.subCrytstal(buyGoldCost);
			int gift = getGoldReward(request.payIndex);
			Tool.print_coins(player.getNickName(),gift,"钻石换金币",player.getCoins());
			player.addCoins(gift);
			player.sendResponse(new BuyGoldRespone(BuyGoldRespone.SUCCESS, getGoldReward(request.payIndex)));
			Tool.print_debug_level0(MsgTypeEnum.PAY_BUYGOLD,"玩家钻石换金币，payIndex :"+request.payIndex+",buyGoldCost:"+buyGoldCost+",获得奖励:"+getGoldReward(request.payIndex));
			return;
		}
		player.sendResponse(new BuyGoldRespone(BuyGoldRespone.ERROR_金币不足));
	}
	
	private int getGoldReward(int payIndex)
	{
		int reward = 0;
		switch (payIndex)
		{
			case 0:
				reward = 50000;
				break;
				
			case 1:
				reward = 110000;
				break;
				
			case 2:
				reward = 333000;
				break;
				
			case 3:
				reward = 565000;
				break;
				
			case 4:
				reward = 1300000;
				break;
			
			case 5:
				reward = 7500000;
				break;

			default:
				break;
		}
		return reward;
	}
	
	private int getBuyGoldCost(int payIndex)
	{
		 int cost = 0;
		        switch (payIndex)
		        {
		            case 0:
		                cost = 5;
		                break;

		            case 1:
		                cost = 10;
		                break;

		            case 2:
		                cost = 30;
		                break;

		            case 3:
		                cost = 50;
		                break;

		            case 4:
		                cost = 100;
		                break;

		            case 5:
		                cost = 500;
		                break;

		            default:
		                break;
		        }
		        return cost;
	}
	
	private int getBuyItemCost(int payIndex)
	{
		 int cost = 0;
		        switch (payIndex)
		        {
		            case 0:
		                cost = 5;
		                break;

		            case 1:
		                cost = 5;
		                break;

		            case 2:
		                cost = 5;
		                break;

		            case 3:
		                cost = 50;
		                break;

		            case 4:
		                cost = 50;
		                break;

		            case 5:
		                cost = 50;
		                break;

		            default:
		                break;
		        }
		        return cost;
	}
	
	@Protocol(msgType = MsgTypeEnum.PAY_TestPay) // 指定本消息对应的协议号
	public void payTestPay(ChannelHandlerContext ctx,PayTestPayRequest request,MySession session)
	{
		PayTestPayRespone respone = null;
		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}
		
		request.payNum *= luckyService.lucky(player.getUserId());
		luckyService.resetLuckyRate(player.getUserId());
		
		Tool.print_crystals(player.getNickName(), request.payNum, "测试钻石");
		player.addCrystals(request.payNum);
		
		vipService.TopUp(player.getUserId(),request.payNum);
		moneyTreeService.addTreeLv(player.getUserId(),request.payNum);
		rankService.addPayNum(player.getUserId(), request.payNum, player.getNickName(), player.getLevel());
		
		respone = new PayTestPayRespone(PayTestPayRespone.SUCCESS, request.payNum);
		player.sendResponse(respone);
	}
	
	@Protocol(msgType = MsgTypeEnum.PAY_微信支付下单) // 指定本消息对应的协议号
	public void getWxPayOrder(ChannelHandlerContext ctx,GetWxPayOrderRequest request,MySession session)
	{
		GetWxPayOrderRespone response = null;

		Player player = playerService.getPlayerAndCheck(session);
		if(player == null)
		{
			return;
		}

		
		String cost = (int)(aliPayService.getCost(request.payId) * 100) + "";
		String info = aliPayService.getPayInfo(request.payId);
				
		// 第四步，向数据库中添加订单记录
//		if (!PayBiz.insertWxUserOrder(orderId, map_initOrder.get("out_trade_no"), userId, payOrderDef.cost + "", map_userOrder.get("prepayid"), player.userData.nickName))
//		{
//			Tool.print_debug_level2(player.userData.nickName, MsgTypeEnum.PAY_微信支付下单, "插入数据库错误:" + orderId);
//
//			response = new GetWxPayOrderRespone(GetWxPayOrderRespone.ERROR_插入数据库错误);
//			GameServerHelper.sendResponse(ctx, response);
//			return;
//		}

		String wxPayOrder = wxPayService.getWxPayOrder(player.getUserId(), cost, info,player.getUserId()+":"+request.payId);
		
		Tool.print_debug_level0(MsgTypeEnum.PAY_微信支付下单,player.getNickName()+ ",获取成功,订单:" + request.payId+",order:"+wxPayOrder);
		response = new GetWxPayOrderRespone(GetWxPayOrderRespone.SUCCESS, wxPayOrder);
		GameServerHelper.sendResponse(ctx, response);
	
	}


	

	@Protocol(msgType = MsgTypeEnum.PAY_支付宝支付下单) // 指定本消息对应的协议号	
	public void getAliPayOrder(ChannelHandlerContext ctx, GetAliPayOrderRequest request,MySession session)
	{
		boolean isClose = true;
		if(isClose)
		{
			return;
		}
		
		GetAliPayOrderRespone respone = null;
		
		 Player player = playerService.getPlayerAndCheck(session);
		
		
		 String payInfo = aliPayService.getPayInfo(request.payId);
		 String cost = aliPayService.getCost(request.payId) +"";
		 AliPayOrderInfo aliPayOrderInfo = aliPayService.getAliPayOrder(player.getUserId(), cost,"购买"+payInfo, request.payId);
		 respone = new GetAliPayOrderRespone(GetAliPayOrderRespone.SUCCESS, aliPayOrderInfo);
		 player.sendResponse(respone);
	}
	
	@Protocol(msgType = MsgTypeEnum.PAY_支付宝网页支付下单) // 指定本消息对应的协议号	
	public void getAliPayWebOrder(ChannelHandlerContext ctx, GetAliPayWebOrderRequest request,MySession session)
	{
		boolean isClose = true;
		if(isClose)
		{
			return;
		}
		
		GetAliPayWebOrderRespone respone = null;
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			return ;
		}
		int userId = player.getUserId();
		Tool.print_debug_level0(MsgTypeEnum.PAY_支付宝网页支付下单,"userId:"+userId+",orderId:"+request.payId);

		// 第一步，生成订单
		 String payInfo = aliPayService.getPayInfo(request.payId);
		 String cost = aliPayService.getCost(request.payId) +"";
		 AliPayOrderInfo aliPayOrderInfo = aliPayService.getAliPayOrder(player.getUserId(), cost,"购买"+payInfo, request.payId);
		if (aliPayOrderInfo == null)
		{
			Tool.print_error(player.getNickName()+ ",初始化订单信息出错" + request.payId);

			respone = new GetAliPayWebOrderRespone(GetAliPayWebOrderRespone.ERROR_初始化订单信息出错);
			 player.sendResponse(respone);
			return;
		}

		String url = "http://47.100.229.107:7070/webPay/alipay.jsp?" + "WIDout_trade_no=" + aliPayOrderInfo.out_trade_no + "&WIDsubject=" + payInfo + "&WIDtotal_amount=" + aliPayOrderInfo.total_fee + "&WIDbody=" + payInfo;
		respone = new GetAliPayWebOrderRespone(GetAliPayWebOrderRespone.SUCCESS,url);
		Tool.print_debug_level0(player.getNickName() + ",支付宝网页支付下单,获取成功,订单:" + url);
		 player.sendResponse(respone);
	}
	
	@Protocol(msgType = MsgTypeEnum.PAY_微信网页支付下单) // 指定本消息对应的协议号	
	public void getWxWebPayOrder(ChannelHandlerContext ctx, GetWxWebPayOrderRequest request,MySession session)
	{
		Player player = playerService.getPlayerAndCheck(session);
		if (player == null)
		{
			Tool.print_debug_level0(MsgTypeEnum.PAY_微信网页支付下单, "获取玩家数据错误");
			return;
		}
		Tool.print_debug_level0(MsgTypeEnum.PAY_微信网页支付下单,"单号:"+request.payId+",userId:"+player.getUserId());
		String cost =  (int)(aliPayService.getCost(request.payId) * 100) + "";
		String info = aliPayService.getPayInfo(request.payId);
		
		String wxPayOrder = wxPayService.getWxPayOrder(player.getUserId(), cost, info,player.getUserId()+":"+request.payId);
		if(wxPayOrder == null)
		{
			player.sendResponse(new GetWxWebPayOrderRespone(GetWxWebPayOrderRespone.ERROR_初始化订单信息出错));
			return;
		}

		Tool.print_debug_level0(player.getNickName(), MsgTypeEnum.PAY_微信网页支付下单, "获取成功,订单:" + wxPayOrder);
		player.sendResponse(new GetWxWebPayOrderRespone(GetWxWebPayOrderRespone.SUCCESS, wxPayOrder));
	}
	
	private float getTestCost(int payId)
	{
		switch (payId)
		{
			case 0:
				return 0.01f;
				
			case 1:
				return 0.02f;
				
			case 2:
				return 0.03f;
				
			case 3:
				return 0.04f;
				
			case 4:
				return 0.05f;
				
			case 5:
				return 0.06f;

			default:
				return 0.01f;
		}
	}
	
	@Protocol(msgType = MsgTypeEnum.PAY_获取苹果支付结果) // 指定本消息对应的协议号
	public static void getApplePayVerifyReceipt(ChannelHandlerContext ctx, Request obj)
	{
		GetApplePayVerifyReceiptRequest request = (GetApplePayVerifyReceiptRequest) obj;

		GetApplePayVerifyReceiptRespone response = null;
		
		MySession session = ctx.channel().attr(MySession.attr_session).get();
		if (!UserBiz.checkSessionAndOnline(ctx, session))
		{
			return;
		}
		int userId = session.getUserId();
		
		Player player = UserCache.getPlay(userId);
		if (player == null)
		{
			Tool.print_debug_level2(MsgType.PAY_获取苹果支付结果, "获取玩家数据错误,玩家不在线");
			response = new GetApplePayVerifyReceiptRespone(GetApplePayVerifyReceiptRespone.ERROR_UNKNOWN);
			GameServerHelper.sendResponse(ctx, response);
			return;
		}
		
		response = new GetApplePayVerifyReceiptRespone(GetApplePayVerifyReceiptRespone.SUCCESS);
		GameServerHelper.sendResponse(ctx, response);
		
		Tool.print_debug_level2(MsgType.PAY_获取苹果支付结果, "verifyReceipt:"+request.verifyReceipt);
//		MyApplePay.checkApplePay(request.verifyReceipt,userId, player.getNickName());
	}
	
	@PostConstruct
	private void init()
	{
		try
		{
			ClassTool.registerApi(this);
		}
		catch (Exception e)
		{
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		
//		testPay();
	}
}

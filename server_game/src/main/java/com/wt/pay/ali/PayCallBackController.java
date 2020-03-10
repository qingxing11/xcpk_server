package com.wt.pay.ali;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
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
@PropertySource({ "classpath:alipay.properties" })
public class PayCallBackController
{
	@Value("${ali.appid}")
	private String appid;

	@Value("${ali.privateKey}")
	private String privateKey;

	@Value("${ali.publicKey}")
	private String publicKey;
	
	@Autowired
	private VipService vipService;
	
	@Autowired
	private PlayerService playerService;
	
	@Autowired
	private MoneyTreeService moneyTreeService;
	
	@Autowired
	private LuckyService luckyService;
	
	@Autowired
	private RankService rankService;
	
//	aliPayCallBack->params:{gmt_create=2020-02-05 16:36:21, charset=UTF-8, seller_email=18360532025, 
//	notify_time=2020-02-05 17:06:33, subject=购买750diamond,
//	sign=XIjnpP8FtWWmVaVSAxjiW9IVcydgZWjhtm6/hHfAMqgdmv6maXTuEk0UFMdpkPFFlTfPklyv/DqF/ZYgZei3stsUTuxaq+mUfr9eM/NE4I3iVuv55sju4F1aKkBBpACahYVyXNRd2N91SPbFL5i8qqInS9gtfGPpmvE6NcKQuKSYCp1fVTvc6wYtowGqYs0u6Gus8REUJwirDeF8/9OuCRbI10mdEvxe8g5qUnYQt/Eml+a6YU4zZwzrzGjmKv4+YwfFyhaenuBo8424y1a/I8QKcA0+Tc+QixjXBch9kqIKsi1D2i88FfIIvWDS8wafZgDIlhXV5hO24lEU9TAB3A==, 
//	body=34966:5, buyer_id=2088532532196893, version=1.0, notify_id=2020020500222170633096891432125025, notify_type=trade_status_sync, out_trade_no=77404H6Mn6rmy34966, 
//	total_amount=500.00, trade_status=TRADE_CLOSED, refund_fee=0.00, trade_no=2020020522001496891418173117, auth_app_id=2019063065766271, gmt_close=2020-02-05 17:06:33, buyer_logon_id=134***@qq.com, app_id=2019063065766271, sign_type=RSA2, seller_id=2088532618293221}

	@RequestMapping("/aliPayCallBack")
	@ResponseBody
	public String aliPayCallBack(HttpServletRequest request) throws AlipayApiException
	{
		boolean isClose = true;
		if(isClose)
		{
			return null;
		}
		
		Tool.print_debug_level0("aliPayCallBack->params:");
		if(request == null)
		{
			return "request空";
		}
		//获取支付宝POST过来反馈信息
		Map<?, ?> requestParams = request.getParameterMap();
		if(requestParams == null)
		{
			return "requestParams空";
		}
		
		Map<String,String> params = new HashMap<String,String>();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter.hasNext();) {
		    String name = (String) iter.next();
		    String[] values = (String[]) requestParams.get(name);
		    String valueStr = "";
		    for (int i = 0; i < values.length; i++) {
		        valueStr = (i == values.length - 1) ? valueStr + values[i]
		                    : valueStr + values[i] + ",";
		  	}
		    //乱码解决，这段代码在出现乱码时使用。
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
			params.put(name, valueStr);
		}
		
		if(params.size() == 0)
		{
			return "获取到的参数空";
		}
		Tool.print_debug_level0("aliPayCallBack->params:"+params);
		//切记alipaypublickey是支付宝的公钥，请去open.alipay.com对应应用下查看。
		//boolean AlipaySignature.rsaCheckV1(Map<String, String> params, String publicKey, String charset, String sign_type)
		boolean flag = AlipaySignature.rsaCheckV1(params, publicKey, "UTF-8","RSA2");
		
		Tool.print_debug_level0("aliPayCallBack--->flag:"+flag);
		if(flag)
		{
			String outTradeNo = params.get("out_trade_no");
			String totalFee = params.get("invoice_amount");
			String attach = params.get("body");
			Integer integer = null;
			int userId = 0;
			int payId = 0;
			float payNum = 0;
			try
			{
				String[] attachArr = attach.split(":");
				userId = Integer.parseInt(attachArr[0]);
				payId = Integer.parseInt(attachArr[1]);
				
				payNum = Float.parseFloat(totalFee);
			}
			catch (Exception e)
			{
				Tool.print_error("解析ali支付回调时错误",e);
			}
			
			integer = PayDaoImpl.insertPayLog(outTradeNo, "Ali", totalFee, userId);
			if(integer != null)
			{
				Player player = playerService.getPlayer(userId);
				int giftNum = getPayCrytstal(payId);
				giftNum *= luckyService.lucky(userId);
				giftNum += getPayGift(payId);
				luckyService.resetLuckyRate(userId);
				if(player != null)
				{
					Tool.print_debug_level0("ali支付后玩家在线，增加钻石:"+giftNum+",userId:"+userId);
					Push_GameMoneyChange push_GameMoneyChange = new Push_GameMoneyChange(Push_GameMoneyChange.MONEYTYPE_CRYTSTAL, Push_GameMoneyChange.STATE_ADD, giftNum, player.getCrytstal());
					player.sendResponse(push_GameMoneyChange);
					PlayerDaoImpl.addPlayerCrytstal(giftNum,userId);
					player.addCrystals(giftNum);
					
					rankService.addPayNum(player.getUserId(), (int)payNum, player.getNickName(), player.getLevel());
				}
				else
				{
					Tool.print_debug_level0("ali支付后玩家离线，增加钻石到数据库:"+giftNum+",userId:"+userId);
					PlayerDaoImpl.addPlayerCrytstal(giftNum,userId);
					
					PlayerSimpleData playerSimpleData = playerService.getPlayerSimpleData(userId);
					rankService.addPayNum(userId, (int)payNum, playerSimpleData.getNickName(), playerSimpleData.getLv());
				}
				
				vipService.TopUp(userId, (int)payNum);
				moneyTreeService.addTreeLv(userId, (int)payNum);
			}
			else
			{
				Tool.print_error("aliPayCallBack,已经验证过的订单");
			}
			return "success";
		}
		else
		{
			return "fail";
		}
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

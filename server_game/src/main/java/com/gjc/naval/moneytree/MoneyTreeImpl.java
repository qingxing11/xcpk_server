package com.gjc.naval.moneytree;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gjc.cmd.monetTree.AskMonetTreeResponse;
import com.gjc.cmd.monetTree.GetMoneyTreeResponse;
import com.gjc.cmd.monetTree.PushMoneyTreeLvResponse;
import com.wt.naval.dao.impl.UserDaoImpl;
import com.wt.naval.dao.model.user.GameDataModel;
import com.wt.naval.module.player.PlayerService;
import com.wt.naval.vo.user.Player;
import com.wt.util.MyTimeUtil;
import com.wt.util.Tool;

@Service
public class MoneyTreeImpl implements MoneyTreeService
{
	private boolean isDebug=true;
	private int baseMoneyInHour=1000;//基本金币
	private float baseOutputfficiency=100/100f;//基本效率
	private int lvAddMoneyInHour=50;//基本等级增加，每小时增加金币量
	private int improveLvNeedExp=20;//提升一等级，需要的经验
	private int topUpAddExp=1;//每充值一元增加的经验值
	private float  improveLvAddOutput=0.01f;//每提升一级增加的产出效率
	private float  improveVipLvAddOutput=0.2f;//VIp每级增加的产出效率
	
	private int intTime=10*60*1000;//间隔10分钟
	//private int intTime=1*60*1000;//间隔10分钟//TODO 测试
	private int treeLvNeedMoney=20;//每升一级需要的 充值金额
	private int treeLvMax=5000;//摇钱最大等级
	private int addMonthtreeLvMax=200;//摇钱每月增加最大等级
	private int treeLvInit=0;//摇钱树原来等级
	
	@Autowired
	private PlayerService playerService;
	
	@PostConstruct
	private void ini()
	{
	}
	
	@Override
	public void  askMonetTree(int userId)
	{
		Tool.print_debug_level0("请求同步！");
		Player player=playerService.getPlayer(userId);
		if (player==null) 
		{
			Tool.print_debug_level0("player==null"+userId);
			return;
		}
		long curTime=MyTimeUtil.getCurrTimeMM();
		long startTime=player.gameData.getStartOutPutTime();
		long money=calculateCurTimeMoney(player.gameData.getTreeLv(),player.getVipLv(),player.getLevel(),curTime,startTime);
		long hour=8*60*60*1000-(curTime-startTime);
		hour = hour >= 0 ? hour : 0;
		Tool.print_debug_level0("askMonetTree,hour:"+hour);
		AskMonetTreeResponse response=new AskMonetTreeResponse(Math.abs(hour),money);
		playerService.sendMsg(response, player.getUserId());
	}
	
	
	@Override
	/**领取金币*/
	public void getMoney(int userId)
	{
		Player player=playerService.getPlayer(userId);
		//判断是否满足8个小时
		long curTime=MyTimeUtil.getCurrTimeMM();
		long startTime=player.gameData.getStartOutPutTime();
		boolean isCalculate = MyTimeUtil.getHour(curTime - startTime) >= 8 ? true : false;
		isCalculate = true;
		if (isDebug)
			Tool.print_debug_level0("lastTime：" + curTime + ",startTime：" + startTime + "isCalculate" + isCalculate);

		if (!isCalculate)
		{
			GetMoneyTreeResponse response=new GetMoneyTreeResponse(GetMoneyTreeResponse.No_Money);
			playerService.sendMsg(response, userId);
			return;
		}
		
		long money=calculateCurTimeMoney(player.gameData.getTreeLv(),player.getVipLv(),player.getLevel(),curTime,startTime);
		Tool.print_coins(player.getNickName(),money,"摇钱树",player.getCoins());
		player.addCoins(money);
		
		player.gameData.setStartOutPutTime(curTime);
		UserDaoImpl.updateUserStartOutPutTime(userId, curTime);
		
//		//保存玩家金币（首次产出时间）
//		UserDaoImpl.updateUserCoins((int)player.getCoins(), userId);
	
		GetMoneyTreeResponse response=new GetMoneyTreeResponse(GetMoneyTreeResponse.SUCCESS,money);
		playerService.sendMsg(response, userId);
	}
	
	
	
	public long calculateCurTimeMoney(int treeLv,int vipLv,int lv,long lastTime,long startTime)
	{
		long money = 0;
		long margin = lastTime - startTime;
		long hour = MyTimeUtil.getHour(margin);

		if (isDebug)
			Tool.print_debug_level0("当前时间：" + lastTime + ",上次产出时间：" + startTime + "产出时间相差：" + margin + ",相差小时数：" + hour);

		if (hour >= 1)
		{
			hour = hour > 8 ? 8 : hour;
			long hourMoney = calculateMoney(treeLv, vipLv, lv);
			Tool.print_debug_level0("calculateCurTimeMoney   -----> hourMoney:"+hourMoney);
			money = hourMoney * hour;
		}
		return money;
	}
	
	
	
	/**计算当前时间产出金币（摇钱树等级，vip等级）*/
	@Override
	public long calculateMoney(int treelv,int vipLv,int lv)
	{
		float effect=baseOutputfficiency+improveVipLvAddOutput*vipLv+(lv)*improveLvAddOutput;
		Tool.print_debug_level0("效率："+effect);
		long money=baseMoneyInHour+(treelv)*lvAddMoneyInHour;
		Tool.print_debug_level0("金币："+money);
		return (long)(money*effect);
	}

	
	
	/**摇钱树升级*/
	@Override
	public void addTreeLv(int userId,int addTopUp)
	{
		if (addTopUp<=0) {
			return;
		}
		
		UserDaoImpl.updateUserTopUp(userId, addTopUp);
		
		Player player=playerService.getPlayer(userId);
		int realTree=0;
		int topUp=0;
		if (player!=null) 
		{
			player.addTopUp(addTopUp);
			topUp=player.getTopUp();
			realTree=player.gameData.getTreeLv();
		}
		else
		{
			GameDataModel game=UserDaoImpl.getUserData(userId);
			topUp=game.getTopUp();
			realTree=game.getTreeLv();
		}

		Tool.print_debug_level0("当前充值金额："+topUp);
		int treeLv=topUp/treeLvNeedMoney+treeLvInit;
		
		if (treeLv <= realTree) {
			Tool.print_debug_level0("摇钱树等级没有提升，realTree="+realTree+"treeLv="+treeLv);
			return;
		}
		
		if (treeLv>treeLvMax) {
			treeLv=treeLvMax;
		}
		
		int curMonthAddLv=player.gameData.getCurMonthAddTreeLv();
		int addLv=treeLv-realTree;
		if (curMonthAddLv+addLv > addMonthtreeLvMax) 
		{
			addLv=addMonthtreeLvMax-curMonthAddLv;
			treeLv=realTree+addLv;
		}
		
		if (addLv<=0) {
			Tool.print_debug_level0("本月摇钱树充值增加等级达到上线！");
			return;
		}
		Tool.print_debug_level0("摇钱树addLv："+addLv+",增加后实际等级：treeLv="+treeLv);
		
		player.gameData.AddCurMonthAddTreeLv(addLv);
		UserDaoImpl.updateUserAddTreeLv(userId, addLv);
		
		if (player!=null) {
			player.gameData.setTreeLv(treeLv);
			player.sendResponse(new PushMoneyTreeLvResponse(treeLv));
		}
		
		Tool.print_debug_level0("摇钱树上次等级：" +realTree+"，本次充值摇钱树等级提升到：" + treeLv);
		UserDaoImpl.updateUserTreeLv(userId, treeLv);
	}
	
}

package com.yt.xcpk.fruitMachine;

import java.util.HashMap;

import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;

public class FruitMachineVO
{
	private Player player;// 当前玩家
	private boolean isContinueXiaZhu;// 是否连续下注
	private HashMap<Integer, Integer> map_typeAnfValue = new HashMap<Integer, Integer>();// 玩家下的注
												// 类型和数值
												// 类型对应FruitTable表中枚举
												// FruitTYpe

	public void setPlayer(Player player)
	{
		this.player = player;
	}

	public Player getPlayer()
	{
		return this.player;
	}

	// 下一局下注前清除上一局的押注
	public void clearMapContent()
	{

		// Tool.print_debug_level0("下注前 金币为："+player.getCoins());
		if (isContinueXiaZhu)
		{
			int valueSub = 0;
			for (Integer value : map_typeAnfValue.values())
			{
				if (player.getCoins() >= value)
				{
					valueSub += value;
					Tool.print_subCoins(player.getNickName(),value,"水果机",player.getCoins());
					player.subCoinse(value);
				}
				else
				{
					valueSub += player.getCoins();
					Tool.print_subCoins(player.getNickName(),player.getCoins(),"水果机",player.getCoins());
					player.subCoinse(player.getCoins());
					value = (int) player.getCoins();
				}
			}
			Tool.print_debug_level0("续押下注后 金币为：" + player.getCoins() + ",下注扣除金币为：" + valueSub);
			return;
		}
		map_typeAnfValue.clear();
	}

	public void setIsContinueXiaZhu(boolean isContinueXiaZhu)
	{
		this.isContinueXiaZhu = isContinueXiaZhu;
	}

	public void putTypeAndValue(int type, int value)
	{
		if (map_typeAnfValue.containsKey(type))
		{
			map_typeAnfValue.put(type, map_typeAnfValue.get(type) + value);
			return;
		}
		map_typeAnfValue.put(type, value);
	}

	public HashMap<Integer, Integer> getMap()
	{
		// TODO Auto-generated method stub
		return map_typeAnfValue;
	}

	@Override
	public String toString()
	{
		return "FruitMachineVO [player=" + player.getUserId() + ", isContinueXiaZhu=" + isContinueXiaZhu + ", map_typeAnfValue=" + map_typeAnfValue + "]";
	}

}

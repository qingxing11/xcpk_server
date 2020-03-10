package com.wt.util;

import java.util.ArrayList;

public class AntiCrackNum
{
	private ArrayList<Integer> guardianCode = new ArrayList<Integer>(LEVEL);// 守护数据

	private ArrayList<Integer> maskCode = new ArrayList<Integer>(LEVEL);// 影子数据

	public int value;// 真值数据
	
	public static final int LEVEL = 32;

	public AntiCrackNum()
	{
		new AntiCrackNum(0);
	}

	public AntiCrackNum(int value)
	{
		resetGuardian();
		setValue(value);
	}

	private void resetGuardian()
	{
		guardianCode.clear();
		for (int i = 0 ; i < LEVEL ; i++)
		{
			guardianCode.add((Tool.getRandomBoolean() ? 1 : -1) * Tool.getRandom(100, 1000));
		}
	}

	public void setValue(int value)
	{
		maskCode.clear();
		this.value = value;
		resetGuardian();
		for (int i = 0 ; i < LEVEL ; i++)
		{
			maskCode.add(guardianCode.get(i) + value);
		}
	}

	public void addValue(int value)
	{
		setValue(this.value + value);
	}

	public void subValue(int value)
	{
		setValue(this.value - value);
	}

	private int[] value_weight = new int[LEVEL];
	private int[] value_test = new int[LEVEL];

	public int getValue()
	{
		boolean dataCracked = false;
		int maybeTrue = value;// 可能为真的数值

		for (int i = 0 ; i < guardianCode.size()  ; i++)
		{
			if (value != maskCode.get(i) - guardianCode.get(i))
			{
				dataCracked = true;
				break;
			}
		}
		if (dataCracked)
		{// 如果数据被破解，尝试猜出真数值，重新生成对守护数据
			int specialCode = -Tool.getRandom(128);
			for (int i = 0 ; i < value_test.length ; i++)
			{// 清空表格
				value_test[i] = specialCode;
				value_weight[i] = 0;
			}
			boolean valueInserted = false;
			int currValue = 0;
			for (int index = 0 ; index < maskCode.size() ; index++)
			{
				currValue = maskCode.get(index) - guardianCode.get(index);
				for (int i = 0 ; i < value_test.length ; i++)
				{
					if (value_test[i] == currValue)
					{
						value_weight[i]++;
						valueInserted = true;
						break;
					}
				}
				if (!valueInserted)
				{
					for (int i = 0 ; i < value_test.length ; i++)
					{
						if (value_test[i] == specialCode)
						{// 尚未赋值
							value_test[i] = currValue;
							value_weight[i]++;
							break;
						}
					}
				}
			}

			int heavyIndex = 0;
			for (int i = 0 ; i < value_weight.length - 1 ; i++)
			{
				if (value_weight[i + 1] > value_weight[i])
				{
					heavyIndex = i + 1;
				}
			}
			int maybeTrueValue = value_test[heavyIndex];// 最可能是真实的值
			setValue(maybeTrueValue);// 重新赋值
			return value;
		}
		else
		{
			return value;
		}
	}

	public String toString()
	{
		return "" + getValue();
	}

}

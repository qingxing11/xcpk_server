package com.dataStruct.orignalEnumType;

public enum OrignalActionDataEnum
{

	DoNothing(0, "什么也不做"),
	Timer(500,"计时"),
	LoopTimer(501," 循环计时器");

	private int value;
	private String discripe;

	private OrignalActionDataEnum(int num, String name)
	{
		this.value = num;
		this.discripe = name;
	}

	public int getValue()
	{
		return value;
	}

	public String getDiscripe()
	{
		return discripe;
	}
}

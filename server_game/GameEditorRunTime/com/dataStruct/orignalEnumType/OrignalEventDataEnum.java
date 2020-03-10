package com.dataStruct.orignalEnumType;

public enum OrignalEventDataEnum
{

	playerEnterGame(1000, "玩家进入游戏"),
	EventTrigger(2500, "一个触发器作为一个事件");
	
	
	

	private int value;
	private String discripe;

	private OrignalEventDataEnum(int num, String name)
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

















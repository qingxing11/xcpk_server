package com.dataStruct.orignalEnumType;

public enum OrignalConditionDataEnum
{
	OR(2001, "或条件"),
	AND(2002,"与条件");

	private int value;
	private String discripe;

	private OrignalConditionDataEnum(int num, String name)
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

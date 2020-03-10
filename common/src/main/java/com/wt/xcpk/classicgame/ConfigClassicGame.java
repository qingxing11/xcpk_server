package com.wt.xcpk.classicgame;

public class ConfigClassicGame
{
	private int waitStartTime;//9秒
	
	private int waitPlayerThink;//服务器第一次多0.9*N+0.5
	
	private int waitPayBetTime;

	private int waitCalcWin;
	
	private int  waitComparerpoker;
	public ConfigClassicGame()
	{
		waitStartTime = 5;
		waitPlayerThink = 20;
		waitPayBetTime = 5;
		waitCalcWin= 5;
		waitComparerpoker = 4;
	}
	
	public int getWaitComparerpoker()
	{
		return waitComparerpoker;
	}

	public void setWaitComparerpoker(int waitComparerpoker)
	{
		this.waitComparerpoker = waitComparerpoker;
	}
	
	public int getWaitCalcWin()
	{
		return waitCalcWin;
	}

	public void setWaitCalcWin(int waitCalcWin)
	{
		this.waitCalcWin = waitCalcWin;
	}
	
	public int getWaitPayBetTime()
	{
		return waitPayBetTime;
	}

	public void setWaitPayBetTime(int waitPayBetTime)
	{
		this.waitPayBetTime = waitPayBetTime;
	}
	
	public void setWaitPlayerThink(int waitPlayerThink)
	{
		this.waitPlayerThink = waitPlayerThink;
	}

	public int getWaitStartTime()
	{
		return waitStartTime;
	}

	public void setWaitStartTime(int waitStartTime)
	{
		this.waitStartTime = waitStartTime;
	}

	public int getWaitPlayerThink()
	{
		return waitPlayerThink;
	}
}

package com.wt.unity.unityengine;

public class Vector2
{
	public float x;
	public float y;
	
	public Vector2(float x,float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector2()
	{
	}
	
	public Vector2(int x,int y)
	{
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString()
	{
		return "Vector2 [x=" + x + ", y=" + y + "]";
	}

	/**
	 * 将该vector2的值由像素单位转换为unity单位
	 */
	public void pixelToUnity()
	{
		x /= 100f;
		y /= 100f;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof Vector2)
		{
			Vector2 v2 = (Vector2)obj;
			return this.x == v2.x && this.y == v2.y;
		}
		return super.equals(obj);
	}

	public int getIntX()
	{
		return (int)x;
	}
	
	public int getIntY()
	{
		return (int)y;
	}
	
}

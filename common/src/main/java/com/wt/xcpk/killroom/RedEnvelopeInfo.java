package com.wt.xcpk.killroom;

import java.io.Serializable;

public class RedEnvelopeInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -503314163486711551L;

	// 红包索引
	public int redEnvelopeIndex;

	// 红包额度
	public long redEnvelopeValue;

	public int userId;

	public RedEnvelopeInfo(int redEnvelopeIndex, long redEnvelopeValue, int userId)
	{
		this.redEnvelopeIndex = redEnvelopeIndex;
		this.redEnvelopeValue = redEnvelopeValue;
		this.userId=userId;
	}

	@Override
	public String toString()
	{
		return "RedEnvelopeInfo [redEnvelopeIndex=" + redEnvelopeIndex + ", redEnvelopeValue=" + redEnvelopeValue + "]";
	}
}

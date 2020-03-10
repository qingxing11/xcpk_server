package com.wt.naval.vo.res;

public class HotfixLineVO
{
	/**
	 * 文件所处目录
	 */
	public String url;
	
	/**
	 *文件md5
	 */
	public String md5;
	
	/**
	 * 文件字节数
	 */
	public int size;

	public String getUrl()
	{
		return url;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getMd5()
	{
		return md5;
	}

	public void setMd5(String md5)
	{
		this.md5 = md5;
	}

	public int getSize()
	{
		return size;
	}

	public void setSize(int size)
	{
		this.size = size;
	}

	@Override
	public String toString()
	{
		return "HotfixLinePO [url=" + url + ", md5=" + md5 + ", size=" + size + "]";
	}
	
	
}

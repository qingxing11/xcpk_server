package com.wt.naval.vo.res;

import java.util.ArrayList;

public class HotfixUpdateFileVO
{
	/**
	 * 所有文件
	 */
	public ArrayList<String> list_fileUrl = new ArrayList<>();
	
	/**
	 * 下载路径，公共部分
	 */
	public String url_path;
	
	/**
	 * 总字节数
	 */
	public int allSize;
	
	/**
	 * 对应的版本号
	 */
	public int version;

	public String gateway_host;
	public int gateway_port;
	@Override
	public String toString()
	{
		return "HotfixUpdateFileVO [list_fileUrl=" + list_fileUrl + ", url_path=" + url_path + ", allSize=" + allSize + ", version=" + version + "]";
	}
}

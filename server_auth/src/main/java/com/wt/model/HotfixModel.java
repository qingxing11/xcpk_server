package com.wt.model;

public class HotfixModel
{
	public String url;
	public int start_version;
	public int now_version;
	public String platform;
	public String gateway_host;
	public int gateway_port;
	public int big_version;
	public String install_pack_url;
	
	public String getInstall_pack_url()
	{
		return install_pack_url;
	}
	public void setInstall_pack_url(String install_pack_url)
	{
		this.install_pack_url = install_pack_url;
	}
	public int getBig_version()
	{
		return big_version;
	}
	public void setBig_version(int big_version)
	{
		this.big_version = big_version;
	}
	public String getGateway_host()
	{
		return gateway_host;
	}
	public void setGateway_host(String gateway_host)
	{
		this.gateway_host = gateway_host;
	}
	public int getGateway_port()
	{
		return gateway_port;
	}
	public void setGateway_port(int gateway_port)
	{
		this.gateway_port = gateway_port;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public int getStart_version()
	{
		return start_version;
	}
	
	public String getPlatform()
	{
		return platform;
	}
	public void setPlatform(String platform)
	{
		this.platform = platform;
	}
	public void setStart_version(int start_version)
	{
		this.start_version = start_version;
	}
	public int getNow_version()
	{
		return now_version;
	}
	public void setNow_version(int now_version)
	{
		this.now_version = now_version;
	}
	
	@Override
	public String toString()
	{
		return "HotfixModel [url=" + url + ", start_version=" + start_version + ", now_version=" + now_version + ", platform=" + platform + ", gateway_host=" + gateway_host + ", gateway_port=" + gateway_port + ", big_version=" + big_version + "]";
	}
}

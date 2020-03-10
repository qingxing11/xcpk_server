package com.wt.cmd.user;

import com.wt.cmd.Request;

public class WxLoginRequest extends Request
{

	public String openid;
	public String token;
	public String nickname;
	public int sex;
	public String province;
	public String city;
	public String country;
	public String headimgurl;

	public WxLoginRequest()
	{
		this.msgType = USER_WX_LOGIN;
	}

	public WxLoginRequest(String openid, String token, String nickname, int sex, String province, String city, String country, String headimgurl)
	{
		this.msgType = USER_WX_LOGIN;
		this.openid = openid;
		this.token = token;
		this.nickname = nickname;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.country = country;
		this.headimgurl = headimgurl;
	}

	@Override
	public String toString()
	{
		return "WxLoginRequest [openid=" + openid + ", token=" + token + ", nickname=" + nickname + ", sex=" + sex + ", province=" + province + ", city=" + city + ", country=" + country + ", headimgurl=" + headimgurl + "]";
	}
}

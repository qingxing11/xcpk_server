package com.wt.archive;

import java.io.Serializable;
import java.sql.Timestamp;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.annotation.note.FieldNote;
import com.wt.naval.dao.model.user.GameDataModel;

/**
 * 玩家信息
 */
public class UserData implements Serializable
{
	private static final long serialVersionUID = -7453111802570945614L;

	@FieldNote(info = "用户id,服务器唯一的")
	public int userId;// 用户id --平台中的
	

	@FieldNote(info = "注册时间")
	public Timestamp reg_time;// 注册时间

	public String account;
	
	private String nickName;

	

	@FieldNote(info = "性别")
	public int gender;// 性别
	public static final int MALE = 1, FEMALE = 2, NOTDECIDE = 0;//

	/**
	 * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
	 * http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0
	 * <br>
	 * 战绩使用头像，查看下家时使用头像，
	 */
	public String headImgurl = "";

	@FieldNote(info = "上次登录时间")
	public Timestamp last_login_time;// 上次登录时间

	@FieldNote(info = "账号状态")
	public int accountStatus;// 账号状态

	/** 玩家真实姓名 */
	public String re_user_name;

	@FieldNote(info = "绑定的手机号")
	public String mobile_num;

	public String open_id;
	
	public int  guildId;
	
//	public transient int autoAddId;

	@FieldNote(info = "上次上传存档时间")
	public long lastUpdateTime;

	/**上次登出时间*/
	public Timestamp lastLogoutTime;
	
	public byte[] headIcon;
	public String getOpen_id()
	{
		return open_id;
	}

	public void setOpen_id(String open_id)
	{
		this.open_id = open_id;
	}

	private transient byte[] zxing_img;

	
	/**
	 * 比赛回放数量
	 */
	public int replayNum;


	public byte[] getZxingImg()
	{
		return zxing_img;
	}

	public void setZxingImg(byte[] zxingImg)
	{
		this.zxing_img = zxingImg;
	}

	

	public UserData()
	{}


	public String getOpenId()
	{
		return open_id;
	}


	public void setOpenId(String openId)
	{
		this.open_id = openId;
	}

	@Override
	public String toString()
	{
		String json = JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
		return json;
	}

	public int getUserId()
	{
		return userId;
	}

	public void setUserId(int userId)
	{
		this.userId = userId;
	}

	public int getGender()
	{
		return gender;
	}

	public void setGender(int gender)
	{
		this.gender = gender;
	}

	public String getHeadImgurl()
	{
		return headImgurl;
	}

	public void setHeadImgurl(String headImgurl)
	{
		this.headImgurl = headImgurl;
	}

	public int getAccountStatus()
	{
		return accountStatus;
	}

	public void setAccountStatus(int accountStatus)
	{
		this.accountStatus = accountStatus;
	}

	public String getRe_user_name()
	{
		return re_user_name;
	}

	public void setRe_user_name(String re_user_name)
	{
		this.re_user_name = re_user_name;
	}

	public String getMobile_num()
	{
		return mobile_num;
	}

	public void setMobile_num(String mobile_num)
	{
		this.mobile_num = mobile_num;
	}


	public byte[] getZxing_img()
	{
		return zxing_img;
	}

	public void setZxing_img(byte[] zxing_img)
	{
		this.zxing_img = zxing_img;
	}

	public int getReplayNum()
	{
		return replayNum;
	}

	public void setReplayNum(int replayNum)
	{
		this.replayNum = replayNum;
	}

	public static long getSerialversionuid()
	{
		return serialVersionUID;
	}

	public static int getMale()
	{
		return MALE;
	}

	public static int getFemale()
	{
		return FEMALE;
	}

	public static int getNotdecide()
	{
		return NOTDECIDE;
	}
	
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}

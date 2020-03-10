package com.yt.cmd.antiaddiction;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;
import com.wt.util.security.token.TokenVO;

public class RealNameAuthenticationRequest extends Request
{	
	public int isAdult; // 1为未成年  2为成年
	
	public TokenVO tokenVO;
	public RealNameAuthenticationRequest()
	{
		this.msgType = MsgTypeEnum.RealNameAuthentication.getType();
	}
	public RealNameAuthenticationRequest(int isAdult)
	{
		this.msgType = MsgTypeEnum.RealNameAuthentication.getType();
		this.isAdult=isAdult;
	}
	@Override
	public String toString()
	{
		return "RealNameAuthenticationRequest [isAdult=" + isAdult + ", msgType=" + msgType + "]";
	}
}

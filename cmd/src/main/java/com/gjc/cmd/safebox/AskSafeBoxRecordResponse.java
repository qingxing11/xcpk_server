package com.gjc.cmd.safebox;

import java.util.ArrayList;
import java.util.Arrays;

import com.gjc.naval.vo.safebox.SafeBoxRecordVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskSafeBoxRecordResponse extends Response
{
	public ArrayList<SafeBoxRecordVO> list;
	
	public AskSafeBoxRecordResponse(int code,ArrayList<SafeBoxRecordVO> list) 
	{
		this.msgType= MsgTypeEnum.SafeBox_银行记录.getType();
		this.code=code;
		this.list = list;
	}

	public AskSafeBoxRecordResponse(int code)
	{
		this.msgType= MsgTypeEnum.SafeBox_银行记录.getType();
		this.code=code;
	}
	
	public AskSafeBoxRecordResponse()
	{
		this.msgType= MsgTypeEnum.SafeBox_银行记录.getType();
	}
	
	@Override
	public String toString() {
		return "AskSafeBoxRecordResponse [list=" + list + ", msgType=" + msgType + ", data=" + Arrays.toString(data)
				+ ", callBackId=" + callBackId + ", code=" + code + "]";
	}

}

package com.gjc.cmd.safebox;

import java.util.ArrayList;
import java.util.Arrays;

import com.gjc.naval.vo.safebox.SafeBoxRecordVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushSafeBoxToOtherResponse extends Response
{
	public ArrayList<SafeBoxRecordVO> po;
	public boolean show;//仅显示
	public float transferAccountsPer;//转账手续费
	
	public PushSafeBoxToOtherResponse() 
	{
		this.msgType= MsgTypeEnum.SafeBox_某人转账给玩家.getType();
	}

	public PushSafeBoxToOtherResponse(SafeBoxRecordVO po,boolean show) 
	{
		this.msgType= MsgTypeEnum.SafeBox_某人转账给玩家.getType();
		this.code=PushSafeBoxToOtherResponse.SUCCESS;
		ArrayList<SafeBoxRecordVO> vo=new ArrayList<SafeBoxRecordVO>();
		vo.add(po);
		this.po = vo;
		this.show=show;
	}

	public PushSafeBoxToOtherResponse(ArrayList<SafeBoxRecordVO> po,boolean show,float transferAccountsPer) 
	{
		this.msgType= MsgTypeEnum.SafeBox_某人转账给玩家.getType();
		this.code=PushSafeBoxToOtherResponse.SUCCESS;
		this.po = po;
		this.show=show;
		this.transferAccountsPer=transferAccountsPer;
	}

	@Override
	public String toString() {
		return "PushSafeBoxToOtherResponse [po=" + po + ", show=" + show + ", transferAccountsPer="
				+ transferAccountsPer + ", msgType=" + msgType + ", data=" + Arrays.toString(data) + ", callBackId="
				+ callBackId + ", code=" + code + "]";
	}
	
	
}

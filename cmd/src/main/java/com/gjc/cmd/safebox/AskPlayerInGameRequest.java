package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskPlayerInGameRequest extends Request
{
   public AskPlayerInGameRequest() 
   {
	   this.msgType= MsgTypeEnum.Player_进入游戏主界面.getType();
   }

	@Override
	public String toString() 
	{
		return "AskPlayerInGameRequest [msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
   
}

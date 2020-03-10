package com.gjc.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushShortCutResponse extends Response
{
			public int userId;//消息来源Id
			public String nikeName;
			public int vipLv;
			public String info;
			
			public PushShortCutResponse() 
			{
				this.msgType= MsgTypeEnum.Chat_快捷回复.getType();
			}
			public PushShortCutResponse(int code,int userId,String nikeName,int vipLv,String info) 
			{
				this.msgType= MsgTypeEnum.Chat_快捷回复.getType();
				this.code=code;
				this.nikeName=nikeName;
				this.vipLv=vipLv;
				this.info=info;
				this.userId=userId;
			}

			@Override
			public String toString() {
			return "PushShortCutResponse  快捷回复[消息来源 id=" + userId +",info="+info+", nikeName="+nikeName+",vipLv="+vipLv+ "]";
			}
}

package com.gjc.cmd.friends;

import java.util.ArrayList;

import com.wt.archive.FriendInfoVO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushFriendsInfoResponse extends Response
{
		public ArrayList<FriendInfoVO> info;
		
		public PushFriendsInfoResponse() 
		{
			this.msgType=MsgTypeEnum.Info_消息通知.getType();
		}
		public PushFriendsInfoResponse(int code,ArrayList<FriendInfoVO> info) 
		{
			this.msgType=MsgTypeEnum.Info_消息通知.getType();
			this.code=code;
			this.info=info;
		}
		
		@Override
		public String toString()
		{
			return "PushFriendsInfoResponse [info=" + info+"]";
		}
}

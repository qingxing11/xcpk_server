package com.gjc.cmd.chat;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class AskFenestraeChatResponse extends Response {
	public static final int ROOM_通杀 = 0;
	public static final int ROOM_经典 = 1;
	public static final int ROOM_万人场 = 2;
	
	public static final int Error_暂无聊天功能 = 0;
	public static final int Error_频繁发送 = 1;

	public int userId;// 消息来源Id
	public String nikeName;
	public int vipLv;
	public String info;
	public String headIconUrl;
	public int lv;
	public int headId;
	public int roelId;
	public boolean enjoy;
	public boolean shutCut;
	public int shutCutIndex;
	public int curRoom;
	
	public int roomType;

	public AskFenestraeChatResponse(int code,int userId, String nikeName, int vipLv, String info,String headIconUrl, int lv,
			int headId, int roelId, boolean enjoy, boolean shutCut, int shutCutIndex, int curRoom,int roomType) {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
		this.code = code;
		this.userId = userId;
		this.nikeName = nikeName;
		this.vipLv = vipLv;
		this.info = info;
		this.headIconUrl = headIconUrl;
		this.lv = lv;
		this.headId = headId;
		this.roelId = roelId;
		this.enjoy = enjoy;
		this.shutCut = shutCut;
		this.shutCutIndex = shutCutIndex;
		this.curRoom=curRoom;
		this.roomType = roomType;
	}

	public AskFenestraeChatResponse(int code) {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
		this.code = code;
	}

	public AskFenestraeChatResponse() {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
	}

	@Override
	public String toString() {
		return "AskFenestraeChatResponse [userId=" + userId + ", nikeName=" + nikeName + ", vipLv=" + vipLv + ", info="
				+ info + ", lv=" + lv + ", headId=" + headId + ", roelId=" + roelId + ", enjoy="
				+ enjoy + ", shutCut=" + shutCut + ", shutCutIndex=" + shutCutIndex + ", curRoom=" + curRoom
				+ ", msgType=" + msgType  + ", callBackId=" + callBackId + ", code="
				+ code + "]";
	}
}

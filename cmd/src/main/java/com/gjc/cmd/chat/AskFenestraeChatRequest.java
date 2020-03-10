package com.gjc.cmd.chat;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskFenestraeChatRequest extends Request {
	public static final int ROOM_通杀 = 0;
	public static final int ROOM_经典 = 1;
	public static final int ROOM_万人场 = 5;
	public static final int ROOM_水果机 = 6;
	
	public String info;
	public boolean enjoy;
	public boolean shutCut;
	public int shutCutIndex;
	public int curRoom;
	public int roomType;
	
	public AskFenestraeChatRequest(String info,  boolean shutCut, int shutCutIndex,int curRoom) {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
		this.info = info;
		this.enjoy = false;
		this.shutCut = shutCut;
		this.shutCutIndex = shutCutIndex;
		this.curRoom=curRoom;
	}

	public AskFenestraeChatRequest() {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
	}

	public AskFenestraeChatRequest(String info,int curRoom) {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
		this.info = info;
		this.enjoy = false;
		this.shutCut=false;
		this.shutCutIndex=-1;
		this.curRoom=curRoom;
	}

	public AskFenestraeChatRequest(String info, boolean enjoy,int curRoom) {
		this.msgType = MsgTypeEnum.Chat_小窗聊天.getType();
		this.info = info;
		this.enjoy = enjoy;
		this.shutCut=false;
		this.shutCutIndex=-1;
		this.curRoom=curRoom;
	}

	@Override
	public String toString() {
		return "AskFenestraeChatRequest [info=" + info + ", enjoy=" + enjoy + ", shutCut=" + shutCut + ", shutCutIndex="
				+ shutCutIndex + ", curRoom=" + curRoom + ", msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
}

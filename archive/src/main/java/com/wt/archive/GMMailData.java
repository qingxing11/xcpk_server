package com.wt.archive;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.annotation.note.FieldNote;

public class GMMailData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8711871564275756752L;
	public static final byte cond_指定玩家 = 0;
	public static final byte cond_全服玩家 = 1;
	// ...

	public static final byte status_无效 = 0;
	public static final byte status_有效 = 1;

	@FieldNote(info = "邮件接收类型")
	public byte condType;

	@FieldNote(info = "邮件接收条件")
	public String condContent;

	@FieldNote(info = "收件人id")
	public int toUserId;

	@FieldNote(info = "消息类型")
	public byte type;

	@FieldNote(info = "发件人名字")
	public String fromNick = "";

	@FieldNote(info = "发件时间")
	public long time;

	@FieldNote(info = "时长")
	public int duration;

	@FieldNote(info = "标题")
	public String title = "";

	@FieldNote(info = "内容")
	public String content = "";

	@FieldNote(info = "附件类型")
	public byte attachmentType;

	@FieldNote(info = "附件物品内容")
	public String itemType = "";

	@FieldNote(info = "附件物品数量")
	public String itemNum = "";

	@FieldNote(info = "邮件状态(是否过期)")
	public byte status;

	@FieldNote(info = "邮件id")
	public int mailId;

	@Override
	public String toString() {
		return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
	}
}

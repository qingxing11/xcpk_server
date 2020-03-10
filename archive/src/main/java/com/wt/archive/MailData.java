package com.wt.archive;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.annotation.note.FieldNote;

public class MailData implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8348134664001217810L;

	public static final byte STATUS_状态未读 = 0;
	public static final byte STATUS_状态已读 = 1;
	public static final byte STATUS_状态已删除 = 2;

	public static final byte TYPE_系统 = 0;

    public static final byte ATTACHMENT_不带附件  = 0;
    public static final byte ATTACHMENT_带附件    = 1;
    public static final byte ATTACHMENT_附件已领取 = 2;

    public static final byte TIME_当前时间 = 0;
	
	@FieldNote(info = "收件人id")
	public int toUserId;
	
	@FieldNote(info = "消息类型")
	public byte type;
	
	@FieldNote(info = "发件人名字")
	public String fromNick = "";
	
	@FieldNote(info = "发件时间")
	public long time;
	
	@FieldNote(info = "标题")
	public String title = "";
	
	@FieldNote(info = "内容")
	public String content = "";
	
	@FieldNote(info = "邮件状态")
	public byte status;

	@FieldNote(info = "附件类型")
	public byte attachmentType;
	
	@FieldNote(info = "附件物品内容")
	public String itemType = "";
	
	@FieldNote(info = "附件物品数量")
	public String itemNum = "";

	@FieldNote(info = "邮件id")
	public int mailId;


    public void init(int toUserId, byte type, String fromNick,
                     long time, String title, String content,
                     byte attachmentType, String itemType, String itemNum)
    {
        if (attachmentType == ATTACHMENT_带附件)
        {
           init(toUserId, type, fromNick, title, content, itemType, itemNum);
        }
        else
        {
           init(toUserId, type, fromNick, title, content);
        }

        //自定义发送时间
        if (time != TIME_当前时间)
        {
           this.time = time;
        }
    }

    /**
     * 不带附件的邮件
     * @param toUserId
     * @param type
     * @param fromNick
     * @param title
     * @param content
     */
	public void init(int toUserId, byte type, String fromNick,
                     String title, String content)
    {
        this.toUserId   = toUserId;
		this.type       = type;
        this.fromNick   = fromNick;
        this.title      = title;
        this.content    = content;
        this.status     = STATUS_状态未读;
        this.itemType   = "";
        this.itemNum    = "";
        this.time       = System.currentTimeMillis()/1000;
        this.attachmentType = ATTACHMENT_不带附件;
    }

    /**
     * 带附件的邮件
     * @param toUserId
     * @param type
     * @param fromNick
     * @param title
     * @param content
     * @param itemType
     * @param itemNum
     */
    public void init(int toUserId, byte type, String fromNick,
                     String title, String content, String itemType,
                     String itemNum)
    {
        this.toUserId   = toUserId;
        this.type       = type;
        this.fromNick   = fromNick;
        this.title      = title;
        this.content    = content;
        this.status     = STATUS_状态未读;
        this.itemType   = itemType;
        this.itemNum    = itemNum;
        this.time       = System.currentTimeMillis()/1000;
        this.attachmentType = ATTACHMENT_带附件;
    }

    public void setId(int id)
    {
        mailId = id;
    }

    @Override
    public String toString()
    {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}



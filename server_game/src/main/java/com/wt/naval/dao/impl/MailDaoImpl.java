package com.wt.naval.dao.impl;

import java.util.ArrayList;

import com.brc.naval.mail.model.MailModel;
import com.wt.archive.MailDataPO;
import com.wt.db.SqlSimpleUtil;

public class MailDaoImpl 
{
	public static final String ALLMAILS="SELECT * FROM `mail` ";
	/**
	 * 获取所有邮件
	 * */
	public static ArrayList<MailModel> getAllMailI()
	{
		return(ArrayList<MailModel>)SqlSimpleUtil.selectBeanList(ALLMAILS,MailModel.class);
	}
	
	private static final String INSERT_MAIL = "INSERT INTO `mail`(`userId`,`sendtime`,`title`,`content`,`attachContent`,`attachState`) VALUES (?,?,?,?,?,?)";
	/**
	 * 添加一封新的邮件
	 */
	public static int insertMail(int userId,String timer, String title, String content,String attachContent,int attachState)
	{
		return SqlSimpleUtil.insert(INSERT_MAIL, userId,timer,title,content,attachContent,attachState);
	}
	
	private static final String REATAMAIL="UPDATE `mail` SET `state` =1 WHERE `mailId` = ?";
	/**
	 * 读取一封邮件
	 */
	public static int updateMailState(int mailId)
	{
		return SqlSimpleUtil.update(REATAMAIL,mailId);
	}
	
	private static final String GETATTACH="UPDATE `mail` SET `attachState` =2 WHERE `mailId` = ?";
	/**
	 * 领取一封邮件
	 */
	public static int updateMailAttachState(int mailId)
	{
		return SqlSimpleUtil.update(GETATTACH,mailId);
	}
	
	private static final String DELETEMAIL="DELETE  FROM `mail` WHERE `mailId` = ?";
	/**
	 * 删除一封邮件
	 */
	public static int deleteMail(int mailId)
	{
		return SqlSimpleUtil.update(DELETEMAIL,mailId);
	}
}

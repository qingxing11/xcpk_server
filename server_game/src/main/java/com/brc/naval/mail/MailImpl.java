package com.brc.naval.mail;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.brc.cmd.mail.Attach;
import com.brc.naval.mail.model.MailModel;
import com.wt.archive.MailDataPO;
import com.wt.cmd.MsgTypeEnum;
import com.wt.naval.dao.impl.MailDaoImpl;
import com.wt.naval.vo.user.Player;
import com.wt.util.Tool;
import com.yt.xcpk.task.TaskService;
/**
 * @author 包小威
 * 邮件实现类
 */
@Service
public class MailImpl implements MailService
{
	@Autowired
	private TaskService taskService;
	
	// 服务器内存中的全服邮件
	//private ArrayList<MailDataPO> allMails = new ArrayList<MailDataPO>();
	private HashMap<Integer, MailDataPO>map_allMails=new HashMap<Integer, MailDataPO>();
	@PostConstruct
	private void init() // 初始化方法
	{
		ArrayList<MailModel> mailModels = MailDaoImpl.getAllMailI(); // 所有的系统邮件
		for (MailModel mailModel : mailModels)
		{
			if(mailModel!=null)
			{
				MailDataPO mailDataPO=new MailDataPO(mailModel.getMailId(), mailModel.getUserId(), mailModel.getSendTime(), 
						mailModel.getTitle(), mailModel.getContent(), mailModel.getAttachContent(), mailModel.getState(), mailModel.getAttachState());
				map_allMails.put(mailDataPO.getMailID(), mailDataPO);
			}
		}
	}
	
	// 向玩家发送一封邮件
	@Override
	public void sendMail(int userId,String title, String content, String attachContent)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
		String timer = df.format(new Date()).toString(); // 邮件的时间	
		int attachState;        //附件状态
		if(attachContent==null||attachContent.length()<=0)
		{
			attachState=MailDataPO.ATTACHSTATE_无附件;
		}
		else
		{
			attachState=MailDataPO.ATTACHSTATE_未领取;
		}
		int mailID=MailDaoImpl.insertMail(userId, timer,  title,  content, attachContent,attachState);
	
		Tool.print_debug_level0("发送邮件，userId："+userId+",attachContent:"+attachContent);
		MailDataPO mailDataPO=new MailDataPO(mailID, userId, timer, title, content, attachContent, MailDataPO.STATE_未读, attachState);
		map_allMails.put(mailID, mailDataPO);
	}
	//判断邮件是否存在
	@Override
	public MailDataPO mailExist( int mailId)
	{
		if(map_allMails.containsKey(mailId))
		{
			return map_allMails.get(mailId);
		}
		else {
			return null;
		}
	}
	//判断邮件是否是已读状态
	@Override
	public boolean isRead(MailDataPO mailDataPO)
	{
		return mailDataPO.getState()==MailDataPO.STATE_已读;
	}
	//读取邮件
	@Override
	public void readMail(Player player, MailDataPO mailDataPO)
	{
		if(mailDataPO.getState()==MailDataPO.STATE_未读)
			mailDataPO.setState(MailDataPO.STATE_已读); //改变内存中的数据             
		MailDaoImpl.updateMailState(mailDataPO.getMailID());    //改变数据库中的邮件数据
	}
	//玩家获取所有邮件
	@Override
	public ArrayList<MailDataPO>getUserMailDataPOs(int userId)
	{
		ArrayList<MailDataPO>mailDataPOs=new ArrayList<MailDataPO>();
		for (MailDataPO mailDataPO : map_allMails.values()) 
		{
			if(mailDataPO!=null&&mailDataPO.getUserId()==userId)
				mailDataPOs.add(mailDataPO);
		}
		return mailDataPOs;
	}
	
	//删除一封邮件
	@Override
	public void removeMail(int mailId)
	{
		map_allMails.remove(mailId);
		MailDaoImpl.deleteMail(mailId);
	}
	//判断邮件附件是否能领取
	@Override
	public boolean isCanReceive(MailDataPO mailDataPO)
	{
		return mailDataPO.getAttachState()==MailDataPO.ATTACHSTATE_未领取;
	}
	
	
	//领取附件
	@Override
	public void getAttach(MailDataPO mailDataPO) 
	{
		Tool.print_debug_level0(MsgTypeEnum.MAIL_GETATTACH,"领取玩家:"+mailDataPO.getUserId()+",附件内容:"+mailDataPO.getAttachContent());
		Attach attach=(Attach)JSON.parseObject(mailDataPO.getAttachContent(), Attach.class);
		taskService.setPlayerAttachRewardInfo(mailDataPO.getUserId(), attach.id, attach.num);
		mailDataPO.setAttachState(MailDataPO.ATTACHSTATE_已领取);
		MailDaoImpl.updateMailAttachState(mailDataPO.getMailID());
	}
}

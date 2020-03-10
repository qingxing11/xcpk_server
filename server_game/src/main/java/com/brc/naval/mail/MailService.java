package com.brc.naval.mail;

import java.util.ArrayList;

import com.wt.archive.MailDataPO;
import com.wt.naval.vo.user.Player;

/**
 * @author 包小威
 * 邮件接口
 */
public interface MailService {
	/**
	 * 向玩家发送一封邮件
	 */
	public void sendMail(int userId,String title, String content, String attachContent) ;
	
	/**
	 * 判断邮件是否存在
	 */
	public MailDataPO mailExist(int mailId);
	
	/**
	 *判断邮件是否是已读状态
	 */
	public boolean isRead(MailDataPO mailDataPO);
	
	/**
	 * 读取邮件
	 */
	public void readMail(Player player, MailDataPO mailDataPO);
	/**
	 * 玩家获取所有邮件
	 */
	public ArrayList<MailDataPO>getUserMailDataPOs(int userId);
	
	/**
	 * 删除一封邮件
	 */
	public void removeMail(int mailId);
	
	/**
	 * 判断附件是能领取
	 */
	public boolean isCanReceive(MailDataPO mailDataPO);
	
	/**
	 * 领取附件
	 */
	public void getAttach(MailDataPO mailDataPO);
}

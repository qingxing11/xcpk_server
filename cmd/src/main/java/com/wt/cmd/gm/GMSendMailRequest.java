package com.wt.cmd.gm;

import com.wt.archive.GMMailData;
import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GMSendMailRequest extends Request
{
    public GMMailData mailData;

    public GMSendMailRequest()
    {
        msgType = MsgType.GM_发送邮件;
    }

    public GMSendMailRequest(GMMailData mailData)
    {
        msgType = MsgType.GM_发送邮件;
        this.mailData = mailData;
    }
}

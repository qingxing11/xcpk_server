package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GMSendMailResponse extends Response
{
    public GMSendMailResponse()
    {
        this.msgType = MsgType.GM_发送邮件;
    }

    /**
     * @param code 返回码
     */
    public GMSendMailResponse(int code) {
        this.msgType = MsgType.GM_发送邮件;
        this.code = code;
    }
}

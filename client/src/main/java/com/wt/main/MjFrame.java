package com.wt.main;

import com.wt.client.ClientHelper;
import com.wt.cmd.test.TestRequest;
import com.wt.io.MySerializerUtil;
import com.wt.util.MyTimeUtil;

public class MjFrame implements Runnable
{
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjBVs124Jq3RI1AL+fgED1 APgYSMPTfUxBKlMV/PTB9Li54XwKT1O8scjam2yLF3VjKRQz6est3Wwc+BKr 28vqiRbkah2N5PRoaO7WIrefTiDfyL7XSWtwviY/pJgyWatmdu0ZXAMQQDsg G61dOjCFtN/vX3YpKFW32toUMqU6kQIDAQAB";

	public static final int STATE_CONNECT = 0;
	public static final int STATE_TEST1 = 1;
	public static final int STATE_CHAT = 2;
	public static final int STATE_REGISTER = 3;
	public static final int STATE_LOGIN = 4;
	public static final int STATE_WXLOGIN = 5;
	
	public static int state;
	
	public static void main(String[] args)
	{
		for (int i = 0 ; i < 10 ; i++)
		{
			new MjFrame().testConnect();
		}
		
	}
	
	ClientHelper clientHelper;
	public MjFrame()
	{
		clientHelper = new ClientHelper();
	}
	

	public void testConnect()
	{
		startTime = MyTimeUtil.getCurrTimeMM();
		TestRequest testRequest = new TestRequest();
		testRequest.name = "testRequest";
		
		testRequest.data = MySerializerUtil.serializer_protobufIOUtil(testRequest);//序列化成protobuf-net
		clientHelper.sendRequest(testRequest);
	}
	
	private static long startTime;
	public static void response()
	{
		System.out.println("use:"+(MyTimeUtil.getCurrTimeMM() - startTime));
	}

	private void runTest1()
	{}
	
	private Thread thread;
	public void start()
	{
		if(thread == null)
		{
			thread = new Thread(this);
		}
		thread.start();
	}
		
	public void join()
	{
		try
		{
			thread.join();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
	
	private boolean isRun;
	public void run()
	{
		while (true)
		{
			if(isRun)
			{
				isRun = false;
				switch (state)
				{
					case STATE_WXLOGIN:
						runWxLoing();
						break;
					
					case STATE_LOGIN:
						runLogin();
						break;
				
					case STATE_CONNECT:
						testConnect();
						break;

					case STATE_TEST1:
						runTest1();
						break;
						
					case STATE_CHAT:
						runTestChat();
						break;
						
					case STATE_REGISTER:
						runRegister();
						break;
						
					default:
						break;
				}
			}
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}

	private void runWxLoing()
	{}
	
	public static  void reStart()
	{}

	public UserData userData;
	private void runLogin() {}

	private void runRegister() {}

	private void runTestChat() {}

	public void connect()
	{
		state = STATE_CONNECT;
		isRun = true;
	}

	public void test1()
	{
		state = STATE_TEST1;
		isRun = true;
	}

	private String chat_txt;
	public void testChat(String txt) {
		state = STATE_CHAT;
		isRun = true;
		this.chat_txt = txt;
	}

	private String register_txt;
	public void testRegister(String str) {
		state = STATE_REGISTER;
		isRun = true;
		
		this.register_txt = str;
	}

	private String login_txt;
	public void testLogin(String str) {
		state = STATE_LOGIN;
		isRun = true;
		
		this.login_txt = str;
	}

	private String wxLogin_txt;
	public void testWxLogin(String str)
	{
		System.out.println("testWxLogin:");
		state = STATE_WXLOGIN;
		isRun = true;
		
		this.wxLogin_txt = str;
	}


	public void close()
	{
		clientHelper.close();
	}
}

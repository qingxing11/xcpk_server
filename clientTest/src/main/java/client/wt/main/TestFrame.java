package client.wt.main;

import java.util.UUID;

import com.wt.cmd.MsgType;
import com.wt.cmd.loadtest.LoadTestConnectRequest;
import com.wt.cmd.test.TestRequest;
import com.wt.cmd.user.GuestLoginAuthRequest;
import com.wt.util.MyTimeUtil;
import com.wt.util.UuidUtil;

import client.wt.client.ClientHelper;

public class TestFrame implements Runnable
{
	public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjBVs124Jq3RI1AL+fgED1 APgYSMPTfUxBKlMV/PTB9Li54XwKT1O8scjam2yLF3VjKRQz6est3Wwc+BKr 28vqiRbkah2N5PRoaO7WIrefTiDfyL7XSWtwviY/pJgyWatmdu0ZXAMQQDsg G61dOjCFtN/vX3YpKFW32toUMqU6kQIDAQAB";

	public static final int STATE_CONNECT = 0;
	public static final int STATE_TEST1 = 1;
	public static final int STATE_CHAT = 2;
	public static final int STATE_REGISTER = 3;
	public static final int STATE_LOGIN = 4;
	public static final int STATE_WXLOGIN = 5;
	private static final int STATE_LOOPLOGIN = 6;
	
	public static int state;
	
	ClientHelper clientHelper;
	public TestFrame()
	{
		clientHelper = new ClientHelper();
	}
	
	public void testConnect()
	{
		startTime = MyTimeUtil.getCurrTimeMM();
		TestRequest testRequest = new TestRequest();
		clientHelper.sendRequest(testRequest);
	}
	
	private static long startTime;
	public static void response(int msgType, byte[] data)
	{
		switch (msgType)
		{
			case MsgType.TEST:
				
				break;
				
			case MsgType.USER_游客快速登录:
			case MsgType.LOADTEST_连接:
				TestMain.addConnectNum();
				break;

			default:
				break;
		}
		System.out.println("use:"+(MyTimeUtil.getCurrTimeMM() - startTime));
	}

	private void runTest1()
	{
		startTime = MyTimeUtil.getCurrTimeMM();
		TestRequest testRequest = new TestRequest();
		clientHelper.sendRequest(testRequest);
	}
	
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
					
					case STATE_LOOPLOGIN:
						runLoopLogin();
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
				e.printStackTrace();
			}
		}
	}

	private void runWxLoing()
	{}
	
	public static  void reStart()
	{}

	private void runLogin() {
		LoadTestConnectRequest request  = new LoadTestConnectRequest();
		request.device_code = UuidUtil.getRandStr(12);
		clientHelper.sendRequest(request);
	}
	
	private void runLoopLogin()
	{
//		LoadTestConnectRequest request  = new LoadTestConnectRequest();
//		request.device_code = UUID.randomUUID().toString();
//		clientHelper.sendRequest(request);
		
		
		GuestLoginAuthRequest request = new GuestLoginAuthRequest();
		request.device_code = UUID.randomUUID().toString();
		clientHelper.sendRequest(request);
	}
	
	

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

	public void testLogin() {
		state = STATE_LOGIN;
		isRun = true;
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


	public void loopLogin()
	{
		state = STATE_LOOPLOGIN;
		isRun = true;
	}
}

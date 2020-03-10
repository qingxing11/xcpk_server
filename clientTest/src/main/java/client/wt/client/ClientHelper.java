package client.wt.client;

import com.wt.cmd.Request;

import io.netty.channel.Channel;

public class ClientHelper
{
	public static String SERVER_HOST = "58.215.191.193";
	public static int SERVER_PORT = 1985;
	// 开始时间
	private static long startTime = System.currentTimeMillis();

	public Channel gameChannel = null;

	// 初始化服务器连接（后期需要增加自动/手动选择节点功能）

	Client gameClient;
	public void initGameClient()
	{
		if (gameChannel == null || !gameChannel.isOpen())
		{
			gameClient = new Client();
//			System.out.println("通道已关闭，重建，SERVER_HOST:" + SERVER_HOST + ":" + SERVER_PORT);
			gameChannel = gameClient.createChannel(SERVER_HOST, SERVER_PORT);
		}

		if (gameChannel != null)
		{// 创建连接成功
//			TestMain.addConnectNum();
			gameClient.setConnectStatus(true);
		}
	}
	
	public void close()
	{
		gameChannel.close();
	}

	private static long lastSendTimeMM;


	// 客户端发送请求
	public void sendRequest(Request request, boolean shortLink)
	{
		initGameClient();// 每次发送信息前都检查

		// byte[] data = requestToByteArray(request);
		if (shortLink)
		{
			gameClient.sendMsgShortLink(SERVER_HOST, SERVER_PORT, request);
		}
		else
		{// 使用保存的channel
			gameClient.sendMsg(gameChannel, request);
		}
	}
	
	public void sendRequest(Request request)
	{
		sendRequest(request, false);
	}

	// 记录流量用
	public static int totalGetBytes;
	public static int totalSendBytes;
	// 将request包装为byte流发送出去
	private static byte[] requestToByteArray(Request obj)
	{
		byte[] bytes = null;

		// byte[] encodedData = RSAUtils.encryptByPublicKey(bytes,
		// publicKey);// 加密
		// //
		// System.out.println("加密前后数据量对比："+bytes.length+"/"+encodedData.length);
		// byte[] compressedData = GZipUtils.compress(encodedData);//压缩

		totalSendBytes += bytes.length;
		return bytes;
	}
}

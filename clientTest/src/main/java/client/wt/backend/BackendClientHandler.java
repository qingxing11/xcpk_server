package client.wt.backend;

public class BackendClientHandler
{
	public static void bufToRequest(int msgType, byte[] data)
	{
		System.out.println("msgType:"+msgType);
		switch (msgType)
		{
			case 0://协议号
				
				break;

			default:
				break;
		}
		ClientHelper.close();
	}
}
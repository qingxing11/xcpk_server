package client.wt.backend;

import com.wt.cmd.Request;

public class TestClient
{
	public static void main(String[] args)
	{
		Request request = new Request();
		request.msgType = -999;
		ClientHelper.sendRequest(request);
		
		request = new Request();
		request.msgType = -999;
		ClientHelper.sendRequest(request);
	}
}

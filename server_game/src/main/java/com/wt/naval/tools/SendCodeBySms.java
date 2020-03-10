package com.wt.naval.tools;


import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wt.util.Tool;

public class SendCodeBySms
{
	public static final String accessKeyId = "LTAIHPnfASosl4lc";
	public static final String keySecret = "r8ombAhv9dxPQsqaBIC4TPoK6O0g1e";
	public static boolean sendCode(String mobileNum, int rand)
	{
		boolean isSuccess = false;
		DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou",accessKeyId,keySecret);
		IAcsClient client = new DefaultAcsClient(profile);

		CommonRequest request = new CommonRequest();
		// request.setProtocol(ProtocolType.HTTPS);
		request.setMethod(MethodType.POST);
		request.setDomain("dysmsapi.aliyuncs.com");
		request.setVersion("2017-05-25");
		request.setAction("SendSms");
		request.putQueryParameter("RegionId", "cn-hangzhou");
		request.putQueryParameter("PhoneNumbers", mobileNum);
		request.putQueryParameter("SignName", "注册验证");
		request.putQueryParameter("TemplateCode", "SMS_5054275");
		request.putQueryParameter("TemplateParam", "{\"code\":\""+rand+"\",\"product\":\"xcpk\"}");
		try
		{
			CommonResponse response = client.getCommonResponse(request);
			System.out.println(response.getData());
			isSuccess= true;
		}
		catch (ServerException e)
		{
			e.printStackTrace();
			Tool.print_error("发送验证码错误："+isSuccess);
			isSuccess = false;
		}
		catch (ClientException e)
		{
			Tool.print_error("发送验证码错误："+isSuccess);
			isSuccess = false;
		}
		return isSuccess;
	}
}

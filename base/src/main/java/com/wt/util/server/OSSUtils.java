package com.wt.util.server;

import java.io.ByteArrayInputStream;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;

public class OSSUtils
{
	public final static String bucketMain = "xcpk-game";
	private static String ACCESS_ID = "LTAI4FiBGReUzWNT5Xmki35g";// naW3jBdFdM7PcmF3
	private static String ACCESS_KEY = "n159hB1vqF9QEbAHUQjlH9O9Fc5lF7";// p3dOKDSVjqR3LCxmGLNab6XFPeT78m
	private static final String endpoint = "http://oss-cn-shanghai.aliyuncs.com"; 

	
	// 上传
	public static void uploadFile(String path, byte[] data)
	{
		// Endpoint以杭州为例，其它Region请按实际情况填写。
		
		// 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。

		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(endpoint, ACCESS_ID,ACCESS_KEY);

		// 上传Byte数组。
		ossClient.putObject(bucketMain, path, new ByteArrayInputStream(data));
		// 关闭OSSClient。
		ossClient.shutdown();
	}
}

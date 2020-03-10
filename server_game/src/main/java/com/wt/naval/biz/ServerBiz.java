package com.wt.naval.biz;

import com.wt.naval.server.GameServerImpl;

public class ServerBiz
{
//	private static void saveServerInfo_object(ServerBean bean)
//	{
//		boolean oneDayPassed=false;
//		
//		ByteArrayOutputStream bout = new ByteArrayOutputStream();
//		DataOutputStream dos = new DataOutputStream(bout);
//		ObjectOutputStream objectOutputStream = null;
//		try
//		{
//			objectOutputStream = new ObjectOutputStream(dos);
//			objectOutputStream.writeObject(bean);
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
//		byte[] bytes = bout.toByteArray();
//		
//		if(Tool.mmToDay(bean.lastUpdateTime)<Tool.mmToDay(Tool.getCurrTimeMM())){
//			oneDayPassed=true;
//		}
//		bean.lastUpdateTime = Tool.getCurrTimeMM();
//		if(oneDayPassed){
//			Date date=new Date(bean.lastUpdateTime);
//			String backup =   "serverinfo_mj/backup/serverdata_"+(date.getYear()+1900)+"_"+(date.getMonth()+1)+"_"+(date.getDate());
//			FileUtils.updataFile(FileUtils.bucketMain, backup, bytes);
//		}
//		String path =   "serverinfo_mj/serverdata";
//		FileUtils.updataFile(FileUtils.bucketMain, path, bytes);
//	}

	public static void saveServerData(GameServerImpl instance)
	{
//		saveServerInfo_object(instance);
	}
	
//	pu		
}
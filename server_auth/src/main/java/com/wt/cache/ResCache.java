package com.wt.cache;

import java.util.ArrayList;
import java.util.HashMap;

import com.wt.biz.ResBiz;
import com.wt.def.ResDef;
import com.wt.model.HotfixModel;
import com.wt.naval.vo.res.HotfixLineVO;
import com.wt.naval.vo.res.HotfixUpdateFileVO;
import com.wt.util.io.FileTool;

public class ResCache
{
	public static final int PLATFORM_ANDROID = 0;
	public static final int PLATFORM_IOS = 1;
	public static final int PLATFORM_WINDOWS = 2;
	
//	public static void main(String[] args)
//	{
//		initHotfix();
//	}
	
//	public static ResDef resDef = new ResDef();

	private static HashMap<Integer,HotfixUpdateFileVO> map_hotfix_android = new HashMap<>();
	private static HashMap<Integer,HotfixUpdateFileVO> map_hotfix_ios = new HashMap<>();
	private static HashMap<Integer,HotfixUpdateFileVO> map_hotfix_windows = new HashMap<>();
	
	public static HotfixUpdateFileVO getUpdateFile(int clientVersion, int clientPlatform)
	{
		switch (clientPlatform)
		{
			case PLATFORM_ANDROID:
				return map_hotfix_android.get(clientVersion);
				
			case PLATFORM_IOS:
				return map_hotfix_ios.get(clientVersion);
				
			case PLATFORM_WINDOWS:
				return map_hotfix_windows.get(clientVersion);

			default:
				return null;
		}
	}
	
	/***
	 * 初始化热更新相关，可供后台接口调用
	 */
	public static void initHotfix()
	{
		ResDef.getResDef().hotfixModel_android = ResBiz.getHotfixModel_android();//配置文件
		ResDef.getResDef().hotfixModel_ios = ResBiz.getHotfixModel_ios();
		ResDef.getResDef().hotfixModel_windows = ResBiz.getHotfixModel_windows();
		
		map_hotfix_android = initHotfix_byPlatform(ResDef.getResDef().hotfixModel_android);
		map_hotfix_ios = initHotfix_byPlatform(ResDef.getResDef().hotfixModel_ios);
		map_hotfix_windows = initHotfix_byPlatform(ResDef.getResDef().hotfixModel_windows);
	}

	private static HashMap<Integer,HotfixUpdateFileVO> initHotfix_byPlatform(HotfixModel hotfixModel)
	{
		HashMap<Integer,HotfixUpdateFileVO> map_hotfix = new HashMap<>();
		
		String path = "./def/hotfix_list/"+hotfixModel.platform+"/"+hotfixModel.now_version+".txt";//最新版本资源配置文件
		System.out.println("path_now:"+path);
		HashMap<String,HotfixLineVO> nowVersion = getVersionTxtToHitfixPo(hotfixModel,path);
//		System.out.println("nowVersion:"+nowVersion);
		for (int i = hotfixModel.start_version ; i < hotfixModel.now_version ; i++)
		{
			path = "./def/hotfix_list/"+hotfixModel.platform+"/"+i+".txt";//其他需要更新的版本配置文件
			System.out.println("path_version:"+path);
			HashMap<String,HotfixLineVO> map_versionPO  = getVersionTxtToHitfixPo(hotfixModel,path);
//			System.out.println("list_versionPO:"+list_versionPO);
			
			HotfixUpdateFileVO fileVO = new HotfixUpdateFileVO();
			fileVO.version = hotfixModel.now_version;
			fileVO.url_path = hotfixModel.url;
//			fileVO.gateway_host = hotfixModel.gateway_host;
//			fileVO.gateway_port = hotfixModel.gateway_port;
			for (String item_now_key : nowVersion.keySet())//最新的
			{
				HotfixLineVO newFile = nowVersion.get(item_now_key);
				if(map_versionPO.containsKey(item_now_key))//旧版本有文件，对比md5
				{
					HotfixLineVO oldVersionFile = map_versionPO.get(item_now_key);
					if(!newFile.md5.equals(oldVersionFile.md5))
					{
						fileVO.list_fileUrl.add(hotfixModel.url+newFile.url);
						fileVO.allSize+=newFile.size;
					}
				}
				else//旧版本没有这跟文件
				{
					fileVO.list_fileUrl.add(hotfixModel.url+newFile.url);
					fileVO.allSize+=newFile.size;
				}
			}
			map_hotfix.put(i, fileVO);
		}
		
		System.out.println(hotfixModel.platform+"=====>"+map_hotfix);
		return map_hotfix;
	}

	private static HashMap<String,HotfixLineVO> getVersionTxtToHitfixPo(HotfixModel hotfixModel,String path)
	{
		ArrayList<String> list_line = FileTool.readFileByLine(path);
		
		HashMap<String,HotfixLineVO> map_hotfixVO = new HashMap<>();
		for (String string : list_line)
		{
//			System.out.println("string:"+string);
			String[] line_info = string.split("\\|");
//			System.out.println("split===>0:"+line_info[0]+",1:"+line_info[1]+",2:"+line_info[2]);
			HotfixLineVO hotfixLinePO = new HotfixLineVO();
			hotfixLinePO.url = line_info[0];
			hotfixLinePO.md5 = line_info[1];
			hotfixLinePO.size = Integer.parseInt(line_info[2]);
			map_hotfixVO.put(hotfixLinePO.url, hotfixLinePO);
		}
		return map_hotfixVO;
	}
}

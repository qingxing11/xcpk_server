package com.wt.naval.biz;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.wt.naval.dao.impl.GMDaoImpl;

public class GMBiz
{
	public static boolean isAdminPhone(String phone)
	{
		return GMDaoImpl.isAdminPhoneNumber(phone);
	}

	public static StringBuffer getLogFile(File file)
	{
		StringBuffer stringBuffer = new StringBuffer();
		BufferedReader br;
		try
		{
			br = new BufferedReader(new FileReader(file));
			String r = br.readLine();
			if(r != null)
				stringBuffer.append(r +"\n");
			while (r != null)
			{
				r = br.readLine();
				stringBuffer.append(r +"\n");
			}
			
			br.close();
			return stringBuffer;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}

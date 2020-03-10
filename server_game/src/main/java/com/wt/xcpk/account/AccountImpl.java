package com.wt.xcpk.account;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.wt.naval.tools.SendCodeBySms;
import com.wt.util.MyUtil;

@Service
public class AccountImpl implements AccountService
{
	private ConcurrentHashMap<Integer,Integer> map_bindPhoneCode = new ConcurrentHashMap<Integer, Integer>(); 
	
	private ConcurrentHashMap<Integer,Integer> map_findPasswordCode = new ConcurrentHashMap<Integer, Integer>(); 
	
	@Override
	public int getBindPhoneCode(int userId,String phoneNum)
	{
		int code = MyUtil.getRandom(1000,9999);
		boolean b = SendCodeBySms.sendCode(phoneNum,code);
		if(b)
		{
			map_bindPhoneCode.put(userId, code);
			return code;
		}
		return -1;
	}

	@Override
	public boolean validateBindPhoneCode(int userId, int code)
	{
		Integer hasCode = map_bindPhoneCode.get(userId);
		if(hasCode != null && hasCode == code)
		{
			map_bindPhoneCode.remove(userId);
			return true;
		}
		return false;
	}

	@Override
	public int getFindPasswordCode(int userId,String phoneNum)
	{
		int code = MyUtil.getRandom(1000,9999);
		boolean b = SendCodeBySms.sendCode(phoneNum,code);
		if(b)
		{
			map_findPasswordCode.put(userId, code);
			return code;
		}
		return -1;
	}

	@Override
	public boolean validateFindPasswordCode(int userId, int code)
	{
		Integer hasCode = map_findPasswordCode.get(userId);
		if(hasCode != null && hasCode == code)
		{
			map_findPasswordCode.remove(userId);
			return true;
		}
		return false;
	}
}

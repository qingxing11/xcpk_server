package com.wt.naval.main;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wt.xcpk.zjh.logic.PokerLogicService;

@Service
public class MyGlobalService
{
	private static MyGlobalService inst;
	
	@Autowired
	private PokerLogicService pokerLogicService;
	public static PokerLogicService getPokerLogicService()
	{
		return inst.pokerLogicService;
	}
	
	
	@PostConstruct
	private void init()
	{
		inst = this;
	}
}

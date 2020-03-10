package com.wt.server.config;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.wt.naval.dao.impl.ManyPopelRoomDaoImpl;

import model.ConfigManypepolroompoker;

@Service
public class ConfigImpl implements ConfigService
{
	private ConfigManypepolroompoker configManypepolroompoker;
	
	
	@PostConstruct
	private void init()
	{
		configManypepolroompoker = ManyPopelRoomDaoImpl.getConfigManypepolGame();
	}


	@Override
	public ConfigManypepolroompoker getConfigManypepolroompoker()
	{
		return configManypepolroompoker;
	}
}

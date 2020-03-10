package com.wt.main;

import java.util.ArrayList;

import org.springframework.boot.SpringApplication;import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.netflix.discovery.shared.Applications;
import com.netflix.eureka.EurekaServerContextHolder;
import com.netflix.eureka.registry.PeerAwareInstanceRegistry;
import com.wt.naval.vo.ServerInfoVO;

@SpringBootApplication
@EnableEurekaServer
@RestController
public class CenterEurekaMain
{
	public static void main(String[] args)
	{
		SpringApplication.run(CenterEurekaMain.class, args);
	}

	@RequestMapping("/getServerList")
	public String getRegistered()
	{
		ArrayList<ServerInfoVO> list_serverInfoVO = new ArrayList<>();
		
		PeerAwareInstanceRegistry registry = EurekaServerContextHolder.getInstance().getServerContext().getRegistry();
		Applications applications = registry.getApplications();
		applications.getRegisteredApplications().forEach((registeredApplication) -> {
			registeredApplication.getInstances().forEach((instance) -> {
				ServerInfoVO serverInfoVO = new ServerInfoVO();
				serverInfoVO.host = instance.getHostName();
				serverInfoVO.name = instance.getAppName();
				serverInfoVO.port = instance.getPort();
				
				list_serverInfoVO.add(serverInfoVO);
//				System.out.println(instance.getAppName() + " (" + instance.getInstanceId() + ") ");
			});
		});

		String list_str = JSONObject.toJSONString(list_serverInfoVO);
		return  list_str;
	}
}
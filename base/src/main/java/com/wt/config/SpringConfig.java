package com.wt.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@ComponentScan(basePackages = {"com","data.data"},excludeFilters ={@ComponentScan.Filter(type= FilterType.ANNOTATION,value=Configuration.class)})
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
public class SpringConfig implements AsyncConfigurer {
}

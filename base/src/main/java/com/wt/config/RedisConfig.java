package com.wt.config;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig
{
	private static RedisConfig inst;
	
	public static RedisConfig inst()
	{
		if(inst == null)
		{
			synchronized (RedisConfig.class)
			{
				if(inst == null)
				{
					inst = new RedisConfig();
					BaseConfig.init(inst, "config/redis.properties");
				}
			}
		}
		return inst;
	}
	 
	//Matser的ip地址  
	public String hostName;
	//端口号  
	public int port;
	//如果有密码  
	public String password;
	//客户端超时时间单位是毫秒 默认是2000 
	public int timeout;

	//最大空闲数  
	public int maxIdle;
	//连接池的最大数据库连接数。设为0表示无限制,如果是jedis 2.4以后用public int.maxTotal  
	//public int.maxActive=600  
	//控制一个pool可分配多少个jedis实例,用来替换上面的public int.maxActive,如果是jedis 2.4以后用该属性  
	public int maxTotal;
	//最大建立连接等待时间。如果超过此时间将接到异常。设为-1表示无限制。  
	public int maxWaitMillis;
	//连接的最小空闲时间 默认1800000毫秒(30分钟)  
	public int minEvictableIdleTimeMillis;
	//每次释放连接的最大数目,默认3  
	public int numTestsPerEvictionRun;
	//逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1  
	public int timeBetweenEvictionRunsMillis;
	//是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个  
	public boolean testOnBorrow;
	//在空闲时检查有效性, 默认false  
	public boolean testWhileIdle;
	
	public JedisPoolConfig getPoolConfig()
	{
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		// 最大空闲数
		jedisPoolConfig.setMaxIdle(inst.maxIdle);
		// 连接池的最大数据库连接数
		jedisPoolConfig.setMaxTotal(inst.maxTotal);
		// 最大建立连接等待时间
		jedisPoolConfig.setMaxWaitMillis(inst.maxWaitMillis);
		// 逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
		jedisPoolConfig.setMinEvictableIdleTimeMillis(inst.minEvictableIdleTimeMillis);
		// 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
		jedisPoolConfig.setNumTestsPerEvictionRun(inst.numTestsPerEvictionRun);
		// 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
		jedisPoolConfig.setTimeBetweenEvictionRunsMillis(inst.timeBetweenEvictionRunsMillis);
		// 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
		jedisPoolConfig.setTestOnBorrow(inst.testOnBorrow);
		// 在空闲时检查有效性, 默认false
		jedisPoolConfig.setTestWhileIdle(inst.testWhileIdle);
		
		return jedisPoolConfig;
	}

	@SuppressWarnings("deprecation") 
//	@Bean
	public JedisConnectionFactory JedisConnectionFactory(JedisPoolConfig jedisPoolConfig)
	{
		JedisConnectionFactory JedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);

		// // 连接池
		  JedisConnectionFactory.setPoolConfig(jedisPoolConfig);
		 // IP地址
		 JedisConnectionFactory.setHostName(hostName);
		 // 端口号
		 JedisConnectionFactory.setPort(port);
		 // 如果Redis设置有密码
		 JedisConnectionFactory.setPassword(password);
		 // 客户端超时时间单位是毫秒
		 JedisConnectionFactory.setTimeout(5000);
		 return JedisConnectionFactory;
	}
	
//	@Bean
	public RedisTemplate<String, Object> functionDomainRedisTemplate(RedisConnectionFactory factory)
	{
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		initDomainRedisTemplate(redisTemplate, factory);
		return redisTemplate;
	}

	/**
	 * 设置数据存入 redis 的序列化方式
	 * 
	 * @param redisTemplate
	 * @param factory
	 */
	private void initDomainRedisTemplate(RedisTemplate<String, Object> redisTemplate, RedisConnectionFactory factory)
	{
		redisTemplate.setKeySerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setHashKeySerializer(new JdkSerializationRedisSerializer());
 		redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
		redisTemplate.afterPropertiesSet();
	}
}
package com.wt.db;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import com.wt.config.RedisConfig;
import com.wt.util.Tool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis工具集,使用springboot模板
 * @author WangTuo
 *
 */

//@Service 
@SuppressWarnings("unchecked") 
public class RedisUtil
{
// 	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	private static RedisUtil inst;

	/**
	 * 向集合中添加一个或多个元素
	 * @param key
	 * @param values
	 */
	public static void hashsetAdd(String key,Object... values)
	{
		inst().redisTemplate.opsForSet().add(key, values);
	}
	
	/**
	 * 根据主key获取所有包含的hashKey
	 * @param hashKey
	 * @return
	 */
	public static <HK> HK getAllKeys(String keys)
	{
		return (HK) inst().redisTemplate.opsForHash().keys(keys);
	}
	
	/**
	 * 判断给定集合中是否包含指定元素
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean hashsetIsMember(String key,Object value)
	{
		return inst().redisTemplate.opsForSet().isMember(key, value);
	}
	
	/**
	 * 获取set中的所有值
	 * @param key
	 * @return
	 */
	public static <V>V getHashset(String key)
	{
		return (V) inst().redisTemplate.opsForSet().members(key);
	}
	
	public static Long hashsetIsMember(String key)
	{
		return inst().redisTemplate.opsForSet().size(key);
	}
	
	/**
	 * 返回set集合
	 * @param key
	 * @return
	 */
	public static <V>V getSet(String key)
	{
		 SetOperations<String, Object> oper = inst().redisTemplate.opsForSet();
		 V set = (V) oper.members(key);
		 return set;
	}
	
	
	/**
	 * 设置一个value
	 * @param key
	 * @param value 当前默认使用java序列化，传入对象时,对象必须实现序列化接口<Serializable>
	 * @throws RedisIO 
	 */
	public static void setValue(String key, Object value) throws Exception
	{
		if(key == null)
		{
			throw new Exception("key为空");
		}
		try
		{
			inst().redisTemplate.opsForValue().set(key, value);
		}
		catch (Exception e)
		{
			throw new Exception("redis写入失败");
		}
	}
	
	public static void setListLeft(String key,Object value)
	{
		inst().redisTemplate.opsForList().leftPush(key, value);
	}
	
	public static void setListRight(String key,Object value)
	{
		inst().redisTemplate.opsForList().rightPush(key, value);
	}
	
	public static <V>V leftPop(String key)
	{
		return (V) inst().redisTemplate.opsForList().leftPop(key);
	}
	
	public static <V>V rightPop(String key)
	{
		return (V) inst().redisTemplate.opsForList().rightPop(key);
	}
	
	/**
	 * 根据key获取list中指定范围
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public static <V> V  getListRange(String key,long start,long end)
	{
		return (V) inst().redisTemplate.opsForList().range(key, start, end);
	}
	
	public static <T> T getO()
	{
		return null;
	}
	
	public static long getListSize(String key)
	{
		return inst().redisTemplate.opsForList().size(key);
	}
	
	/**
	 * 截取指定key的list中指定范围内的数据,删除其他元素
	 * @param key
	 * @param start
	 * @param end
	 */
	public static void listTrim(String key,int start,int end)
	{
		inst().redisTemplate.opsForList().trim(key, start, end);
	}
	
	/**
	 * 添加一个value,并且设置过期时间
	 * @param key
	 * @param value
	 * @param time
	 */
	public static void setAndExpireValue(String key, Object value,int time)
	{
		inst().redisTemplate.opsForValue().set(key, value,time, TimeUnit.SECONDS);
	}
	
	
	/**
	 * 将map中的key value全部存储
	 * @param allValue
	 */
	public static void setValueAll(Map<String,Object> allValue)
	{
		inst().redisTemplate.opsForValue().multiSet(allValue);
	}
	
	/**
	 * 获取key集合所对应的所有值
	 * @param keys key不为空
	 * @return
	 */
	public static <V>V multiGet(Collection<String> keys)
	{
		return (V) inst().redisTemplate.opsForValue().multiGet(keys);
	}
	
	/**
	 * 使一个key的值递增
	 * @param key
	 * @param delta
	 */
	public static long increment(String key,long delta)
	{
		return inst().redisTemplate.opsForValue().increment(key, delta);
	}
	
	
	/*********************************************************************************************/
	
	
	/**
	 * 设置一个hash value
	 * @param key
	 * @param hKey
	 * @param value
	 */
	public static void setHashValue(String key,Object hKey,Object value)
	{
		inst().redisTemplate.opsForHash().put(key, hKey, value);
	}
	
	/**
	 * 移除一个hashValue
	 * @param key
	 * @param hKey
	 */
	public static void removeHashValue(String key,Object hKey)
	{
		inst().redisTemplate.opsForHash().delete(key, hKey);
	}
	
	/**
	 * 将map完整的存储到hash value 
	 * @param key
	 * @param allValue
	 */
	public static void setHashValueAll(String key,Map<String,Object> allValue)
	{
		inst().redisTemplate.opsForHash().putAll(key, allValue);
	}
	
	/**
	 * 返回所有 hashKey所对应的值
	 * @param key
	 * @param hKeys
	 * @return
	 */
	public static <HV>HV hashMhultiGet(String key,Collection<Object> hKeys)
	{
		return (HV) inst().redisTemplate.opsForHash().multiGet(key, hKeys);
	}
	
	/**
	 * 删除指定hashKey对应的值
	 * @param key
	 * @param hKey
	 * @return
	 */
	public static long delHashValue(String key,Object hKey)
	{
		return inst().redisTemplate.opsForHash().delete(key, hKey);
	}
	
	/**
	 * 将hash对应的值递增
	 * @param key
	 * @param hKey
	 * @param delta
	 */
	public static void hashIncrement(String key,Object hKey,long delta)
	{
		inst().redisTemplate.opsForHash().increment(key, hKey, delta);
	}
	
	/**
	 * hashKey是否已存在
	 * @param key
	 * @param hKey
	 */
	public static boolean hasHashKey(String key,Object hKey)
	{
		return inst().redisTemplate.opsForHash().hasKey(key, hKey);
	}
	
	/**
	 * 获取一个hash value
	 * @param key
	 * @return
	 */
	public static <HV>HV getHashValue(String key,Object hKey)
	{
		return (HV) inst().redisTemplate.opsForHash().get(key, hKey);
	}
	
	/**
	 * 获取key中的所有 hashKey hValue
	 * @param key
	 * @return
	 */
	public static <HV> HV getHashValueAll(String key)
	{
		return (HV) inst().redisTemplate.opsForHash().values(key);
	}
	
	/**
	 * 设置一个value的过期时间
	 * @param key
	 * @param value
	 * @param time
	 */
	public static void expireValue(String key,int time,TimeUnit timeUnit)
	{
		inst().redisTemplate.expire(key, time, timeUnit);
		
	}
	
	/**
	 * 删除一个value
	 * @param key
	 * @return
	 */
	public static boolean delValue(String key){
		
		
		return inst().redisTemplate.delete(key);
	}
	
	/***
	 * 
	 * @param key
	 * @param values
	 * @return
	 */
	public static long hashSetRemove(String key,Object... values)
	{
		return inst().redisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * 根据key获得value
	 * @param key
	 * @return
	 */
	public static <V> V getValue(String key)
	{
		return  (V) inst().redisTemplate.opsForValue().get(key);
	}

	public static RedisUtil inst()
	{
		if(inst == null)
		{
			synchronized (RedisUtil.class)
			{
				if(inst == null)
				{
					Tool.print_debug_level0("初始化redis");
					inst = new RedisUtil();
					JedisPoolConfig jpc = RedisConfig.inst().getPoolConfig();
					JedisConnectionFactory  jcf = RedisConfig.inst().JedisConnectionFactory(jpc);
					inst.redisTemplate =  RedisConfig.inst().functionDomainRedisTemplate(jcf);
					Tool.print_debug_level0("初始化redis完成....");
				}
			}
		}
		return inst;
	}
}
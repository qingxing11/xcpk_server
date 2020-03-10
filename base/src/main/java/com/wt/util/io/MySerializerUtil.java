package com.wt.util.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wt.util.Tool;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

/**
 * java类序列化反序列化反射等基础工具
 *
 */
@SuppressWarnings("unchecked") 
public class MySerializerUtil
{
	private static ConcurrentHashMap<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

	public static Object deserializer_Java(byte[] data)
	{
		ByteArrayInputStream bin = new ByteArrayInputStream(data);
		ObjectInputStream objectInputStream = null;
		Object bean = null;
		try
		{
			objectInputStream = new ObjectInputStream(bin);
			bean = objectInputStream.readObject();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return bean;
	}

	/**
	 * java默认序列化格式，支持最好，流比protobuf大，需要实现<Serializable>接口和添加串行标识，体积大
	 * 
	 * @param object
	 * @return
	 */
	public static byte[] serializer_Java(Object object)
	{
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bout);
		ObjectOutputStream objectOutputStream = null;
		try
		{
			objectOutputStream = new ObjectOutputStream(dos);
			objectOutputStream.writeObject(object);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		byte[] bytes = bout.toByteArray();
		return bytes;
	}

	/**
	 * 序列化为protostuff格式，体积非常小
	 * 
	 * @param obj
	 * @return
	 */
	public static <T> byte[] serializer_protobufIOUtil(T obj)
	{
		Class<T> clazz = (Class<T>) obj.getClass();
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
		try
		{
			Schema<T> schema = getSchema(clazz);

			return ProtobufIOUtil.toByteArray(obj, schema, buffer);//
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
		finally
		{
			buffer.clear();
		}
	}

	/**
	 * 从protostuff反序列化
	 * 
	 * @param data
	 * @param clazz
	 * @return
	 */
	public static <T> T deserializer_protobufIOUtil(byte[] data, Class<T> clazz)
	{
		T obj = null;
		Schema<T> schema = null;
		try
		{
			obj = clazz.newInstance();
			schema = getSchema(clazz);
			obj = deserializerProtobufIOUtil(data, obj, schema);
		}
		catch (Exception e)
		{
			Tool.print_error("deserializer_protobufIOUtil反序列化错误:"+clazz.getName());
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * 从protostuff反序列化
	 * @param data
	 * @param clazz
	 * @return
	 */
//	public static <T> T deserializer_protobufIOUtil(byte[] data, Class<T> cls)
//	{
//		Objenesis objenesis = new ObjenesisStd();
//		try
//		{
//			T message = (T) objenesis.newInstance(cls);
//			Schema<T> schema = getSchema(cls);
//			ProtostuffIOUtil.mergeFrom(data, message, schema);
//			return message;
//		}
//		catch (Exception e)
//		{
//			throw new IllegalStateException(e.getMessage(), e);
//		}
//	}

	private static <T> T deserializerProtobufIOUtil(byte[] data, T obj, Schema<T> schema)
	{
		try
		{
			//		ProtostuffIOUtil.mergeFrom(data, obj, schema);
			ProtobufIOUtil.mergeFrom(data, obj, schema);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e.getMessage(), e);
		}
		return obj;
	}

	private static <T> Schema<T> getSchema(Class<T> clazz)
	{
		Schema<T> schema = (Schema<T>) cachedSchema.get(clazz);
		if (schema == null)
		{
			schema = RuntimeSchema.getSchema(clazz);
			if (schema != null)
			{
				cachedSchema.put(clazz, schema);
			}
		}
		return schema;
	}

	/**
	 * json转为实例类
	 * 
	 * @param json
	 * @param clazz
	 * @return
	 */
	public static <T> T deserializer_json(String json, Class<T> clazz)
	{
		return JSONObject.parseObject(json, clazz);
	}

	public static String serializer_json(Object obj)
	{
		return JSONObject.toJSONString(obj,SerializerFeature.DisableCircularReferenceDetect);
	}

}

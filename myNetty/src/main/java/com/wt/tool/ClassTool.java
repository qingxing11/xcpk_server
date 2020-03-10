package com.wt.tool;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.wt.annotation.Validation.Validation.ValidationSession;
import com.wt.annotation.api.Protocol;
import com.wt.annotation.api.RegisterApi;
import com.wt.cmd.MsgType;
import com.wt.cmd.Request;
import com.wt.cmd.Response;
import com.wt.config.Config;
import com.wt.iserver.ServerHelper;
import com.wt.util.Tool;
import com.wt.util.io.MySerializerUtil;
import com.wt.util.io.PackageUtil;
import com.wt.util.log.LogUtil;
import com.wt.util.security.MySession;

import io.netty.channel.ChannelHandlerContext;

/**
 * api注册
 * 
 * @author WangTuo
 *
 */
@SuppressWarnings("unchecked") 
public class ClassTool
{
	/**
	 * 存储所有的方法和对应的类
	 */
	private static final HashMap<Integer, ApiProperties> hashMap_api = new HashMap<Integer, ApiProperties>(256);

	/**
	 * 根据协议号在配置文件中寻找指定的执行方法
	 * 
	 * @param ctx
	 * @param msgType
	 * @param request
	 */
	public static void handlerReuqest(ChannelHandlerContext ctx, int msgType, byte[] request, int util)
	{
		invokeMethod(ctx, msgType, request, util);
	}

	private static ApiProperties getApiProperties(int msgType)
	{
		return hashMap_api.get(msgType);
		// return tmap_api.get(msgType);
	}

	/**
	 * 反射调用指定类的指定方法
	 * 
	 * @param classPath
	 * @param methodName
	 * @param request
	 */
	private static void invokeMethod(ChannelHandlerContext ctx, int msgType, byte[] data, int util)
	{
		ApiProperties apiProperties = getApiProperties(msgType);
		if (apiProperties == null)
		{
			if (!PrintMsgTool.isFilterMsgType(msgType))
				LogUtil.print_error("未注册的指令:" + msgType);
			return;
		}

		try
		{
			Request request = null;
			switch (util)
			{
				case 1:
					request = (Request) MySerializerUtil.deserializer_protobufIOUtil(data, apiProperties.request.requestClazz);
					break;

				case 2:
					String str = new String(data, "UTF-8");
					request = (Request) JSON.parseObject(str, apiProperties.request.requestClazz);
					break;

				default:
					break;
			}
			
			switch (apiProperties.request.method.getParameterCount())
			{
				case 2://2个参数
					invokeMethod_by2Parameter(ctx, apiProperties, request);
					break;

				case 3://3个参数
					 invokeMethod_by3Parameter(ctx, apiProperties, request);
					break;
					
				default:
					break;
			}
		}
		catch (Exception e)
		{
			Tool.print_error("调用方法出错,apiProperties:"+apiProperties, e);
		}
	}

	private static void invokeMethod_by2Parameter(ChannelHandlerContext ctx, ApiProperties apiProperties, Request request) throws Exception, IllegalAccessException, InvocationTargetException
	{
		if (validation(apiProperties, ctx))
		{
			apiProperties.request.method.invoke(apiProperties.clazz, ctx, request);
		}
		else
		{
			Tool.print_error("validation失败,apiProperties:" + apiProperties);
		}
	}

	private static void invokeMethod_by3Parameter(ChannelHandlerContext ctx, ApiProperties apiProperties, Request request) throws IllegalAccessException, InvocationTargetException
	{
		MySession mySession = Tool.getSession(ctx);
		if (mySession == null)
		{
			Response response = new Response(MsgType.ERROR_SESSIONERR,Response.ERROR_通道鉴权错误);
			Tool.print_error("ERROR_通道鉴权错误,request:"+request.msgType);
			ServerHelper.sendResponse(ctx, response);
			return;
		}
		apiProperties.request.method.invoke(apiProperties.clazz, ctx, request,mySession);
	}
	
	private static boolean validation(ApiProperties apiProperties, ChannelHandlerContext ctx) throws Exception
	{
		if (ctx == null)
		{
			throw new Exception("通道为空");
		}

		ArrayList<Object> list = apiProperties.request.list_annotation;
		for (int i = 0 ; i < list.size() ; i++)
		{
			Object object = list.get(i);
			if (object instanceof ValidationSession)
			{
				return validationSession(ctx);
			}
		}
		return true;
	}

	/**
	 * 验证Session
	 * 
	 * @param ctx
	 * @return
	 */
	private static boolean validationSession(ChannelHandlerContext ctx)
	{
		MySession session = ctx.channel().attr(MySession.attr_session).get();
		if (session == null)
		{
			Response response = new Response(MsgType.ERROR_SESSIONERR,Response.ERROR_通道鉴权错误);
			ServerHelper.sendResponse(ctx, response);
			return false;
		}
		return true;
	}

	private ClassTool()
	{

	}

	/**
	 * 注册指定类的所有带协议号方法
	 * 
	 * @param clazz
	 */
	public static <T> void registerApi(Class<T> clazz)
	{
		String moduleName = clazz.getSimpleName();
		System.out.println("注册Api:" + moduleName);
		try
		{
			RegisterApi packagePath = clazz.getAnnotation(RegisterApi.class);
			if (packagePath == null)
			{
				throw new Exception("注册" + moduleName + "时错误,request路径为空");
			}
			String packageName = packagePath.packagePath();// request类包路径

			initClass(clazz, packageName);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static <T> void registerApi(Object clazz) throws Exception
	{
		String moduleName = clazz.getClass().getName();
		System.out.println("注册Api:" + moduleName);

		RegisterApi packagePath = clazz.getClass().getAnnotation(RegisterApi.class);
		if (packagePath == null)
		{
			throw new Exception("注册" + moduleName + "时错误,request路径为空");
		}
		String packageName = packagePath.packagePath();// request类包路径

		initClass(clazz, packageName);
	}

	/**
	 * 根据类名获取对应的实体，如没有则创建并存入hashmap 此方法在注册指令时初始化调用，不考虑多线程问题
	 * 
	 * @param clazz
	 * @param requestPackageName
	 * @return
	 */
	private static <T> Object initClass(Class<T> clazz, String requestPackageName)
	{
		Method[] methods = clazz.getMethods();
		Object object = null;
		for (Method method : methods)
		{
			try
			{
				object = clazz.newInstance();
				Protocol protocol = method.getAnnotation(Protocol.class);
				if (protocol == null)
				{
					continue;
				}

				int msgType = protocol.msgType().getType();
				ApiMethodClass methodAndClazz = new ApiMethodClass();
				methodAndClazz.method = method;
				String methodName = StringUtils.capitalize(method.getName());

				addAttr(methodAndClazz);

				String requestClassName = requestPackageName + "." + methodName + "Request";
				methodAndClazz.requestClazz = ClassLoader.getSystemClassLoader().loadClass(requestClassName);

				ApiProperties apiProperties = new ApiProperties(msgType, object, methodAndClazz);
				hashMap_api.put(msgType, apiProperties);
			}
			catch (Exception e)
			{
				// System.out.println("注册方法:【"+clazz.getSimpleName()+"."+method.getName()+"】时错误。");
				LogUtil.print_error("注册方法:【" + clazz.getSimpleName() + "." + method.getName() + "】时错误。", e);
				return null;
			}
		}
		return object;
	}
	
	private static <T> Object initClass(Object clazz, String requestPackageName)
	{
		Method[] methods = clazz.getClass().getMethods();
		Object object = null;
		for (Method method : methods)
		{
			try
			{
				object = clazz;
				Protocol protocol = method.getAnnotation(Protocol.class);
				if (protocol == null)
				{
					continue;
				}

				int msgType = protocol.msgType().getType();
				ApiMethodClass methodAndClazz = new ApiMethodClass();
				methodAndClazz.method = method;
				String methodName = StringUtils.capitalize(method.getName());

				addAttr(methodAndClazz);

				String requestClassName = requestPackageName + "." + methodName + "Request";
				methodAndClazz.requestClazz = ClassLoader.getSystemClassLoader().loadClass(requestClassName);

				ApiProperties apiProperties = new ApiProperties(msgType, object, methodAndClazz);
				hashMap_api.put(msgType, apiProperties);
			}
			catch (Exception e)
			{
				// System.out.println("注册方法:【"+clazz.getSimpleName()+"."+method.getName()+"】时错误。");
				LogUtil.print_error("注册方法:【" + clazz.getClass().getName() + "." + method.getName() + "】时错误。", e);
				return null;
			}
		}
		return object;
	}

	private static void addAttr(ApiMethodClass methodAndClazz)
	{
		ValidationSession check = methodAndClazz.method.getAnnotation(ValidationSession.class);
		methodAndClazz.list_annotation.add(check);
	}

	public static void scanningApi()
	{
		List<String> classNames = PackageUtil.getClassName(Config.instance().apiClassPath, false);

		for (String classPath : classNames)
		{
			try
			{
				Class<?> clazz = ClassLoader.getSystemClassLoader().loadClass(classPath);

				for (Annotation iterable_element : clazz.getAnnotations())
				{
					if (iterable_element instanceof RegisterApi)
					{
						ClassTool.registerApi(clazz);
						break;
					}
				}
			}
			catch (ClassNotFoundException e)
			{
				Tool.print_error("加载类错误，[" + classPath + "]不是一个有效路径。", e);
			}
		}
	}
}

class ApiProperties
{
	public int msgType;
	public Object clazz;
	public ApiMethodClass request;

	public ApiProperties()
	{}

	public ApiProperties(int msgType, Object clazz, ApiMethodClass request)
	{
		this.msgType = msgType;
		this.clazz = clazz;
		this.request = request;
	}

	@Override
	public String toString()
	{
		return "ApiProperties [msgType=" + msgType + ", clazz=" + clazz + ", request=" + request + "]";
	}
}

@SuppressWarnings("rawtypes") class ApiMethodClass
{
	public Method method;
	public Class requestClazz;
	public ArrayList<Object> list_annotation = new ArrayList<>();

	@Override
	public String toString()
	{
		return "ApiMethodClass [method=" + method + ", requestClazz=" + requestClazz + "]";
	}
}

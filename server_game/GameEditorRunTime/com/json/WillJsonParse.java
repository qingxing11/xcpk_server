package com.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import com.dataStruct.triggerData.DataStructAnnotat;
import com.wt.factory.MyBeanFactory;
import com.wt.util.Tool;
import com.wt.util.io.PackageUtil;

public class WillJsonParse
{
	private final int MARK_CLASS_OPEN = 0;
	private final int MARK_CALSS_CLOSE = 1;
	private final int MARK_LIST_OPEN = 2;
	private final int MARK_LIST_CLOSE = 3;
	private final int MARK_STRING = 4;
	/// <summary>
	/// ","
	/// </summary>
	private final int MARK_COMMA = 5;
	/// <summary>
	/// 数字
	/// </summary>
	private final int MARK_NUMBER = 6;
	private final int MARK_TRUE = 7;
	private final int MARK_FALSE = 8;
	private final int MARK_NULL = 9;

	/// <summary>
	/// ":"
	/// </summary>
	private final int MARK_COLON = 10;// 冒号
	private final int MARK_NONE = 1000;

	private List<String> classNames;

	public Object JsonDeserialize(String json)
	{
		Object obj = null;
		char[] charJson = json.toCharArray();

		Integer index = 0;
		Boolean success = true;
		NumDeliver numd = new NumDeliver(0);
		obj = ParseValue(charJson, numd, success);

		return obj;
	}

	private Object ParseValue(char[] json, NumDeliver index, Boolean success)
	{
		switch (GetAhead(json, index.index))
		{
			case MARK_STRING:
				return parseString(json, index);
			case MARK_CLASS_OPEN:
				return parseClass(json, index);
			case MARK_LIST_OPEN:
				return parseList(json, index);
			case MARK_NUMBER:
				return parseNumber(json, index);
			case MARK_TRUE:
				nextMark(json, index);
				return Boolean.parseBoolean("True");
			case MARK_FALSE:
				nextMark(json, index);
				return Boolean.parseBoolean("FALSE");
			case MARK_NULL:
				nextMark(json, index);
				return null;
			case MARK_NONE:
				break;
		}

		success = false;
		return null;
	}

	private ArrayList<Object> parseList(char[] json, NumDeliver index)
	{
		ArrayList<Object> list = new ArrayList<Object>();
		// List<>
		nextMark(json, index);

		boolean done = false;
		while (!done)
		{
			int mark = GetAhead(json, index.index);
			if (mark == MARK_NONE)
			{
				return null;
			}
			else if (mark == MARK_COMMA)// list中的一个元素结束
			{
				nextMark(json, index);
			}
			else if (mark == MARK_LIST_CLOSE)// list结束符
			{
				nextMark(json, index);
				done = true;
				break;
			}
			else
			{
				Boolean success = true;
				Object value = ParseValue(json, index, success);
				if (!success)
				{
					return null;
				}

				list.add(value);// 添加value

			}
		}

		return list;
	}

	private float parseNumber(char[] json, NumDeliver index)
	{
		int lastIndex = getLastIndexOfNumber(json, index.index);
		int charLength = (lastIndex - index.index) + 1;
		// char[] numberCharArray = new char[charLength];

		String str = "";
		for (int i = 0 ; i < charLength ; i++)
		{
			str += json[index.index + i];
		}

		// ArrayList.Copy(json, index, numberCharArray, 0, charLength);
		index.index = lastIndex + 1;

		return Float.parseFloat(str);
	}

	private int getLastIndexOfNumber(char[] json, int index)
	{
		int lastIndex;
		for (lastIndex = index ; lastIndex < json.length ; lastIndex++)
		{
			if ("0123456789+-.eE".indexOf(json[lastIndex]) == -1)
			{
				break;
			}
		}
		return lastIndex - 1;
	}

	private String parseString(char[] json, NumDeliver index)
	{
		String s = "";
		char c;

		// "--将第一个"去掉
		c = json[index.index++];

		boolean complete = false;

		while (!complete)
		{
			// 已经到最后一个字符
			if (index.index == json.length)
			{
				complete = true;
				break;
			}

			c = json[index.index++];
			if (c == '"')
			{
				complete = true;
				break;
			}
			else /* if () */
			{
				s += c;
			}
		}

		return s;
	}

	private Object parseClass(char[] json, NumDeliver index)
	{
		// 返回一个完整的类？？？？？？

		Hashtable table = new Hashtable();
		int mark;
		Object clazz = null;

		// { 去掉{
		nextMark(json, index);

		boolean done = false;
		while (!done)
		{
			mark = GetAhead(json, index.index);

			if (mark == MARK_NONE)
			{
				// 数据格式不符合，返回null值
				return null;
			}
			else if (mark == MARK_COMMA)
			{// 遇到“,”跳过该符号读取下一元素
				nextMark(json, index);
			}
			else if (mark == MARK_CALSS_CLOSE)
			{// 遇到类结束符“}”该类结束返回该类

				nextMark(json, index);// 去掉“}”

				clazz = creatClazz(table);
				return clazz;
			}
			else
			{
				String name = parseString(json, index);
				if (name == null || name.equals(""))
				{// 没有变量名数据出错
					return null;
				}

				mark = nextMark(json, index);
				if (mark != MARK_COLON)
				{//// 若不是冒号，数据出错
					return null;
				}

				// 值
				Boolean success = true;
				Object value = ParseValue(json, index, success);
				if (!success)// 若没有成功，如何处置
				{// 没有值,不符合标准，该类出错返回null
					return null;
				}
				table.put(name, value);

			}
		}

		clazz = creatClazz(table);

		return clazz;
	}

	/**
	 * 创建类
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private Object creatClazz(Hashtable table)
	{
		if (classNames == null)
		{
			classNames = PackageUtil.getClassName("com.dataStruct.triggerData", true);
		}


		String fullName = table.get("fullName").toString();

		String[] names = fullName.split("\\.");// t.T

		String name = names[1];
		Object clazz = null;

		for (String classPath : classNames)
		{
			try
			{
				if (classPath.endsWith(name))
				{
					Class<?> ca = ClassLoader.getSystemClassLoader().loadClass(classPath);
					if (Modifier.isAbstract(ca.getModifiers()))
					{
						Tool.print_debug_level0("该类是抽象的不能实例化。" + ca.getName());
						continue;
					}

					for (Annotation an : ca.getAnnotations())
					{
						if (an instanceof DataStructAnnotat)
						{
							clazz = MyBeanFactory.getBean(ca);
							break;
						}
					}

					if (clazz == null)
					{
						Tool.print_debug_level0("类[" + ca.getName() + "]没有使用DataStructAnnotat注解，不生产该类的实例");
					}
					else
					{

						Field[] all_fields = ca.getFields();
						Method[] methods = ca.getMethods();

						Tool.print_debug_level0("类[" + ca.getName() + "]已实例化该类。" + all_fields.toString());
						for (Object key : table.keySet())
						{
							Object obj = table.get(key);
							if (obj instanceof ArrayList<?>)
							{
								ArrayList<Object> list = (ArrayList<Object>) obj;

								Object arguments;
								List<Object> list_i = new ArrayList<Object>();

								Method method = ca.getMethod("listVoluation", list_i.getClass());

								method.setAccessible(true);
								method.invoke(clazz, list);
							}
							else
							{
								Field field = ca.getField(key.toString());
								field.setAccessible(true);
								field.set(clazz, obj);
							}
						}
					}
				}
			}
			catch (Exception e)
			{
				Tool.print_error(e);// "加载类错误，[" +
								// classPath +
								// "]不是一个有效路径。",
			}
		}
		return clazz;
	}

	private int GetAhead(char[] json, int index)
	{
		int saveIndex = index;
		return nextMark(json, new NumDeliver(index));
	}

	private int nextMark(char[] json, NumDeliver index)
	{
		if (index.index == json.length)
		{
			return MARK_NONE;
		}

		char c = json[index.index];
		index.index++;
		switch (c)
		{
			case '{':
				return MARK_CLASS_OPEN;
			case '}':
				return MARK_CALSS_CLOSE;
			case '[':
				return MARK_LIST_OPEN;
			case ']':
				return MARK_LIST_CLOSE;
			case '"':
				return MARK_STRING;
			case ',':
				return MARK_COMMA;
			case ':':
				return MARK_COLON;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
			case '.':
				return MARK_NUMBER;
		}
		index.index--;

		int remainLength = json.length - index.index;

		if (remainLength >= 5)
		{
			if (json[index.index] == 'f' && json[index.index + 1] == 'a' && json[index.index + 2] == 'l' && json[index.index + 3] == 's' && json[index.index + 4] == 'e')
			{
				index.index += 5;
				return MARK_FALSE;
			}
		}

		if (remainLength >= 4)
		{
			if (json[index.index] == 't' && json[index.index + 1] == 'r' && json[index.index + 2] == 'u' && json[index.index + 3] == 'e')
			{
				index.index += 4;
				return MARK_TRUE;
			}
		}
		if (remainLength >= 4)
		{
			if (json[index.index] == 'n' && json[index.index + 1] == 'u' && json[index.index + 2] == 'l' && json[index.index + 3] == 'l')
			{
				index.index += 4;
				return MARK_NULL;
			}
		}

		return MARK_NONE;
	}

}

class NumDeliver
{
	public NumDeliver(int index)
	{
		this.index = index;
	}

	public int index;
}

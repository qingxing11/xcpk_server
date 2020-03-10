package com.wt.util.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class FileTool
{
	public static void saveFile(String name, byte[] data)
	{
		File file = new File(name);
		FileOutputStream fos = null;

		try
		{
			if (!file.exists())
			{// 文件不存在则创建
				file.createNewFile();
			}
			fos = new FileOutputStream(name);
			fos.write(data);// 写入文件内容
			fos.flush();
		}
		catch (IOException e)
		{
			System.err.println("文件创建失败");
		}
		finally
		{
			if (fos != null)
			{
				try
				{
					fos.close();
				}
				catch (IOException e)
				{
					System.err.println("文件流关闭失败");
				}
			}
		}
	}

	/**
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFile(String filename) throws IOException
	{
		File f = new File(filename);
		if (!f.exists())
		{
			throw new FileNotFoundException(filename);
		}

		FileChannel channel = null;
		FileInputStream fs = null;
		try
		{
			fs = new FileInputStream(f);
			channel = fs.getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
			while ((channel.read(byteBuffer)) > 0)
			{
				// do nothing
				// System.out.println("reading");
			}
			return byteBuffer.array();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				channel.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			try
			{
				fs.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Mapped File way MappedByteBuffer 可以在处理大文件时，提升性能
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	public static byte[] toByteArray3(String filename) throws IOException
	{
		FileChannel fc = null;
		try
		{
			fc = new RandomAccessFile(filename, "r").getChannel();
			MappedByteBuffer byteBuffer = fc.map(MapMode.READ_ONLY, 0, fc.size()).load();
			System.out.println(byteBuffer.isLoaded());
			byte[] result = new byte[(int) fc.size()];
			if (byteBuffer.remaining() > 0)
			{
				// System.out.println("remain");
				byteBuffer.get(result, 0, byteBuffer.remaining());
			}
			return result;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			throw e;
		}
		finally
		{
			try
			{
				fc.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static String getMd5(File file)
	{
		try
		{
			FileInputStream fis = new FileInputStream(file);
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = fis.read(buffer, 0, 1024)) != -1)
			{
				md.update(buffer, 0, length);
			}
			BigInteger bigInt = new BigInteger(1, md.digest());
			fis.close();
			return bigInt.toString(16);
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> readFileByLine(String path)
	{
		ArrayList<String> arrayList = new ArrayList<>();
		try
		{
			FileInputStream fstream = new FileInputStream(path);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

			String strLine;

			while ((strLine = br.readLine()) != null)
			{
				arrayList.add(strLine);
			}

			br.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return arrayList;
	} 

	public static void readTxtAndTrim(String path)
	{
		BufferedReader br = null;
		BufferedWriter bw = null;
		try
		{
			br = new BufferedReader(new FileReader(path));

			bw = new BufferedWriter(new FileWriter("new_" + path));

			String line = "";
			while ((line = br.readLine()) != null)
			{
				if (line != null && !line.equals(""))
				{
					bw.write(line);
					bw.newLine();
				}
			}
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static void writeMapToFile(HashMap<String, String> map_allFile, String path)
	{
		File file = new File(path);
		try
		{
			if (!file.exists())
			{// 文件不存在则创建
				file.createNewFile();
			}

			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			Iterator<Entry<String, String>> iter = map_allFile.entrySet().iterator();
			while (iter.hasNext())
			{
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				bw.write(entry.getKey() + "=" + entry.getValue());
				bw.newLine();
			}
			bw.close();
		}
		catch (IOException e)
		{
			System.err.println("文件创建失败:" + file);
		}
	}

	public static void writeMapToFile_byPlist(HashMap<String, String> map_allFile, String path, boolean isLua)
	{
		File file = new File(path);
		try
		{
			if (!file.exists())
			{// 文件不存在则创建
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(path));
			Iterator<Entry<String, String>> iter = map_allFile.entrySet().iterator();
			String head = "<?xml version=\"1.0\"" + "  encoding=\"UTF-8\"?>\n" + "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" + "<plist version=\"1.0\">\n" + "<dict>\n";
			bw.write(head);
			while (iter.hasNext())
			{
				Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
				if (!isLua)// 不要lua
				{
					if (entry.getKey().indexOf(".lua") == -1)
					{
						bw.write("\t" + "<key>" + entry.getKey() + "</key>\n" + "\t<string>" + entry.getValue() + "</string>");
						bw.newLine();
					}
				}
				else
				{
					bw.write("\t" + "<key>" + entry.getKey() + "</key>\n" + "\t<string>" + entry.getValue() + "</string>");
					bw.newLine();
				}
			}
			String titl = "</dict> \n" + "</plist>";
			bw.write(titl);
			bw.close();

		}
		catch (IOException e)
		{
			System.err.println("文件创建失败:" + file);
		}
	}

	public static HashMap<String, String> readFileToMd5Map(String path)
	{
		HashMap<String, String> map = new HashMap<>();
		BufferedReader br = null;
		try
		{
			br = new BufferedReader(new FileReader(path));
			String line = "";
			while ((line = br.readLine()) != null)
			{
				String[] kv = line.split("=");
				map.put(kv[0], kv[1]);
			}
			br.close();
			return map;
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (NumberFormatException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取流
	 *
	 * @param inStream
	 * @return 字节数组
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception
	{
		ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = -1;
		while ((len = inStream.read(buffer)) != -1)
		{
			outSteam.write(buffer, 0, len);
		}
		outSteam.close();
		inStream.close();
		return outSteam.toByteArray();
	}
}

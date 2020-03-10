package com.wt.util;

import java.math.BigInteger;

public class PromoCodeGenV2 {

	// 十进制转换中把字符转换为数
	public static int changeDec(char ch) {
		int num = 0;
		if (ch >= 'A' && ch <= 'Z')
			num = ch - 'A' + 10;
		else if (ch >= 'a' && ch <= 'z')
			num = ch - 'a' + 36;
		else
			num = ch - '0';
		return num;
	}

	// 任意进制转换为10进制
	public static BigInteger anyToTen(String input, int base) {
		BigInteger bigTemp = BigInteger.ZERO, temp = BigInteger.ONE;
		int len = input.length();
		for (int i = len - 1; i >= 0; i--) {
			if (i != len - 1)
				temp = temp.multiply(BigInteger.valueOf(base));
			int num = changeDec(input.charAt(i));
			bigTemp = bigTemp.add(temp.multiply(BigInteger.valueOf(num)));
		}
		 return bigTemp;
	}

	// 数字转换为字符	ABCDEFGHIJKLMNOPQRSTUVWXYZ	0123456789
	public static char numToChar(BigInteger temp) {
		int n = temp.intValue();

		if (n >= 10 && n <= 35){
			return (char) (n - 10 + 'A');
		}else if (n >= 36 && n <= 61){
			return (char) (n - 36 + 'a');
		}else if(n<10){//0-9数字
			return (char) (n + '0');
		}else{
			return (char) (n + '0');
		}
	}

	// 十进制转换为任意进制
	public static String tenToAnyConversion(BigInteger Bigtemp, BigInteger base) {
		String ans = "";
		while (Bigtemp.compareTo(BigInteger.ZERO) != 0) {
			BigInteger temp = Bigtemp.mod(base);
			Bigtemp = Bigtemp.divide(base);
			char ch = numToChar(temp);
			ans = ch + ans;
		}
		return ans;
	}

	public static String anyToAny(String input, int scouceBase, BigInteger targetBase) {
		return tenToAnyConversion(anyToTen(input, scouceBase), targetBase);

	}

	public static char[][] mapping={//总共36字母，去掉0 1 O I Q L  剩余30进制
//		{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',},
		{'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T',},//'U','V','W','X','Y','Z',
		{'2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','J','K','M','N','P','R','S','T','U','V','W','X','Y','Z',},
	};
	/**
	 * 生成邀请码：用户ID->36进制
	 * @param id
	 * @return
	 */
	public static String generateCode(int id) {
		id+=5000;
		String code = tenToAnyConversion(new BigInteger(""+id), new BigInteger("30"));
		String check = anyToAny(code, 36, new BigInteger("16"));
		String finalCode0= code + check.charAt(check.length() - 1);
		String finalCode1="";
		char[] chrs=finalCode0.toCharArray();
		for (int i = 0; i < chrs.length; i++) {
			for (int j = 0; j < mapping[0].length; j++) {
				if(mapping[0][j]==chrs[i]){
					finalCode1+=(mapping[1][j]);
					break;
				}
			}
		}
		return finalCode1;
	}
	/**
	 * 邀请码逆向
	 */
	public static int getUserIdFromCode(String code) {
		if(!isLegal(code)){
			return -1;
		}
		int userId=-1;
		code=code.substring(0, code.length()-1);
		//反转密码
		String secretKey="";
		char[] chrs=code.toCharArray();
		for (int i = 0; i < chrs.length; i++) {
			for (int j = 0; j < mapping[0].length; j++) {
				if(mapping[1][j]==chrs[i]){
					secretKey+=(mapping[0][j]);
					break;
				}
			}
		}
		
		String source = anyToAny(secretKey, 30, new BigInteger("10"));
		userId=Integer.parseInt(source);
		return userId-5000;
	}

	/**
	 * 检查是否合法
	 * @param check
	 * @return
	 */
	public static boolean isLegal(String check) {
		String check1="";
		char[] chrs=check.toCharArray();
		for (int i = 0; i < chrs.length; i++) {
			for (int j = 0; j < mapping[0].length; j++) {
				if(mapping[1][j]==chrs[i]){
					check1+=(mapping[0][j]);
					break;
				}
			}
		}
		check=check1;
		
		String code = check.substring(0, check.length() - 1);
		char checkSum = check.charAt(check.length() - 1);
		String str1 = anyToAny(code, 36, new BigInteger("16"));
		if (str1.charAt(str1.length() - 1) == checkSum) {
			return true;
		} else {
			return false;
		}
	}

}

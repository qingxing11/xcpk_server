
package data.define;
public class NormalReward {
    
/**
* 小苹果
*/
public static final int BigApple = 1;

/**
* 大苹果
*/
public static final int smallApple = 2;

/**
* 小芒果
*/
public static final int BigMangGuo = 3;

/**
* 大芒果
*/
public static final int smallMangGuo = 4;

/**
* 小西瓜
*/
public static final int BigXiHua = 5;

/**
* 大西瓜
*/
public static final int smallXiGua = 6;

/**
* 小橘子
*/
public static final int BigOrange = 7;

/**
* 大橘子
*/
public static final int smallOrange = 8;

/**
* 小星星
*/
public static final int BigStar = 9;

/**
* d大星星
*/
public static final int smallStar = 10;

/**
* 大双七
*/
public static final int BigSeven = 12;

/**
* 小双七
*/
public static final int smallSeven = 11;

/**
* Bar
*/
public static final int BigBar = 13;

/**
* smalBar
*/
public static final int smallBar = 14;

/**
* 小铃铛
*/
public static final int BigLingDang = 15;

/**
* 大铃铛
*/
public static final int smallLingDang = 16;

/**
* 通杀
*/
public static final int LuckNone = 17;

/**
* 通赔
*/
public static final int LuckFail = 18;

    private static int[] values = {BigApple,smallApple,BigMangGuo,smallMangGuo,BigXiHua,smallXiGua,BigOrange,smallOrange,BigStar,smallStar,BigSeven,smallSeven,BigBar,smallBar,BigLingDang,smallLingDang,LuckNone,LuckFail};
    public static int[] getValues(){
        return values;
    }
    /**
	 * 获得常量的显示名称
	 * 
	 * @param value
	 * @return
	 */
	public static String getTitle(int value) {
		if (value == BigApple) {return "小苹果";}
if (value == smallApple) {return "大苹果";}
if (value == BigMangGuo) {return "小芒果";}
if (value == smallMangGuo) {return "大芒果";}
if (value == BigXiHua) {return "小西瓜";}
if (value == smallXiGua) {return "大西瓜";}
if (value == BigOrange) {return "小橘子";}
if (value == smallOrange) {return "大橘子";}
if (value == BigStar) {return "小星星";}
if (value == smallStar) {return "d大星星";}
if (value == BigSeven) {return "大双七";}
if (value == smallSeven) {return "小双七";}
if (value == BigBar) {return "Bar";}
if (value == smallBar) {return "smalBar";}
if (value == BigLingDang) {return "小铃铛";}
if (value == smallLingDang) {return "大铃铛";}
if (value == LuckNone) {return "通杀";}
if (value == LuckFail) {return "通赔";}

		return "";
	}
}

package data.define;
public class FruitType {
    
/**
* 苹果
*/
public static final int Apple = 1;

/**
* 橘子
*/
public static final int Orange = 2;

/**
* 星星
*/
public static final int Star = 3;

/**
* 芒果
*/
public static final int MangGuo = 4;

/**
* 西瓜
*/
public static final int XiGua = 5;

/**
* 铃铛
*/
public static final int LingDang = 6;

/**
* 双七
*/
public static final int DoubleSeven = 7;

/**
* Bar
*/
public static final int Bar = 8;

    private static int[] values = {Apple,Orange,Star,MangGuo,XiGua,LingDang,DoubleSeven,Bar};
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
		if (value == Apple) {return "苹果";}
if (value == Orange) {return "橘子";}
if (value == Star) {return "星星";}
if (value == MangGuo) {return "芒果";}
if (value == XiGua) {return "西瓜";}
if (value == LingDang) {return "铃铛";}
if (value == DoubleSeven) {return "双七";}
if (value == Bar) {return "Bar";}

		return "";
	}
}
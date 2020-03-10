
package data.define;
public class LuckyType {
    
/**
* 获得VIP1
*/
public static final int VIP1 = 1;

/**
* 获得VIP2
*/
public static final int VIP2 = 2;

/**
* 获得VIP3
*/
public static final int VIP3 = 3;

/**
* 获得VIP4
*/
public static final int VIP4 = 4;

/**
* 获得VIP5
*/
public static final int VIP5 = 5;

/**
* 增时卡
*/
public static final int 增时卡 = 6;

/**
* 抢座卡
*/
public static final int 抢座卡 = 7;

/**
* 改名卡
*/
public static final int 改名卡 = 8;

/**
* 获得功能卡
*/
public static final int 金币 = 9;

/**
* 获得金币
*/
public static final int 钻石 = 10;

    private static int[] values = {VIP1,VIP2,VIP3,VIP4,VIP5,增时卡,抢座卡,改名卡,金币,钻石};
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
		if (value == VIP1) {return "获得VIP1";}
if (value == VIP2) {return "获得VIP2";}
if (value == VIP3) {return "获得VIP3";}
if (value == VIP4) {return "获得VIP4";}
if (value == VIP5) {return "获得VIP5";}
if (value == 增时卡) {return "增时卡";}
if (value == 抢座卡) {return "抢座卡";}
if (value == 改名卡) {return "改名卡";}
if (value == 金币) {return "获得功能卡";}
if (value == 钻石) {return "获得金币";}

		return "";
	}
}
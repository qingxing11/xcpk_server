
package data.define;
public class VipLvTopUp {
    
/**
* vip等级0解锁条件
*/
public static final int zero = 0;

/**
* vip等级1解锁条件
*/
public static final int one = 15;

/**
* vip等级2解锁条件
*/
public static final int two = 58;

/**
* vip等级3解锁条件
*/
public static final int three = 188;

/**
* vip等级4解锁条件
*/
public static final int four = 888;

/**
* vip等级5解锁条件
*/
public static final int five = 1888;

    private static int[] values = {zero,one,two,three,four,five};
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
		if (value == zero) {return "vip等级0解锁条件";}
if (value == one) {return "vip等级1解锁条件";}
if (value == two) {return "vip等级2解锁条件";}
if (value == three) {return "vip等级3解锁条件";}
if (value == four) {return "vip等级4解锁条件";}
if (value == five) {return "vip等级5解锁条件";}

		return "";
	}
}
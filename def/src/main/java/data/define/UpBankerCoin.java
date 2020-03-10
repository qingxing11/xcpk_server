
package data.define;
public class UpBankerCoin {
    
/**
* 上庄条件
*/
public static final int Up = 50000000;

/**
* 上庄条件
*/
public static final int Down = 20000000;

    private static int[] values = {Up,Down};
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
		if (value == Up) {return "上庄条件";}
if (value == Down) {return "上庄条件";}

		return "";
	}
}
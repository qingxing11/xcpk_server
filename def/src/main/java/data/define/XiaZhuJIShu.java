
package data.define;
public class XiaZhuJIShu {
    
/**
* 下注基数
*/
public static final int number = 20000;

    private static int[] values = {number};
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
		if (value == number) {return "下注基数";}

		return "";
	}
}
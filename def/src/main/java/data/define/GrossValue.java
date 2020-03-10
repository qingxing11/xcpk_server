
package data.define;
public class GrossValue {
    
/**
* 概率基数
*/
public static final int grossvalue = 1000;

    private static int[] values = {grossvalue};
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
		if (value == grossvalue) {return "概率基数";}

		return "";
	}
}
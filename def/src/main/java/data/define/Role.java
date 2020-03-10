
package data.define;
public class Role {
    
/**
* 男
*/
public static final int man = 0;

/**
* 女
*/
public static final int woman = 1;

    private static int[] values = {man,woman};
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
		if (value == man) {return "男";}
if (value == woman) {return "女";}

		return "";
	}
}
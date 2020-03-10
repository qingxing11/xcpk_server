
package data.define;
public class ReadInfoState {
    
/**
* 未读
*/
public static final int 未读 = 0;

/**
* 已读
*/
public static final int 已读 = 1;

/**
* 删除
*/
public static final int 删除 = 2;

    private static int[] values = {未读,已读,删除};
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
		if (value == 未读) {return "未读";}
if (value == 已读) {return "已读";}
if (value == 删除) {return "删除";}

		return "";
	}
}
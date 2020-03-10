
package data.define;
public class FriendState {
    
/**
* 无数据
*/
public static final int 无 = 0;

/**
* 请求添加好友
*/
public static final int 未处理 = 1;

/**
* 拒绝添加
*/
public static final int 拒绝 = 2;

/**
* 同意添加
*/
public static final int 同意 = 3;

    private static int[] values = {无,未处理,拒绝,同意};
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
		if (value == 无) {return "无数据";}
if (value == 未处理) {return "请求添加好友";}
if (value == 拒绝) {return "拒绝添加";}
if (value == 同意) {return "同意添加";}

		return "";
	}
}

package data.define;
public class TaskType {
    
/**
* 日常任务
*/
public static final int DayTask = 1;

/**
* 个人任务
*/
public static final int PersonSelfTask = 2;

/**
* 系统任务
*/
public static final int SystemTask = 3;

    private static int[] values = {DayTask,PersonSelfTask,SystemTask};
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
		if (value == DayTask) {return "日常任务";}
if (value == PersonSelfTask) {return "个人任务";}
if (value == SystemTask) {return "系统任务";}

		return "";
	}
}
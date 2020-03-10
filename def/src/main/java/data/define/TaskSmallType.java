
package data.define;
public class TaskSmallType {
    
/**
* 大喇叭发言
*/
public static final int 大喇叭发言 = 1;

/**
* 发表情
*/
public static final int 发表情 = 2;

/**
* 经典获胜
*/
public static final int 经典获胜 = 3;

/**
* 每日通杀上庄
*/
public static final int 每日通杀上庄 = 4;

/**
* 每日万人上庄
*/
public static final int 每日万人上庄 = 5;

/**
* 经典场获得金花
*/
public static final int 经典场获得金花 = 6;

/**
* 经典场获得顺金
*/
public static final int 经典场获得顺金 = 7;

/**
* 经典场获得豹子
*/
public static final int 经典场获得豹子 = 8;

/**
* 玩家等级
*/
public static final int 玩家等级 = 9;

/**
* 累积在线
*/
public static final int 累积在线 = 10;

/**
* 累积充值
*/
public static final int 累积充值 = 11;

/**
* 通杀场压中豹子
*/
public static final int 通杀场压中豹子 = 12;

/**
* 万人场获取豹子
*/
public static final int 万人场获取豹子 = 13;

/**
* 累积赢金榜排名第一名
*/
public static final int 累积赢金榜排名第一名 = 14;

/**
* 累积充值榜排名第一名
*/
public static final int 累积充值榜排名第一名 = 15;

    private static int[] values = {大喇叭发言,发表情,经典获胜,每日通杀上庄,每日万人上庄,经典场获得金花,经典场获得顺金,经典场获得豹子,玩家等级,累积在线,累积充值,通杀场压中豹子,万人场获取豹子,累积赢金榜排名第一名,累积充值榜排名第一名};
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
		if (value == 大喇叭发言) {return "大喇叭发言";}
if (value == 发表情) {return "发表情";}
if (value == 经典获胜) {return "经典获胜";}
if (value == 每日通杀上庄) {return "每日通杀上庄";}
if (value == 每日万人上庄) {return "每日万人上庄";}
if (value == 经典场获得金花) {return "经典场获得金花";}
if (value == 经典场获得顺金) {return "经典场获得顺金";}
if (value == 经典场获得豹子) {return "经典场获得豹子";}
if (value == 玩家等级) {return "玩家等级";}
if (value == 累积在线) {return "累积在线";}
if (value == 累积充值) {return "累积充值";}
if (value == 通杀场压中豹子) {return "通杀场压中豹子";}
if (value == 万人场获取豹子) {return "万人场获取豹子";}
if (value == 累积赢金榜排名第一名) {return "累积赢金榜排名第一名";}
if (value == 累积充值榜排名第一名) {return "累积充值榜排名第一名";}

		return "";
	}
}
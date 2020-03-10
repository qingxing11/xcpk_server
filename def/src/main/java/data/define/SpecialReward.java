
package data.define;
public class SpecialReward {
    
/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dasixi
*/
public static final int BigFourXi = 1;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dasanyuan
*/
public static final int BigThreeYuan = 2;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dandian
*/
public static final int SinglePointSmple = 3;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_xiaosanyuan
*/
public static final int SmallThreeYuan = 4;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_kaihuoche
*/
public static final int OnTrain = 5;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dandian
*/
public static final int SinglePointSpecial = 7;

/**
* YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_jiulian
*/
public static final int TheNineTreasureLamp = 6;

/**
* 开火车1
*/
public static final int OnTrain1 = 1;

/**
* 开火车2
*/
public static final int OnTrain2 = 2;

/**
* 开火车3
*/
public static final int OnTrain3 = 3;

    private static int[] values = {BigFourXi,BigThreeYuan,SinglePointSmple,SmallThreeYuan,OnTrain,SinglePointSpecial,TheNineTreasureLamp,OnTrain1,OnTrain2,OnTrain3};
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
		if (value == BigFourXi) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dasixi";}
if (value == BigThreeYuan) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dasanyuan";}
if (value == SinglePointSmple) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dandian";}
if (value == SmallThreeYuan) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_xiaosanyuan";}
if (value == OnTrain) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_kaihuoche";}
if (value == SinglePointSpecial) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_dandian";}
if (value == TheNineTreasureLamp) {return "YT/FruitMachine/SpritePack/shuiguo/shuiguoji_zhongjiangjilu_jiulian";}
if (value == OnTrain1) {return "开火车1";}
if (value == OnTrain2) {return "开火车2";}
if (value == OnTrain3) {return "开火车3";}

		return "";
	}
}
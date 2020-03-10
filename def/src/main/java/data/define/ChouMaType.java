
package data.define;
public class ChouMaType {
    
/**
* BRC/Room/SpritePack/Chip1/game_chip100
*/
public static final int 一百 = 100;

/**
* BRC/Room/SpritePack/Chip1/game_chip300
*/
public static final int 三百 = 300;

/**
* BRC/Room/SpritePack/Chip1/game_chip500
*/
public static final int 五百 = 500;

/**
* BRC/Room/SpritePack/Chip1/game_chip800
*/
public static final int 八百 = 800;

/**
* BRC/Room/SpritePack/Chip1/game_chip1000
*/
public static final int 一千 = 1000;

/**
* BRC/Room/SpritePack/Chip1/game_chip1500
*/
public static final int 一千五 = 1500;

/**
* BRC/Room/SpritePack/Chip1/game_chip2000
*/
public static final int 两千 = 2000;

/**
* BRC/Room/SpritePack/Chip1/game_chip2500
*/
public static final int 两千五 = 2500;

/**
* BRC/Room/SpritePack/Chip1/game_chip3000
*/
public static final int 三千 = 3000;

/**
* BRC/Room/SpritePack/Chip1/game_chip4000
*/
public static final int 四千 = 4000;

/**
* BRC/Room/SpritePack/Chip1/game_chip5000
*/
public static final int 五千 = 5000;

/**
* BRC/Room/SpritePack/Chip1/game_chip6000
*/
public static final int 六千 = 6000;

/**
* BRC/Room/SpritePack/Chip1/game_chip8000
*/
public static final int 八千 = 8000;

/**
* BRC/Room/SpritePack/Chip1/game_chip10000
*/
public static final int 一万 = 10000;

/**
* BRC/Room/SpritePack/Chip1/game_chip20000
*/
public static final int 两万 = 20000;

/**
* BRC/Room/SpritePack/Chip1/game_chip30000
*/
public static final int 三万 = 30000;

/**
* BRC/Room/SpritePack/Chip1/game_chip50000
*/
public static final int 五万 = 50000;

/**
* BRC/Room/SpritePack/Chip1/game_chip80000
*/
public static final int 八万 = 80000;

/**
* BRC/Room/SpritePack/Chip1/game_chip100000
*/
public static final int 十万 = 100000;

/**
* BRC/Room/SpritePack/Chip1/game_chip150000
*/
public static final int 十五万 = 150000;

/**
* BRC/Room/SpritePack/Chip1/game_chip200000
*/
public static final int 二十万 = 200000;

/**
* BRC/Room/SpritePack/Chip1/game_chip250000
*/
public static final int 二十五万 = 250000;

/**
* BRC/Room/SpritePack/Chip1/game_chip300000
*/
public static final int 三十万 = 300000;

/**
* BRC/Room/SpritePack/Chip1/game_chip400000
*/
public static final int 四十万 = 400000;

/**
* BRC/Room/SpritePack/Chip1/game_chip500000
*/
public static final int 五十万 = 500000;

    private static int[] values = {一百,三百,五百,八百,一千,一千五,两千,两千五,三千,四千,五千,六千,八千,一万,两万,三万,五万,八万,十万,十五万,二十万,二十五万,三十万,四十万,五十万};
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
		if (value == 一百) {return "BRC/Room/SpritePack/Chip1/game_chip100";}
if (value == 三百) {return "BRC/Room/SpritePack/Chip1/game_chip300";}
if (value == 五百) {return "BRC/Room/SpritePack/Chip1/game_chip500";}
if (value == 八百) {return "BRC/Room/SpritePack/Chip1/game_chip800";}
if (value == 一千) {return "BRC/Room/SpritePack/Chip1/game_chip1000";}
if (value == 一千五) {return "BRC/Room/SpritePack/Chip1/game_chip1500";}
if (value == 两千) {return "BRC/Room/SpritePack/Chip1/game_chip2000";}
if (value == 两千五) {return "BRC/Room/SpritePack/Chip1/game_chip2500";}
if (value == 三千) {return "BRC/Room/SpritePack/Chip1/game_chip3000";}
if (value == 四千) {return "BRC/Room/SpritePack/Chip1/game_chip4000";}
if (value == 五千) {return "BRC/Room/SpritePack/Chip1/game_chip5000";}
if (value == 六千) {return "BRC/Room/SpritePack/Chip1/game_chip6000";}
if (value == 八千) {return "BRC/Room/SpritePack/Chip1/game_chip8000";}
if (value == 一万) {return "BRC/Room/SpritePack/Chip1/game_chip10000";}
if (value == 两万) {return "BRC/Room/SpritePack/Chip1/game_chip20000";}
if (value == 三万) {return "BRC/Room/SpritePack/Chip1/game_chip30000";}
if (value == 五万) {return "BRC/Room/SpritePack/Chip1/game_chip50000";}
if (value == 八万) {return "BRC/Room/SpritePack/Chip1/game_chip80000";}
if (value == 十万) {return "BRC/Room/SpritePack/Chip1/game_chip100000";}
if (value == 十五万) {return "BRC/Room/SpritePack/Chip1/game_chip150000";}
if (value == 二十万) {return "BRC/Room/SpritePack/Chip1/game_chip200000";}
if (value == 二十五万) {return "BRC/Room/SpritePack/Chip1/game_chip250000";}
if (value == 三十万) {return "BRC/Room/SpritePack/Chip1/game_chip300000";}
if (value == 四十万) {return "BRC/Room/SpritePack/Chip1/game_chip400000";}
if (value == 五十万) {return "BRC/Room/SpritePack/Chip1/game_chip500000";}

		return "";
	}
}
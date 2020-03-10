package com.gjc.naval.safebox;

public interface SafeBoxService 
{
	void onPlayerOnLine(int otherId);
	
	/**是否有银行功能*/
	boolean haveSafeBox(int vipLv);
	
	/**当前银行存取金额*/
	 long safeBoxSaveMoneyMax(int vipLv);
}

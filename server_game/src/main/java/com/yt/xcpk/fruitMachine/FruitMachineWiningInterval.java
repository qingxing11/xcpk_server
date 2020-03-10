package com.yt.xcpk.fruitMachine;

import java.io.Serializable;

public class FruitMachineWiningInterval implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3152112293732728076L;
	
	public int fruitType;
	public boolean rewardSpecial;
	public int min;
	public int max;
	public FruitMachineWiningInterval(int fruitType,boolean rewardSpecial,int min, int max) {
		this.fruitType=fruitType;
		this.rewardSpecial =rewardSpecial;
		this.min=min;
		this.max=max;
	}
	@Override
	public String toString() {
		return "FruitMachineWiningInterval [fruitType=" + fruitType + ", rewardSpecial=" + rewardSpecial + ", min="
				+ min + ", max=" + max + "]";
	}	
}

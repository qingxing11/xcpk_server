package com.wt.util;

import java.io.Serializable;

import com.wt.util.Tool;


public class AntiCrackNumF implements Serializable {

	public static final int LEVEL = 32;

	private int[] guardianCode = new int[LEVEL];// 守护数据

	private float[] maskCode = new float[LEVEL];// 影子数据

	public float value;// 真值数据

	public AntiCrackNumF() {
		this(0);
	}

	public AntiCrackNumF(float value) {
		resetGuardian();
		setValue(value);
	}

	private void resetGuardian() {
		for (int i = 0; i < guardianCode.length; i++) {
			guardianCode[i] = (Tool.getRandomBoolean() ? 1 : -1) * Tool.getRandom(100, 1000);
		}
	}

	public void setValue(float value) {
		this.value = value;
		resetGuardian();
		for (int i = 0; i < maskCode.length; i++) {
			maskCode[i] = guardianCode[i] + value;
		}
	}

	public void addValue(float value) {
		setValue(this.value + value);
	}

	public void subValue(float value) {
		setValue(this.value - value);
	}

	private int[] value_weight = new int[LEVEL];
	private float[] value_test = new float[LEVEL];

	public float getValue() {
		boolean dataCracked = false;
		float maybeTrue = value;// 可能为真的数值

		for (int i = 0; i < guardianCode.length; i++) {
			if (value != maskCode[i] - guardianCode[i]) {
				dataCracked = true;
				break;
			}
		}
		if (dataCracked) {// 如果数据被破解，尝试猜出真数值，重新生成LEVEL对守护数据
			int specialCode = -123456789;
			for (int i = 0; i < value_test.length; i++) {// 清空表格
				value_test[i] = specialCode;
				value_weight[i] = 0;
			}
			boolean valueInserted = false;
			float currValue = 0;
			for (int index = 0; index < maskCode.length; index++) {
				currValue = maskCode[index] - guardianCode[index];
				for (int i = 0; i < value_test.length; i++) {
					if (value_test[i] == currValue) {
						value_weight[i]++;
						valueInserted = true;
						break;
					}
				}
				if (!valueInserted) {
					for (int i = 0; i < value_test.length; i++) {
						if (value_test[i] == specialCode) {// 尚未赋值
							value_test[i] = currValue;
							value_weight[i]++;
							break;
						}
					}
				}
			}
			// for (int i = 0; i < value_test.length; i++) {
			// System.out.println(value_test[i]+" - "+value_weight[i]);
			// }

			int heavyIndex = 0;
			for (int i = 0; i < value_weight.length - 1; i++) {
				if (value_weight[i + 1] > value_weight[i]) {
					heavyIndex = i + 1;
				}
			}
			float maybeTrueValue = value_test[heavyIndex];// 最可能是真实的值
			setValue(maybeTrueValue);// 重新赋值
			return value;
		} else {
			return value;
		}
	}

	@Override
	public String toString() {
		return "" + getValue();
	}

	public int getIntValue() {
		return (int) getValue();
	}

}

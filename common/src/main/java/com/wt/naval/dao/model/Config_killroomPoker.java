package com.wt.naval.dao.model;

import java.io.Serializable;

/**
 *  总1000概率
 */
public class Config_killroomPoker implements Serializable {
	private static final long serialVersionUID = 1L;

	private int flush;

	private int leopard;

	private int onepoker;

	private int pair;

	private int samecolorflush;

	private int samecolorpoker;

	public Config_killroomPoker() {
	}

	public int getFlush() {
		return this.flush;
	}

	public void setFlush(int flush) {
		this.flush = flush;
	}

	public int getLeopard() {
		return this.leopard;
	}

	public void setLeopard(int leopard) {
		this.leopard = leopard;
	}

	public int getOnepoker() {
		return this.onepoker;
	}

	public void setOnepoker(int onepoker) {
		this.onepoker = onepoker;
	}

	public int getPair() {
		return this.pair;
	}

	public void setPair(int pair) {
		this.pair = pair;
	}

	public int getSamecolorflush() {
		return this.samecolorflush;
	}

	public void setSamecolorflush(int samecolorflush) {
		this.samecolorflush = samecolorflush;
	}

	public int getSamecolorpoker() {
		return this.samecolorpoker;
	}

	public void setSamecolorpoker(int samecolorpoker) {
		this.samecolorpoker = samecolorpoker;
	}

}
package com.gjc.naval.horn;

import java.io.Serializable;

public class HornInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5647390690163694187L;
	public int userId;
	public String nikeName;
	public int vipLv;
	public String info;
	public int role;
	public HornInfoVO() 
	{
		
	}
	public HornInfoVO(int userId, String nikeName, int vipLv, String info,int role) {
		this.userId = userId;
		this.nikeName = nikeName;
		this.vipLv = vipLv;
		this.info = info;
		this.role=role;
	}

	@Override
	public String toString() {
		return "HornInfoVO [userId=" + userId + ", nikeName=" + nikeName + ", vipLv=" + vipLv + ", info=" + info
				+ ", role=" + role + "]";
	}

	
}

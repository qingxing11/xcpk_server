package com.brc.cmd.mail;

public class Attach
{
	public int id;      //物品id item表中的id
	public int num;  //物品的数量
	
	 public Attach() {
		
	}
	 public Attach(int id,int num)
	 {
		 this.id=id;
		 this.num=num;
	 }
	
	@Override
	public String toString()
	{
		return "Attach [id=" + id + ", num=" + num + "]";
	}
}
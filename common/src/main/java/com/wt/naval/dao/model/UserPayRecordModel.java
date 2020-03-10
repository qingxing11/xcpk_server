package com.wt.naval.dao.model;

import java.sql.Timestamp;

public class UserPayRecordModel
{
	public int pay_record_id;
	public int user_id;
	public String nick_name;
	public float total_fee;
	public int parent_level1;
	public int parent_level2;
	public int parent_level3;
	public float parent_lv1_rate;
	public float parent_lv2_rate;
	public float parent_lv3_rate;
	public float parent_lv1_receipts;
	public float parent_lv2_receipts;
	public float parent_lv3_receipts;
	public String pay_traget;
	public Timestamp time;
	public int card_num;
	@Override
	public String toString()
	{
		String txt = "id:"+pay_record_id+",user_id:"+user_id+",nick_name:"+nick_name+",total_fee:"+total_fee+",parent_level1:"+parent_level1+",parent_level2:"+parent_level2+",parent_level3:"+parent_level3
				+",parent_lv1_rate:"+parent_lv1_rate+",parent_lv2_rate:"+parent_lv2_rate+",parent_lv3_rate:"+parent_lv3_rate+",parent_lv1_receipts:"+parent_lv1_receipts+",parent_lv2_receipts:"+parent_lv2_receipts+",parent_lv3_receipts:"+parent_lv3_receipts
				+",time:"+time;
		return txt;
	}
}

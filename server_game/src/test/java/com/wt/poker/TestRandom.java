package com.wt.poker;

import java.util.ArrayList;
import com.wt.util.MyUtil;
import junit.framework.TestCase;

public class TestRandom extends TestCase
{
	public void test()
	{
		Item item = new Item(13, 0);// (5)
		Item item2 = new Item(2, 1);// (10)
		ArrayList<Item> list = new ArrayList<Item>();
		list.add(item);
		list.add(item2);

		for (int i = 0 ; i < 15 ; i++)
		{
			int rate = MyUtil.getRandom(15) + 1;
			System.out.println("rate:" + rate);
			int index = 0;
			for (Item it : list)
			{
				if (rate >= index && rate <= index + it.rate)
				{
					System.out.println(it.lottery);
					break;
				}
				index += it.rate;
			}
		}
	}
}

package com.wt.util.sort;

import java.util.Comparator;

public class MySortUtil
{
	/**
	 * 排序比较器
	 * @return
	 */
	public static Comparator<ISort> lowToHighByIndex()
	{
		Comparator<ISort> comparator = new Comparator<ISort>()
		{
			public int compare(ISort p1, ISort p2)
			{
				if (p1.getSortIndex()  > p2.getSortIndex())
				{
					return 1;
				}
				else if (p1.getSortIndex() < p2.getSortIndex())
				{
					return -1;
				}
				return 0;
			}
		};
		return comparator;
	}
	
	public static Comparator<ISortLong> lowToHighByIndexLong()
	{
		Comparator<ISortLong> comparator = new Comparator<ISortLong>()
		{
			public int compare(ISortLong p1, ISortLong p2)
			{
				if (p1.getSortIndex()  > p2.getSortIndex())
				{
					return 1;
				}
				else if (p1.getSortIndex() < p2.getSortIndex())
				{
					return -1;
				}
				return 0;
			}
		};
		return comparator;
	}
	
	public static Comparator<ISort> highToLowByIndex()
	{
		Comparator<ISort> comparator = new Comparator<ISort>()
		{
			public int compare(ISort p1, ISort p2)
			{
				if (p1.getSortIndex()  > p2.getSortIndex())
				{
					return -1;
				}
				else if (p1.getSortIndex() < p2.getSortIndex())
				{
					return 1;
				}
				return 0;
			}
		};
		return comparator;
	}
	
	public static Comparator<ISortLong> highToLowByIndexLong()
	{
		Comparator<ISortLong> comparator = new Comparator<ISortLong>()
		{
			public int compare(ISortLong p1, ISortLong p2)
			{
				if (p1.getSortIndex()  > p2.getSortIndex())
				{
					return -1;
				}
				else if (p1.getSortIndex() < p2.getSortIndex())
				{
					return 1;
				}
				return 0;
			}
		};
		return comparator;
	}
}

package com.wt.pool;

import com.wt.exception.pool.ReusableNullException;

public interface MyReusable
{
	void release() throws ReusableNullException;
	
	MyReusable instance();
}

package com.wt.annotation.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.wt.cmd.MsgTypeEnum;

/**
 * 注释本方法对应的协议号
 * @author WangTuo
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Protocol
{
	public MsgTypeEnum msgType();
}

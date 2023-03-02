package com.example.log.function;

/**
 * 自定义函数需要实现该接口
 */
public interface IParseFunction {
	/**
	 * 是否在业务逻辑执行之前执行
	 */
	default boolean executeBefore() {
		return false;
	}

	String functionName();

	String apply(String value);
}
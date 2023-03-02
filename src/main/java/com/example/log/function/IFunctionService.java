package com.example.log.function;

/**
 * com.example.log.function
 *
 * @author xzwnp
 * 2023/3/1
 * 19:44
 */
public interface IFunctionService {
	String apply(String functionName, String value);

	boolean beforeFunction(String functionName);
}

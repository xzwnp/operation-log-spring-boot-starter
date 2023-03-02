package com.example.log.service;

import com.example.log.function.IFunctionService;
import com.example.log.function.IParseFunction;

import java.util.UUID;

/**
 * com.example.log.service
 *
 * @author xzwnp
 * 2023/3/2
 * 11:12
 */
public class OrderIdFunction implements IParseFunction {
	@Override
	public String functionName() {
		return "getOrderId";
	}

	@Override
	public String apply(String value) {
		return UUID.randomUUID().toString();
	}
}

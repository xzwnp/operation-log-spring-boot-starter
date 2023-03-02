package com.example.log.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 维护一个可以手动设置SpEL变量的上下文
 * SpEL表达式中的部分变量可以通过在业务代码中手动调用putVariable()设置
 * AOP中通过调用putVariable来设置返回值和错误信息
 */
public class LogRecordContext {
	/**
	 * 本质是ThreadLocal.
	 * Map<String, Object>是为了保存方法的所有变量
	 */
	private static final ThreadLocal<Map<String, Object>> variableMaps = new InheritableThreadLocal<>();
	public static final String RETURN_VALUE_KEY = "_ret";
	public static final String ERROR_MESSAGE_KEY = "_msg";

	public static void putVariables(Map<String, Object> variables) {
		variableMaps.set(variables);
	}

	public static void putVariable(String key, Object value) {
		Map<String, Object> map = variableMaps.get();
		if (map == null) {
			map = new HashMap<>();
			variableMaps.set(map);
		}
		map.put(key, value);

	}

	public static Map<String, Object> getVariables() {
		return variableMaps.get();
	}

	public static void clear() {
		variableMaps.remove();
	}
}
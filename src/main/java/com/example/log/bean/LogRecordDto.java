package com.example.log.bean;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * com.example.log.bean
 * 封装操作信息,便于持久化后以报表形式查看
 * @author xzwnp
 * 2023/3/1
 * 21:00
 */
@Data
@Accessors(chain = true)
public class LogRecordDto {
	/**
	 * 业务ID
	 */
	private String bizId;

	/**
	 * 业务类型
	 * 可选
	 */
	private String bizType;

	/**
	 * 日志内容
	 * 可选
	 * 解析SpEL表达式后生成
	 */
	String content;


	/**
	 * 操作人ID
	 * 如果为空,会采取默认的方式尝试获取操作人
	 */
	private String operatorId;

	/**
	 * 操作人姓名/昵称
	 */
	private String operatorName;

	/**
	 * 所有参数
	 */
	private Map<String, Object> parameterMap;
	/**
	 * 返回值
	 */
	private Object returnValue;
	/**
	 * 耗时（单位：毫秒）
	 */
	private Long timeCost;

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 异常信息
	 */
	private Throwable exception;
}

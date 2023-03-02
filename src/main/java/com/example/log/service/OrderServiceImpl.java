package com.example.log.service;

import com.example.log.annotation.LogRecord;
import com.example.log.context.LogRecordContext;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * com.example.log.service
 *
 * @author xzwnp
 * 2023/3/2
 * 10:39
 */
@Service
@Slf4j
public class OrderServiceImpl {
	@LogRecord(bizId = "#orderId", content = "'用户 '+#request.userId + ' 创建了一个新的订单,订单号是:' + #orderId", recordReturnValue = true)
	public Order creteOrder(OrderRequest request) {
		Order order = new Order(UUID.randomUUID().toString(), request.getUserId(), request.getNewFollower());
		LogRecordContext.putVariable("orderId", order.getOrderId());
		log.info("新增订单{}", order.getOrderId());
		return order;
	}
}

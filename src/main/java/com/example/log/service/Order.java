package com.example.log.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * com.example.log
 *
 * @author xzwnp
 * 2023/3/1
 * 19:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
	private String orderId;
	private String userId;
	private String newFollower;
}

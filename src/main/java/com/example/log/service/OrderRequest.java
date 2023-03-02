package com.example.log.service;

import lombok.Data;

/**
 * com.example.log
 *
 * @author xzwnp
 * 2023/3/1
 * 20:39
 */
@Data
public class OrderRequest {
	private String userId;
	private String orderId;
	private String newFollower;

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setNewFollower(String newFollower) {
		this.newFollower = newFollower;
	}

	public String getNewFollower() {
		return newFollower;
	}
}

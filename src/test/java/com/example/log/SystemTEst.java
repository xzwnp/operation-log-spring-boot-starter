package com.example.log;

import com.example.log.service.OrderRequest;
import com.example.log.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

/**
 * com.example.log
 *
 * @author xzwnp
 * 2023/3/1
 * 19:49
 */
@SpringBootTest(classes = OperationLogSpringBootStarterApplication.class)
public class SystemTEst {
	@Autowired
	OrderServiceImpl orderService;

	@Test
	void test1() {
		StopWatch sw = new StopWatch();
		for (int i = 0; i < 5; i++) {
			sw.start();
			OrderRequest orderRequest = new OrderRequest();
			orderRequest.setUserId("123");
			orderService.creteOrder(orderRequest);
			sw.stop();
			System.out.println(sw.getLastTaskTimeMillis() + "ms");
		}

	}
}

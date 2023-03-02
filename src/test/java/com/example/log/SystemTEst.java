package com.example.log;

import com.example.log.service.OrderRequest;
import com.example.log.service.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    void test1() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(() -> {
                OrderRequest orderRequest = new OrderRequest();
                orderRequest.setUserId("123");
                orderService.creteOrder(orderRequest);
                latch.countDown();
            });

        }
        latch.await();
    }

}

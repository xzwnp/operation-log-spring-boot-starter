package com.example.log;

import com.example.log.service.Order;
import com.example.log.service.OrderRequest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * com.example.log
 *
 * @author xzwnp
 * 2023/3/1
 * 19:04
 */
public class SpELTest {
	@Test
	void test1() {
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#root.userId");
		Order order = new Order();
		order.setUserId("张三");
		System.out.println(expression.getValue(order));
	}

	@Test
	void test2() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start("统计总耗时");
		String spElExpression = "'用户' + #request.userId + '修改了订单的跟进人：从' + #request.orderId + '修改到' + #request.newFollower";
		StandardEvaluationContext context = new StandardEvaluationContext();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setUserId("123456");
		orderRequest.setOrderId("654321");
		orderRequest.setNewFollower("1111");
		context.setVariable("request", orderRequest);

		SpelExpressionParser parser = new SpelExpressionParser();
		StopWatch sw1 = new StopWatch();
		sw1.start();
		Expression expression = parser.parseExpression(spElExpression);
		sw1.stop();
		System.out.println("解析spel耗时:" + sw1.getLastTaskTimeMillis());
		sw1.start();
		expression.getValue(context);
		sw1.stop();
		System.out.println("spel取值耗时:" + sw1.getLastTaskTimeMillis());
		stopWatch.stop();
		System.out.println("总耗时" + stopWatch.getTotalTimeMillis());
	}

	@SneakyThrows
	@Test
	void test3() {
		Method method = OrderRequest.class.getDeclaredMethod("setOrderId", String.class);
		Object[] arguments = {"小明"};
		DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
		System.out.println("参数名称:"+ Arrays.toString(discoverer.getParameterNames(method)));
		MethodBasedEvaluationContext context = new MethodBasedEvaluationContext(null, method, arguments,discoverer);
		String expressionString = "'姓名' + #orderId";
		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression(expressionString);
		String res = expression.getValue(context, String.class);
		System.out.println(res);
	}
}

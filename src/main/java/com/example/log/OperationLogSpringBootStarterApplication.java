package com.example.log;

import com.example.log.evaluation.CachedSpelExpressionParser;
import com.example.log.operator.DefaultOperatorGetServiceImpl;
import com.example.log.operator.IOperatorGetService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;

@SpringBootApplication
@Configuration
public class OperationLogSpringBootStarterApplication {

	@Bean
	ExpressionParser expressionParser() {
		return new CachedSpelExpressionParser();
	}

	@Bean
	IOperatorGetService iOperatorGetService() {
		return new DefaultOperatorGetServiceImpl();
	}

	@Bean
	ParameterNameDiscoverer parameterNameDiscoverer() {
		return new DefaultParameterNameDiscoverer();
	}

	public static void main(String[] args) {
		SpringApplication.run(OperationLogSpringBootStarterApplication.class, args);
	}

}

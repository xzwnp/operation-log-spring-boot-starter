package com.example.log.evaluation;

import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对SpEL表达式进行缓存
 * com.example.log.evaluation
 *
 * @author xzwnp
 * 2023/3/1
 * 22:13
 */

public class CachedSpelExpressionParser extends SpelExpressionParser {

	private Map<String, SpelExpression> cache = new ConcurrentHashMap<>(64);

	@Override
	protected SpelExpression doParseExpression(String expressionString, ParserContext context) throws ParseException {
		SpelExpression spelExpression = cache.get(expressionString);
		if (spelExpression == null) {
			spelExpression = super.doParseExpression(expressionString, context);
		}
		return spelExpression;
	}
}

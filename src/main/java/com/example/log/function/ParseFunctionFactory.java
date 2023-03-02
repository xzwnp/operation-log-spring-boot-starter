package com.example.log.function;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Spring的依赖注入,从容器中获取全部IParseFunction装入工厂,需要用的时候直接根据函数名获取即可
 */
public class ParseFunctionFactory {
  private Map<String, IParseFunction> allFunctionMap;

  public ParseFunctionFactory(List<IParseFunction> parseFunctions) {
    if (CollectionUtils.isEmpty(parseFunctions)) {
      return;
    }
    allFunctionMap = new HashMap<>();
    for (IParseFunction parseFunction : parseFunctions) {
      if (StringUtils.isEmpty(parseFunction.functionName())) {
        continue;
      }
      allFunctionMap.put(parseFunction.functionName(), parseFunction);
    }
  }

  public IParseFunction getFunction(String functionName) {
    return allFunctionMap.get(functionName);
  }

  public boolean isBeforeFunction(String functionName) {
    return allFunctionMap.get(functionName) != null && allFunctionMap.get(functionName).executeBefore();
  }
}
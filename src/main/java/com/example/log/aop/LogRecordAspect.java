package com.example.log.aop;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.example.log.annotation.LogRecord;
import com.example.log.bean.LogRecordDto;
import com.example.log.bean.Operator;
import com.example.log.context.LogRecordContext;
import com.example.log.evaluation.LogRecordEvaluationContext;
import com.example.log.operator.IOperatorGetService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.concurrent.*;

/**
 * com.example.autoconfig.config.log
 *
 * @author xzwnp
 * 2023/1/10
 * 11:34
 */
@Slf4j
@Component
@Aspect
@EnableAspectJAutoProxy
public class LogRecordAspect {

    private final ExecutorService logExecutor;

    private final ObjectMapper objectMapper;

    private IOperatorGetService operatorGetService;

    private ExpressionParser expressionParser;

    private ParameterNameDiscoverer parameterNameDiscoverer;

    public LogRecordAspect(IOperatorGetService operatorGetService, ExpressionParser expressionParser, ParameterNameDiscoverer parameterNameDiscoverer) {
        objectMapper = new ObjectMapper();
        //跳过序列化空值属性
        objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 5, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<>(10));
        //包装原有线程池
        logExecutor = TtlExecutors.getTtlExecutorService(executor);
        this.operatorGetService = operatorGetService;
        this.expressionParser = expressionParser;
        this.parameterNameDiscoverer = parameterNameDiscoverer;
    }

    /**
     * 定义切入点
     */
    @Pointcut("@annotation(com.example.log.annotation.LogRecord)")
    public void pointCut() {
    }


    /**
     * 前置通知：目标方法执行之前执行以下方法体的内容。
     * value：绑定通知的切入点表达式。可以关联切入点声明，也可以直接设置切入点表达式
     * <br/>
     * * @param joinPoint：提供对连接点处可用状态和有关它的静态信息的反射访问<br/> <p>
     * * * Object[] getArgs()：返回此连接点处（目标方法）的参数，目标方法无参数时，返回空数组
     * * * Signature getSignature()：返回连接点处的签名。
     * * * Object getTarget()：返回目标对象
     * * * Object getThis()：返回当前正在执行的对象
     * * * StaticPart getStaticPart()：返回一个封装此连接点的静态部分的对象。
     * * * SourceLocation getSourceLocation()：返回与连接点对应的源位置
     * * * String toLongString()：返回连接点的扩展字符串表示形式。
     * * * String toShortString()：返回连接点的缩写字符串表示形式。
     * * * String getKind()：返回表示连接点类型的字符串
     * * * </p>
     */
//	@Before("pointCut()&&@annotation(logRecord)")
//	public void before(LogRecord logRecord) {
//
//	}
    @Around("pointCut() && @annotation(logRecord)")
    public Object afterReturning(ProceedingJoinPoint joinPoint, com.example.log.annotation.LogRecord logRecord) throws Throwable {
        Object result = null; //业务逻辑返回结果
        Throwable throwable = null;
        boolean success; //业务逻辑是否执行成功
        try {
            //目标方法执行前
            StopWatch stopWatch = new StopWatch();
            try {
                stopWatch.start();
                //目标方法执行
                result = joinPoint.proceed();
                stopWatch.stop();
                success = true;
            } catch (Throwable th) {
                //如果目标方法执行出错
                success = false;
                throwable = th;
            }

            //目标方法执行后
            LogRecordDto logRecordDto = new LogRecordDto();

            //多线程打日志,降低性能影响
            logExecutor.execute(() -> {
                MethodSignature signature = (MethodSignature) joinPoint.getSignature();
                Operator operator = getOperator(logRecord);
                EvaluationContext evaluationContext = prepareLogRecordEvaluationContext(signature.getMethod(), joinPoint.getArgs());
                long cost = stopWatch.getTotalTimeMillis(); //业务执行耗时
                //操作内容用SpEL解析器解析后输出
                String content = expressionParser.parseExpression(logRecord.content()).getValue(evaluationContext, String.class);
                String bizId = expressionParser.parseExpression(logRecord.bizId()).getValue(evaluationContext, String.class);
                String bizType = logRecordDto.getBizType();
                //输出操作内容
                log.info("bizId:{},类型:{},描述:{},耗时:{}ms", bizId, bizType, content, cost);

                logRecordDto
                        .setBizId(bizId)
                        .setBizType(bizType)
                        .setOperatorId(operator.getId()).
                        setOperatorName(operator.getName())
                        .setContent(content)
                        .setTimeCost(cost);
                //threadLocal手动清除,避免内存泄露
                LogRecordContext.clear();
            });

            //是否输出返回值
            if (logRecord.recordReturnValue()) {
                logRecordDto.setReturnValue(result).setSuccess(success)
                        .setException(throwable);
                LogRecordContext.putVariable(LogRecordContext.RETURN_VALUE_KEY, result);

                //返回值以json格式输出,只输出前50个字符
                //todo 返回值采用SpEl表达式格式,只输出想看到的部分
                String returnValueString = objectMapper.writeValueAsString(result);
                returnValueString = returnValueString.substring(0, Math.min(returnValueString.length(), 50));
                log.info("返回结果:{}", returnValueString);
            }

        } catch (Exception e) {
            log.error("日志记录出错", e);
            //日志出错也要返回正常执行的业务
            return result;
        }
        //如果业务执行有异常,抛出去,交给ControllerAdvice处理
        if (throwable != null) {
            throw throwable;
        }

        return result;

    }

    /**
     * 获取操作员信息
     *
     * @param logRecord
     * @return
     */
    private Operator getOperator(LogRecord logRecord) {
        if (logRecord.operatorId() == null) {
            return operatorGetService.getOperator();
        } else {
            return new Operator(logRecord.operatorId(), logRecord.operatorName());
        }
    }

    /**
     * 配置日志解析上下文
     */
    private EvaluationContext prepareLogRecordEvaluationContext(Method method, Object[] arguments) {
        return new LogRecordEvaluationContext(null, method, arguments, this.parameterNameDiscoverer);
    }

}

package com.dragon.boot.redis.cache;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * @author： pengl
 * @Date： 2017/11/10 17:10
 * @Description： 自定义缓存，默认KEY生成器
 */
@Component
public class DefaultKeyGenerator {
    private static final Logger LOG = LoggerFactory.getLogger(DefaultKeyGenerator.class);

    /**
     * @Description： redis key生成
     * @param： cacheKey：key值必传 ，可以使用el表达式
     * @return：
     * @throws：
     * @author： pengl
     * @Date： 2017/11/13 10:58
     */
    public String generateKey(ProceedingJoinPoint pjp, String cacheKey) {
        Signature signature = pjp.getSignature();
        /**
         * 解析cacheKey，获取EL表达式解析后的值
         */
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("This annotation can only be used for methods...");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        //method参数列表
        String[] parameterNames = methodSignature.getParameterNames();
        if (parameterNames != null && parameterNames.length > 0) {
            Object[] args = pjp.getArgs();
            for (int i = 0; i < parameterNames.length; i++) {
                String parameterName = parameterNames[i];
                evaluationContext.setVariable(parameterName, args[i]);
            }
        }
        try {
            return new SpelExpressionParser().parseExpression(cacheKey).getValue(evaluationContext, String.class);
        } catch (EvaluationException ev) {
            LOG.error("解析CacheKey异常：{}", ev.getMessage());
            return cacheKey;
        }
    }

}

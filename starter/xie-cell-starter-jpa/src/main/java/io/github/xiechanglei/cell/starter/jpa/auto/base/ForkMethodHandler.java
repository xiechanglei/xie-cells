package io.github.xiechanglei.cell.starter.jpa.auto.base;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * AOP 参数抽取工具类，用于从切点连接点中提取方法参数
 * todo 缓存优化
 *
 * @author xie
 * @date 2026/3/4
 */
public class ForkMethodHandler {

    /**
     * 根据参数名称获取参数值
     *
     * @param joinPoint 切点连接点
     * @param signature 方法签名
     * @param paramName 参数名称
     * @return 参数值，如果找不到则返回 null
     */
    public static Object getParamByName(JoinPoint joinPoint, MethodSignature signature, String paramName) {
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        for (int i = 0; i < parameterNames.length; i++) {
            if (paramName.equals(parameterNames[i])) {
                return args[i];
            }
        }

        return null;
    }

    /**
     * 获取所有参数名和参数值的映射
     *
     * @param joinPoint 切点连接点
     * @param signature 方法签名
     * @return 参数名 - 参数值映射
     */
    public static Map<String, Object> getAllParams(JoinPoint joinPoint, MethodSignature signature) {
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            params.put(parameterNames[i], args[i]);
        }
        return params;
    }


    /**
     * 检查方法参数中是否有 ExecuteResult 类型的参数，并返回其索引，如果没有则返回 -1
     *
     * @param method 要检查的方法
     * @return ExecuteResult 参数的索引，如果没有则返回 -1
     */
    public static int findExecuteResultParameterIndex(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (ExecuteResult.class.isAssignableFrom(parameterTypes[i])) {
                return i;
            }
        }
        return -1;
    }


    /**
     * 解析实体类类型
     */
    public static EntityInfo resolveEntityClass(Method method, Class<?> entityClass) {
        // 优先使用注解上指定的实体类
        if (entityClass != UseTypeDefinedClass.class) {
            return new EntityInfo(entityClass, "资源");
        }

        // 从类上的@EntityClass 注解获取
        EntityClass annotation = method.getDeclaringClass().getAnnotation(EntityClass.class);
        if (annotation != null) {
            return new EntityInfo(annotation.value(), annotation.name());
        }

        throw new IllegalStateException("无法确定实体类类型，请在方法上使用@FindById 指定实体类，或在类上使用@EntityClass 注解");
    }
}

package cn.tucci.aop;

import java.lang.reflect.Method;

/**
 * 存储增强方法的信息，包含类和方法
 *
 * @author tucci.lee
 */
public class AspectInfo {

    private Class<?> aspectClass;
    private Method aspectMethod;

    public AspectInfo(Class<?> aspectClass, Method aspectMethod) {
        this.aspectClass = aspectClass;
        this.aspectMethod = aspectMethod;
    }

    public Class<?> getAspectClass() {
        return aspectClass;
    }

    public Method getAspectMethod() {
        return aspectMethod;
    }
}

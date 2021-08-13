package cn.tucci.aop.test;

import cn.tucci.aop.AspectInfo;
import cn.tucci.aop.Point;
import cn.tucci.aop.ProxyMethodInterceptor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author tucci.lee
 */
public class Test {

    public static void main(String[] args) throws NoSuchMethodException {
        // 需要aop的方法
        Method a = Bean.class.getMethod("a");
        Set<Method> methods = Arrays.stream(Bean.class.getMethods())
                .filter(method -> !method.equals(a))
                .collect(Collectors.toSet());
        // aop增强的信息
        Map<Method, List<AspectInfo>> aspectInfoMap = new HashMap<>();
        for (Method method : methods) {
            List<AspectInfo> aspectInfos = new LinkedList<>();
            Class<Aspect> aspectClass = Aspect.class;
//            Class<Aspect2> aspect2Class = Aspect2.class;
            aspectInfos.add(new AspectInfo(aspectClass, aspectClass.getMethod("around1", Point.class)));
//            aspectInfos.add(new AspectInfo(aspectClass, aspectClass.getMethod("around2", Point.class)));
//            aspectInfos.add(new AspectInfo(aspect2Class, aspect2Class.getMethod("around1", Point.class)));
            aspectInfoMap.put(method, aspectInfos);
        }

        // 创建代理对象
        Enhancer enhancer = new Enhancer();
        MethodInterceptor methodInterceptor = new ProxyMethodInterceptor(new Bean(), aspectInfoMap);
        enhancer.setSuperclass(Bean.class);
        enhancer.setCallback(methodInterceptor);

        Bean bean = (Bean) enhancer.create();
//        bean.a();
//        System.out.println("-");
        bean.a("haha");
//        System.out.println("--");
//        Object b = bean.b();
//        System.out.println("---");
//        Object b1 = bean.b("heihei");
//        System.out.println("----");
//        Object b2 = bean.b("hehe");
//        System.out.println("-----");
//
//        System.out.println(b);
//        System.out.println(b1);
//        System.out.println(b2);
    }
}

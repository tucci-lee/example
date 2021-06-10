package cn.tucci.aop.test;

import cn.tucci.aop.Point;

/**
 * @author tucci.lee
 */
public class Aspect2 {

    public Object around1(Point point) throws Exception {
        System.out.println("aspect2 around1 begin");
        Object invoke = point.invoke();
        System.out.println("aspect2 around1 end");
        return invoke;
    }
}

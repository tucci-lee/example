package cn.tucci.spring;

import cn.tucci.spring.scan.ClassScanner;

import java.util.Set;

/**
 * @author tucci.lee
 */
public class Tests {

    public static void main(String[] args) {
        Set<Class<?>> scan = ClassScanner.scan("org.objectweb.asm", "cn.tucci");
        System.out.println(scan);
    }
}

package cn.tucci.spring.util;

/**
 * @author tucci.lee
 */
public class ClassUtils {

    /**
     * 获取类加载器
     *
     * @return
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = null;
        try {
            classLoader = Thread.currentThread().getContextClassLoader();
        } catch (Exception e) {
            // do nothing
        }
        if (classLoader == null) {
            try {
                classLoader = ClassUtils.class.getClassLoader();
            } catch (Exception e) {
                // do nothing
            }
        }
        if (classLoader == null) {
            try {
                classLoader = ClassLoader.getSystemClassLoader();
            } catch (Exception e) {
                // do nothing
            }
        }
        return classLoader;
    }

    /**
     * 将类路径转换为磁盘路径
     *
     * @param className
     * @return
     */
    public static String convertClassNameToResourcePath(String className) {
        return className.replace('.', '/');
    }
}

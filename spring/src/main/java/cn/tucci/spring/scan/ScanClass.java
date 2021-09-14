package cn.tucci.spring.scan;

import cn.tucci.spring.util.ClassUtils;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;

/**
 * @author tucci.lee
 */
public class ScanClass {

    public static void main(String[] args) {
        Set<Class<?>> scan = scan("org.objectweb.asm","cn.tucci");
        System.out.println(scan);
    }

    /**
     * 扫描包
     * @param basePackages
     * @return
     * @throws IOException
     */
    public static Set<Class<?>> scan(String... basePackages) {
        Set<Class<?>> allClass = new LinkedHashSet<>();
        for (String basePackage : basePackages) {
            basePackage = ClassUtils.convertClassNameToResourcePath(basePackage);
            if(basePackage.startsWith("/")){
                basePackage = basePackage.substring(1);
            }
            if(!basePackage.endsWith("/")){
                basePackage += "/";
            }
            allClass.addAll(doScan(basePackage));
        }
        return allClass;
    }

    /**
     * 查找所有的class
     * @param basePackage
     * @return
     */
    public static Set<Class<?>> doScan(String basePackage) {
        try {
            Set<Resource> rootResources = getRootResources(basePackage);

            Set<Resource> resources = new LinkedHashSet<>();
            for (Resource resource : rootResources) {
                URL url = resource.getUrl();
                if (url.getProtocol().equals("file")) {
                    getFileResources(resource, resources);
                } else if (url.getProtocol().equals("jar")) {
                    getJarResources(basePackage, resource, resources);
                }
            }

            Set<Class<?>> classes = new LinkedHashSet<>();
            for (Resource resource : resources) {
                ClassReader classReader = new ClassReader(resource.getInputStream());
                Class<?> aClass = Class.forName(classReader.getClassName().replaceAll("/", "."));
                classes.add(aClass);
            }
            return classes;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("scan package error", e);
        }
    }

    /**
     * 根据包名获取所有根路径
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    public static Set<Resource> getRootResources(String basePackage) throws IOException {
        Set<Resource> result = new LinkedHashSet<>();

        ClassLoader classLoader = ClassUtils.getClassLoader();
        Enumeration<URL> resources = classLoader.getResources(basePackage);
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            result.add(new UrlResource(url));
        }
        return result;
    }


    /**
     * 扫描文件中的类
     *
     * @param rootResource
     * @param result
     * @throws IOException
     */
    public static void getFileResources(Resource rootResource, Set<Resource> result) throws IOException {
        File rootFile = rootResource.getFile();
        File[] rootfiles = rootFile.listFiles();
        if (rootfiles == null) {
            return;
        }
        for (File file : rootfiles) {
            if (!file.exists() || !file.canRead()) {
                continue;
            }
            FileResource resource = new FileResource(file);
            if (file.isDirectory()) {
                getFileResources(resource, result);
            } else {
                result.add(resource);
            }
        }
    }

    /**
     * 扫描jar中的类
     *
     * @param basePackage
     * @param resource
     * @param result
     * @throws IOException
     */
    public static void getJarResources(String basePackage, Resource resource, Set<Resource> result) throws IOException {
        URL url = resource.getUrl();
        URLConnection connection = url.openConnection();
        if (connection instanceof JarURLConnection) {
            JarURLConnection con = (JarURLConnection) connection;
            Enumeration<JarEntry> entries = con.getJarFile().entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.startsWith(basePackage) && name.endsWith(".class")) {
                    String key = name.substring(basePackage.length());
                    URL jarFileUrl = new URL(url, key);
                    result.add(new UrlResource(jarFileUrl));
                }
            }
        }
    }
}

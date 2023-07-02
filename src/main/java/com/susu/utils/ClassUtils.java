package com.susu.utils;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Description: Class Utils</p>
 * <p>反射、class操作类</p>
 * @author sujay
 * @version 23:48 2023/07/1
 *
 * @since JDK1.8
 */
public class ClassUtils {


    public static ClassLoader getContextClassLoader() {
        return System.getSecurityManager() == null ? Thread.currentThread().getContextClassLoader() : AccessController.doPrivileged((PrivilegedAction<ClassLoader>) () -> {
            return Thread.currentThread().getContextClassLoader();
        });
    }

    public static ClassLoader getSystemClassLoader() {
        return System.getSecurityManager() == null ? ClassLoader.getSystemClassLoader() : AccessController.doPrivileged((PrivilegedAction<ClassLoader>) ClassLoader::getSystemClassLoader);
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader = getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
            if (null == classLoader) {
                classLoader = getSystemClassLoader();
            }
        }
        return classLoader;
    }

    public static Class<?> loadClass(String className, ClassLoader loader) {
        if (null == loader) {
            loader = getClassLoader();
        }

        Class<?> clazz;
        try {
            clazz = Class.forName(className, false, loader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return clazz;
    }


    public static Set<Class<?>> scan(String packagePath) {

        Set<Class<?>> classes = new HashSet<>();

        Enumeration<URL> resources;
        try {
            resources = getClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            switch (url.getProtocol()) {
                case "file":
                    scanFile(new File(url.getFile()), null);
                    break;
                case "jar":
                    System.out.println("jar");
            }
        }

        return new HashSet<>();
    }


    private static Set<Class<?>> scanFile(File file, String rootDir) {
        Set<Class<?>> classes = new HashSet<>();
        if (file.isFile()) {
            String fileName = file.getAbsolutePath();
            if (fileName.endsWith(".class")) {
                String className = fileName.substring(rootDir.length(), fileName.length() - 6).replace(File.separatorChar, '.');

            } else if (fileName.endsWith(".jar")) {

            }
        }

        return null;
    }
}

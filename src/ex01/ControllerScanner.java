package ex01;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ControllerScanner {
    public static void scan(Class<?> clazz, String path) throws Exception {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
            	Annotation anno = method.getDeclaredAnnotation(RequestMapping.class);
    			RequestMapping rm = (RequestMapping) anno;
    			if(rm.uri().equalsIgnoreCase(path)) {
        				method.invoke(clazz.newInstance());
        		}
            }
        }
    }

    public static void scan(String packageName, String inputPath) throws Exception {
        List<Method> methods = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
            for (File classFile : file.listFiles()) {
                String fileName = classFile.getName();
                if (fileName.endsWith(".class")) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    Class<?> clazz = Class.forName(packageName + "." + className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        scan(clazz, inputPath);
                    }
                }
            }
        }
    }
}
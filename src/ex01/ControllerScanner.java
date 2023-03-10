package ex01;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ControllerScanner {

    public static Method[] scan(Class<?> clazz) {
        List<Method> methods = new ArrayList<>();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                methods.add(method);
            }
        }
        Method[] meth =  methods.toArray(new Method[0]);
        return meth;
    }

    public static Method[] scan(String packageName) throws Exception {
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
                    Class<?> clazz = Class.forName(
                    		packageName + "." + 
                    className);
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        methods.addAll(Arrays.asList(scan(clazz)));
                    }
                }
            }
        }
        Method[] meth =  methods.toArray(new Method[0]);
        return meth;
    }
}
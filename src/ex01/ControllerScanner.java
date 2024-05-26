package ex01;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Enumeration;

import ex01.annotation.Controller;
import ex01.annotation.RequestMapping;

public class ControllerScanner {

    // 클래스에서 어노테이션 검색
    public static void scan(Class<?> clazz, String path) throws Exception {
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
    			RequestMapping rm = method.getAnnotation(RequestMapping.class);
    			if(rm.uri().equalsIgnoreCase(path)) {
                    method.invoke(clazz.getDeclaredConstructor().newInstance());
        		}
            }
        }
    }

    // 특정 패키지 검색
    public static void scan(String packageName, String inputPath) throws Exception {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.toURI());
            if (file.exists()) {
                scanDirectory(file, packageName, inputPath);
            }
        }
    }

    // 디렉토리에서 class 를 찾는다
    private static void scanDirectory(File directory, String packageName, String inputPath) throws Exception {
        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                // 디렉토리인 경우, 재귀 호출로 하위 디렉토리를 탐색
                scanDirectory(file, packageName + "." + file.getName(), inputPath);
            } else if (file.getName().endsWith(".class")) {
                // .class 파일인 경우, 클래스 로드 및 어노테이션 확인
                String className = file.getName().substring(0, file.getName().lastIndexOf('.'));
                Class<?> clazz = Class.forName(packageName + "." + className);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    scan(clazz, inputPath);
                }
            }
        }
    }
}
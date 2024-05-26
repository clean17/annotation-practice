# 리플렉션

java 프로그래밍에서 실행중 동적으로 검사, 수정하는 기능을 제공한다<br>

1. 리플렉션을 이용해서 클래스 정보 얻기
```java
public class ReflectionExample {
    public static void main(String[] args) throws ClassNotFoundException {
        // 클래스 객체를 얻는 세 가지 방법
        Class<?> clazz1 = Class.forName("java.lang.String");
        Class<?> clazz2 = String.class;
        Class<?> clazz3 = new String().getClass();
        
        System.out.println("Class Name: " + clazz1.getName());
    }
}
```
2. 생성자 접근 및 인스턴스 생성
``` java
import java.lang.reflect.Constructor;

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.util.ArrayList");
        
        // 기본 생성자 접근 및 인스턴스 생성
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        Object instance = constructor.newInstance();
        
        System.out.println("Instance created: " + instance.getClass().getName());
    }
}
```
3. 메서드 접근 및 호출
```java
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        Class<?> clazz = Class.forName("java.util.ArrayList");
        Object instance = clazz.getDeclaredConstructor().newInstance();
        
        // add 메서드 접근 및 호출
        Method addMethod = clazz.getMethod("add", Object.class);
        addMethod.invoke(instance, "Hello");
        
        // size 메서드 접근 및 호출
        Method sizeMethod = clazz.getMethod("size");
        int size = (int) sizeMethod.invoke(instance);
        
        System.out.println("Size of ArrayList: " + size);
    }
}
```
4. 필드 접근 및 조작
```java
import java.lang.reflect.Field;

public class ReflectionExample {
    private String privateField = "Private Field";

    public static void main(String[] args) throws Exception {
        ReflectionExample example = new ReflectionExample();
        Class<?> clazz = example.getClass();
        
        // private 필드 접근
        Field field = clazz.getDeclaredField("privateField");
        field.setAccessible(true);  // 접근 가능하도록 설정
        
        // 필드 값 읽기
        String fieldValue = (String) field.get(example);
        System.out.println("Field Value: " + fieldValue);
        
        // 필드 값 쓰기
        field.set(example, "New Value");
        System.out.println("Updated Field Value: " + example.privateField);
    }
}
```

# 어노테이션
java 어노테이션 생성
```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
	String uri();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface MyComponent {
    // 추가적인 속성이 필요하다면 여기에 정의
}
```

어노테이션은 다음과 같이 사용한다
```java
@Controller
public class UserController {
	@RequestMapping( uri = "/login")
	public void login() {
		System.out.println("login() 호출됨");
	}
}
```

어노테이션을 적용시킬 패키지 경로와 찾으려는 키워드를 받는다
```java
	public static void main(String[] args) {
		while (true) {
			Scanner sc = new Scanner(System.in);
			String path = sc.nextLine();
			try {
				ControllerScanner.scan("ex01", path);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
```

scan 메서드는 다음과 같이 정의된다
```java
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
```


## `clazz.newInstance()`, `clazz.getDeclaredConstructor().newInstance()` 차이 <br>
`clazz.newInstance()` 는 기본 생성자만 호출 가능하다 <br>
`clazz.getDeclaredConstructor().newInstance()` 는 기본 생성자와 매개변수가 있는 생성자도 호출 가능하다<br>
따라서 두번째 호출 방법이 권장된다
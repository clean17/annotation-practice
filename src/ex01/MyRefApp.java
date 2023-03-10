package ex01;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Scanner;

public class MyRefApp {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		
		UserController uc = new UserController();
		// 리플렉션은 런타임시 여러가지를 보고 연결 이번에는 어노테이션으로 만들어 본다
		Method[] methods = uc.getClass().getDeclaredMethods();
//		System.out.println(methods.length);
//		Arrays.asList(methods).forEach((e)->{System.out.println(e.getName());});
//		Arrays.asList(methods).forEach((e)->{System.out.println(e.getDeclaredAnnotation(RequestMapping.class));});
		Arrays.asList(methods).forEach((e)->{
			Annotation anno = e.getDeclaredAnnotation(RequestMapping.class);
			RequestMapping rm = (RequestMapping) anno;
			System.out.println(rm.uri());
//			if(rm.uri().equalsIgnoreCase(path)) {
//				try {
//					e.invoke(uc);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
		});
		
		// 이제 UserController 밖에 없으니까 새로운 컨트롤러가 필요하면 ? 어노테이션을 만들어서 컴포넌트 스캔을 시켜줘야함
	}
}

package ex01;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class MyRefApp {
	public static void test(String path) {
		try {
		Method[] test = ControllerScanner.scan("ex01");
		Arrays.asList(test).forEach((e)->{
			Annotation anno = ((AccessibleObject) e).getDeclaredAnnotation(RequestMapping.class);
			RequestMapping rm = (RequestMapping) anno;
		if(rm.uri().equalsIgnoreCase(path)) {
			try {
				e.invoke(new UserController());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		});
	} catch (Exception e1) {
		e1.printStackTrace();
	}
		
	};
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		test(path);
		
//		UserController uc = new UserController();	
//		Method[] methods = uc.getClass().getDeclaredMethods();
//		// 패키지 내의 모든 어노테이션을 가져오는 배열 ? 컴포넌트 스캔을 통해서 어노테이션이 붙은거를 다 가져와야함
//		
//		Arrays.asList(methods).forEach((e)->{
//			System.out.println(e);
//			Annotation anno = e.getDeclaredAnnotation(RequestMapping.class);
//			System.out.println(anno);
//			RequestMapping rm = (RequestMapping) anno;
////			System.out.println(rm.uri());
//			if(rm.uri().equalsIgnoreCase(path)) {
//				try {
//					e.invoke(uc);
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//		});
		
//		try {
//			Method[] test = ControllerScanner.scan(UserController.class)
////					.getClass().getDeclaredClasses()
//					;
//			Arrays.asList(test).forEach((e)->{
////				System.out.println(e);
//				Annotation anno = ((AccessibleObject) e).getDeclaredAnnotation(RequestMapping.class);
////				System.out.println(anno);
//				RequestMapping rm = (RequestMapping) anno;
////				System.out.println(rm.uri());
//			if(rm.uri().equalsIgnoreCase(path)) {
//				try {
//					e.invoke(new UserController());
//				} catch (Exception e1) {
//					e1.printStackTrace();
//				}
//			}
//			});
//	
//		} catch (Exception e1) {
//			e1.printStackTrace();
//		}
		

	}
}

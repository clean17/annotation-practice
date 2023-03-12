package ex01;

import java.util.Scanner;


public class MyRefApp {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String path = sc.nextLine();
		try {
			ControllerScanner.scan("ex01", path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

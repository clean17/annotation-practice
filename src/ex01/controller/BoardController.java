package ex01.controller;

import ex01.annotation.Controller;
import ex01.annotation.RequestMapping;

@Controller
public class BoardController {
	@RequestMapping( uri = "/save")
	public void save() {
		System.out.println("save 호출됨");
	}
	
	@RequestMapping( uri = "/super")
	public void sse() {
		System.out.println("super 호출됨");
	}
}

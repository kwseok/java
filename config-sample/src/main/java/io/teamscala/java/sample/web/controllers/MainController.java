package io.teamscala.java.sample.web.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController extends BaseController {

	@RequestMapping
	public String index() {
        return "index";
	}

}

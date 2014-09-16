package io.teamscala.java.sample.web.controllers.admin;

import io.teamscala.java.sample.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class MainController extends BaseController {

	/**
	 * 관리자 화면의 인덱스 페이지.
	 * 
	 * @return
	 */
	@RequestMapping
	public String index() throws Exception {
        return "admin/index";
	}
	
}

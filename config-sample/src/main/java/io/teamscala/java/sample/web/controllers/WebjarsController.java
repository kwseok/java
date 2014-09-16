package io.teamscala.java.sample.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.webjars.RequireJS;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/webjars")
public class WebjarsController {

    @RequestMapping(value = "/setupRequireJS.js", method = RequestMethod.GET)
    public void webjars(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.reset();
        response.setContentType("text/javascript");
        response.getWriter().print(RequireJS.getSetupJavaScript(request.getContextPath() + "/webjars/"));
    }

}

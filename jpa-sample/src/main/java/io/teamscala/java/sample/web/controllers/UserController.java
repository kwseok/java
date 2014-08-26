package io.teamscala.java.sample.web.controllers;

import io.teamscala.java.core.web.method.annotation.EntityAttribute;
import io.teamscala.java.sample.models.User;
import io.teamscala.java.sample.models.UserRole;
import io.teamscala.java.sample.security.util.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 사용자 컨트롤러 샘플.
 *
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    // Dependency injections

    // Request mappings

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() throws Exception {
        return "user/login";
    }

    @RequestMapping(value = "/loginFailure", method = RequestMethod.GET)
    public String loginFailure(Model model) throws Exception {
        model.addAttribute("hasError", true);
        return "user/login";
    }

    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String join(Model model) throws Exception {
        model.addAttribute("user", new User());
        return "user/form";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String form(Model model) throws Exception {
        model.addAttribute("user", User.find.byId(SecurityUtils.getUser().getId()));
        return "user/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(@Validated @EntityAttribute User user, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) return "user/form";
        else {
            if (user.getId() == null) {
                // 기본 권한 부여
                user.addRole(new UserRole(UserRole.Type.ROLE_MEMBER));
            }
            // 저장
            user.save();
            return "user/complete";
        }
    }
}

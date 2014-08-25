package io.teamscala.java.sample.web.controllers;

import io.teamscala.java.core.data.Pageable;
import io.teamscala.java.core.data.SimpleSearch;
import io.teamscala.java.core.web.method.annotation.EntityAttribute;
import io.teamscala.java.core.web.method.annotation.PageableDefaults;
import io.teamscala.java.core.web.util.UriTemplate;
import io.teamscala.java.sample.models.BoardComment;
import io.teamscala.java.sample.models.BoardContent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 게시판 컨트롤러 샘플.
 *
 * @author 석기원
 */
@Controller
@RequestMapping("/board")
public class BoardController extends BaseController {

    // Instance constants

    protected static final UriTemplate URI_LIST =
            new UriTemplate("redirect:/board/list")
                    .params("page", "condition", "keyword");

    protected static final UriTemplate URI_VIEW =
            new UriTemplate("redirect:/board/view/{id}")
                    .params("page", "condition", "keyword");

    // Dependency injections

    // Request mappings

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(
            @Validated
            @ModelAttribute("boardSearch")
            SimpleSearch search,
            BindingResult bindingResult,
            @PageableDefaults(sort = BoardContent.DEFAULT_ORDER)
            Pageable pageable,
            Model model) throws Exception {

        model.addAttribute("contents", BoardContent.find.search(search).findPaginatedList(pageable));
        return "board/list";
    }

    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Long id, Model model) throws Exception {
        BoardContent content = BoardContent.find.byId(id);
        model.addAttribute("content", content);
        model.addAttribute("comment", new BoardComment(content));
        return "board/view";
    }

    @RequestMapping(value = "/form", method = RequestMethod.GET)
    public String form(Model model) throws Exception {
        model.addAttribute("content", new BoardContent());
        return "board/form";
    }

    @RequestMapping(value = "/form/{id}", method = RequestMethod.GET)
    public String form(@PathVariable Long id, Model model) throws Exception {
        model.addAttribute("content", BoardContent.find.byId(id));
        return "board/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(
            @Validated
            @EntityAttribute
            BoardContent content,
            BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) return "board/form";
        else {
            content.save();
            return URI_VIEW.tmpl().toUriString(content.getId());
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable Long id) throws Exception {
        BoardContent.find.ref(id).remove();
        return URI_LIST.tmpl().toUriString();
    }

    @RequestMapping(value = "/{contentId}/comment/create", method = RequestMethod.POST)
    public String commentCreate(
            @PathVariable Long contentId,
            @Validated
            @EntityAttribute
            BoardComment comment,
            BindingResult bindingResult,
            Model model) throws Exception {

        if (bindingResult.hasErrors()) {
            model.addAttribute("content", BoardContent.find.byId(contentId));
            return "board/view";
        }
        else {
            comment.save();
            return URI_VIEW.tmpl().toUriString(contentId);
        }
    }

    @RequestMapping(value = "/{contentId}/comment/delete/{id}", method = RequestMethod.GET)
    public String commentDelete(@PathVariable Long contentId, @PathVariable Long id) throws Exception {
        BoardComment.find.ref(id).remove();
        return URI_VIEW.tmpl().toUriString(contentId);
    }
}

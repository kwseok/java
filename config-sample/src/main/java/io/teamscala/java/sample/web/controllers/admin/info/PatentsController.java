package io.teamscala.java.sample.web.controllers.admin.info;

import com.mysema.query.jpa.JPQLQuery;
import io.teamscala.java.core.data.Pageable;
import io.teamscala.java.core.util.FilenameUtils;
import io.teamscala.java.core.util.StringUtils;
import io.teamscala.java.core.web.method.annotation.EntityAttribute;
import io.teamscala.java.core.web.method.annotation.PageableDefaults;
import io.teamscala.java.sample.misc.Configs;
import io.teamscala.java.sample.models.Patent;
import io.teamscala.java.sample.models.QPatent;
import io.teamscala.java.sample.web.controllers.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/info/patents")
public class PatentsController extends BaseController {

    @RequestMapping(method = RequestMethod.GET)
    public String query(
            String no,
            @PageableDefaults(sort = Patent.DEFAULT_ORDER) Pageable pageable,
            Model model) throws Exception {

        if (StringUtils.isNotBlank(no)) {
            JPQLQuery query = Patent.find.where(QPatent.patent.no.like("%" + no + "%"));
            model.addAttribute(RESULT, Patent.find.search(query).findPaginatedList(pageable));
        } else {
            model.addAttribute(RESULT, Patent.find.findPaginatedList(pageable));
        }
        return JSON_VIEW_RESULT;
    }

    @RequestMapping(value = "/{patent}", method = RequestMethod.GET)
        public String get(@PathVariable Patent patent, Model model) throws Exception {
            model.addAttribute(RESULT, patent);
        return JSON_VIEW_RESULT;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String save(@EntityAttribute Patent patent, Model model) throws Exception {
        patent.save();
        model.addAttribute(RESULT, patent);
        return JSON_VIEW_RESULT;
    }

    @RequestMapping(value = "/{no}", method = RequestMethod.PUT)
    public String save(@PathVariable String no, @EntityAttribute Patent patent, Model model) throws Exception {
        patent.setNo(no);
        patent.save();
        model.addAttribute(RESULT, patent);
        return JSON_VIEW_RESULT;
    }

    @RequestMapping(value = "/{no}", method = RequestMethod.DELETE)
    public String remove(@PathVariable String no, Model model) throws Exception {
        Patent.find.ref(no).remove();
        model.addAttribute(RESULT, no);
        return JSON_VIEW_RESULT;
    }

    @RequestMapping(value = "/upload/{type}", method = RequestMethod.POST)
    public String uploadFile(
            @PathVariable String type,
            @RequestParam MultipartFile file,
            Model model
    ) throws Exception {
        String path = Configs.glas.getString("upload.patentDir") + File.separator + type;

        String filename = FilenameUtils.getNonComflictFilename(path, file.getOriginalFilename());
        File uploadFile = new File(path, filename);
        file.transferTo(uploadFile);

        Map<String, String> result = new HashMap<>();
        result.put(type + "Name", file.getOriginalFilename());
        result.put(type + "Path", type + File.separator + filename);

        model.addAttribute(RESULT, result);
        return JSON_VIEW_RESULT;
    }

}

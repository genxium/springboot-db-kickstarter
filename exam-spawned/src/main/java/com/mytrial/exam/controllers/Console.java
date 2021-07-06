package com.mytrial.exam.controllers;

import com.mytrial.app.shardingsphereds.BeanExporter;
import com.mytrial.app.security.GrantedRolesMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.script.ScriptException;
import java.io.IOException;

@RestController
@Slf4j
public class Console {

    private final BeanExporter beanExporter;

    @Autowired
    public Console(BeanExporter beanExporter) {
        this.beanExporter = beanExporter;
    }

    @Operation(operationId = "LearnerConsole")
    @GetMapping(value = "/Learner")
    @Secured({GrantedRolesMapper.ROLE_LEARNER_NAME})
    public ModelAndView learnerConsole() throws ScriptException, IOException {
        final ModelAndView mv = new ModelAndView("console");
        /**
         * TODO
         *  - Load "title" and similar info from i18n configs.
         */
        mv.addObject("title", "LearnerConsole");
        mv.addObject("metaKeywords", "......");
        mv.addObject("metaDescription", "......");
        mv.addObject("jsBundleUri", "/bin/player_console.bundle.js");

        return mv;
    }

    @Operation(operationId = "CoopAdminConsole")
    @GetMapping(value = "/CoopAdmin")
    @Secured({GrantedRolesMapper.ROLE_COOPADMIN_NAME})
    public ModelAndView coopAdminConsole(final Model model) {model.addAttribute("title", "LearnerConsole");
        model.addAttribute("title", "CoopAdminConsole");
        model.addAttribute("metaKeywords", "......");
        model.addAttribute("metaDescription", "......");
        model.addAttribute("jsBundleUri", "/bin/writer_console.bundle.js");
        final ModelAndView view = new ModelAndView("console");
        view.addAllObjects(model.asMap());
        return view;
    }
}

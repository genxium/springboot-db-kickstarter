package com.mytrial.auth.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@RestController
public class Console {

    @GetMapping(value = "/login")
    public ModelAndView loginPage() {
        final ModelAndView mv = new ModelAndView("console");
        /**
         * TODO
         *  - Load "title" and similar info from i18n configs.
         */
        mv.addObject("title", "LearnerConsole");
        mv.addObject("metaKeywords", "......");
        mv.addObject("metaDescription", "......");
        mv.addObject("jsBundleUri", "/bin/central_auth_console.bundle.js");

        return mv;
    }
}

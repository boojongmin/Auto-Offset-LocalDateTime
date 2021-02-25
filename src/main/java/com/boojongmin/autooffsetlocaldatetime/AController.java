package com.boojongmin.autooffsetlocaldatetime;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AController {

    @GetMapping("/hello")
    public String hello(Model m) {
        ADto dto = new ADto();
        m.addAttribute("a", dto);
        return "hello";
    }
}

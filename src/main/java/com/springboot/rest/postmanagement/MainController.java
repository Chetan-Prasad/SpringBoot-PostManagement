package com.springboot.rest.postmanagement;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping (path = "/")
    public String indexPage() {
        return "index";
    }
}

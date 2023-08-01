package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {
    
    @GetMapping("/40x")
    public String ex40x(){
        // => /viewresolver/src/main/resources/templates/error/ex40x.mustache
        return "error/ex40x";
    }

    @GetMapping("/50x")
    public String ex50x(){
        // => /viewresolver/src/main/resources/templates/error/ex50x.mustache
        return "error/ex50x";
    }
}

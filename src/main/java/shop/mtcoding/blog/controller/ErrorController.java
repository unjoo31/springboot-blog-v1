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
}

package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    // 로그인 에러
    @GetMapping("/exLogin")
    public String exLogin(){
        // => /viewresolver/src/main/resources/templates/error/exLogin.mustache
        return "error/exLogin";
    }

    // 회원가입 에러
    @GetMapping("/50x")
    public String ex50x(){
        // => /viewresolver/src/main/resources/templates/error/ex50x.mustache
        return "error/ex50x";
    }
    
    // 이상한 접근 에러
    @GetMapping("/40x")
    public String ex40x(){
        // => /viewresolver/src/main/resources/templates/error/ex40x.mustache
        return "error/ex40x";
    }
   
}

package shop.mtcoding.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {
    @GetMapping({"/", "/board"})
    public String index(){
        // => /viewresolver/src/main/resources/templates/index.mustache
        return "index";
    }

    @GetMapping("/board/saveForm")
    public String saveForm(){
        // => /viewresolver/src/main/resources/templates/board/saveForm.mustache
        return "board/saveForm";
    }

    @GetMapping("/board/1")
    public String detail(){
        // => /viewresolver/src/main/resources/templates/board/detail.mustache
        return "board/detail";
    }
}

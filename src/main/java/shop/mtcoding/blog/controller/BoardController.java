package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.BoardRepository;

@Controller
public class BoardController {

    @Autowired
    private HttpSession session;

    @Autowired
    private BoardRepository boardRepository;

    // 게시글 전체보기
    @GetMapping({"/", "/board"})
    public String index(Model model){
        List<Board> boardList = boardRepository.findAll();
        model.addAttribute("boardList", boardList);
        // => /viewresolver/src/main/resources/templates/index.mustache
        return "index";
    }

    // 게시글 작성
    @PostMapping("/board/save")
    public String save(WriteDTO writeDTO) {
        // validation check (유효성 검사)
        if (writeDTO.getTitle() == null || writeDTO.getTitle().isEmpty()) {
            return "redirect:/40x";
        }
        if (writeDTO.getContent() == null || writeDTO.getContent().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증체크
        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) {
            return "redirect:/loginForm";
        }

        boardRepository.save(writeDTO, sessionUser.getId());
        return "redirect:/";
    }

    // 미 로그인 시 게시글 작성 불가능
    @GetMapping("/board/saveForm")
    public String saveForm(){
        // session 정보를 가져온다
        User sessionUser = (User) session.getAttribute("sessionUser");
        // sessionUser 정보가 없으면 로그인 페이지로 이동, 정보가 있으면 게시글 작성 페이지로 이동
        if(sessionUser == null){
            return "redirect:/loginForm";
        }
        // => /viewresolver/src/main/resources/templates/board/saveForm.mustache
        return "board/saveForm";
    }

    // 게시글 상세보기
    // localhost:8080/board/1
    @GetMapping("/board/{id}")
    public String detail(@PathVariable Integer id, Model model){
        Board boardDetail = boardRepository.findById(id);
        model.addAttribute("boardDetail", boardDetail);
        // => /viewresolver/src/main/resources/templates/board/detail.mustache
        return "board/detail";
    }
}

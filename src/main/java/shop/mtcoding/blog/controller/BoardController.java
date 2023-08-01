package shop.mtcoding.blog.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    // 게시글 전체보기(request 사용)
    // mustache의 경우 request가 불가능 해서 Model을 사용했는데 사용할 수 있는 코드를 yml에 넣으면 request를 사용해도 된다
    // localhost:8080
    @GetMapping({"/", "/board"})
    // @RequestParam : 값이 안들어오면 원하는 값을 넣어줌
    // @RequestParam(defaultValue = "0") : 쿼리스트링은 문자열이기 때문에 ""를 이용해서 넣고 페이징은 처음에 0이 들어와야 해서 디폴트 값을 0으로 넣는다
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request){
        // 유효성 검사, 인증 검사는 필요 없다

        List<Board> boardList = boardRepository.findAll(0);

        // 조회가 잘되는지 테스트 해보기
        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        // 화면에 뿌려주기 위해서 request에 담아야 한다
        request.setAttribute("boardList", boardList);

        // 페이지가 넘어가기 위해서 page를 알고 있으니까 바인딩을 한다
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);

        // 첫번째 페이지와 마지막 페이지면 페이징 되지 않게 하기
        request.setAttribute("first", page == 0 ? true : false);
        // 쿼리로 전체페이지 갯수를 찾아내서 last
        request.setAttribute("last", false);

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
    public String detail(@PathVariable Integer id){
        // => /viewresolver/src/main/resources/templates/board/detail.mustache
        return "board/detail";
    }
}

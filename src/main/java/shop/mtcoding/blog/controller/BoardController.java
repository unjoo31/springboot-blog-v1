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

    // 게시글 삭제
    @PostMapping("/board/{id}/delete")
    public String delete(@PathVariable int id){ // 1. PathVariable
        // 2. 인증검사 (로그인 되어 있지 않으면 로그인 페이지 보내기)
        // session에 접근해서 sessionUser키값을 가져오세요
        // null이면 로그인페이지로 보내고 null아니면 3번을 실행하세요
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/login";
        }

        // 3. 모델에 접근해서 삭제 
        // boardRepository.deleteById(id); 호출하고 리턴을 받지 마세요
        // delete from board_tb where id = :id
        boardRepository.deleteById(id);

        return "redirect:/";
    }

    // 게시글 전체보기(request 사용)
    // mustache의 경우 request가 불가능 해서 Model을 사용했는데 사용할 수 있는 코드를 yml에 넣으면 request를 사용해도 된다
    // localhost:8080
    @GetMapping({"/", "/board"})
    // @RequestParam : 값이 안들어오면 원하는 값을 넣어줌
    // @RequestParam(defaultValue = "0") : 쿼리스트링은 문자열이기 때문에 ""를 이용해서 넣고 페이징은 처음에 0이 들어와야 해서 디폴트 값을 0으로 넣는다
    public String index(@RequestParam(defaultValue = "0") Integer page, HttpServletRequest request){
        // 유효성 검사, 인증 검사는 필요 없다

        List<Board> boardList = boardRepository.findAll(0); // page : 1
        // count를 가지고 와서 페이징 하기
        int totalCount = boardRepository.count(); // totalCount = 5
        int totalPage = totalCount / 3; // totalPage = 1
        if(totalCount % 3 > 0){
            totalPage = totalPage +1; // totalPage = 2
        }
        boolean last = totalPage + 1 == page;

        // 조회가 잘되는지 테스트 해보기
        System.out.println("테스트 : " + boardList.size());
        System.out.println("테스트 : " + boardList.get(0).getTitle());

        // 화면에 뿌려주기 위해서 request에 담아야 한다
        request.setAttribute("boardList", boardList);

        // 페이지가 넘어가기 위해서 page를 알고 있으니까 바인딩을 한다
        request.setAttribute("prevPage", page - 1);
        request.setAttribute("nextPage", page + 1);

        // 첫번째 페이지와 마지막 페이지면 페이징 되지 않게 하기
        // 삼항연상자 이용함(? true : false -> page가 0과 같으면 결과는 true가 되고, 그렇지 않으면 결과는 false가 됨)
        request.setAttribute("first", page == 0 ? true : false);

        // 쿼리로 전체페이지 갯수를 찾아내서 last에 넣는다
        request.setAttribute("last", last);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("totalCount", totalCount);

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
    public String detail(@PathVariable Integer id, HttpServletRequest request){ // c
        // 권한체크를 하기 위해서 session에 접근한다
        User sessionUser = (User) session.getAttribute("sessionUser");
        Board board = boardRepository.findById(id); // m

        boolean pageOwner = false;
        // 로그인 되어 있으면 권한을 비교한다
        if(sessionUser != null){
            pageOwner = sessionUser.getId() == board.getUser().getId();
        }
        
        request.setAttribute("board", board);
        request.setAttribute("pageOwner", pageOwner);
        // => /viewresolver/src/main/resources/templates/board/detail.mustache
        return "board/detail"; // v
    }
}

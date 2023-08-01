package shop.mtcoding.blog.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.http.HttpRequest;

import javax.persistence.PostLoad;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.UserRepository;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // request는 가방, session은 서랍
    @Autowired
    private HttpSession session;

    

    // 로그인
    @PostMapping("/login")
    public String login(LoginDTO loginDTO){

        // validation check (유효성 검사)
        if(loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()){
            return "redirect:/exLogin";
        }
        if(loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()){
            return "redirect:/exLogin";
        }

        // 핵심기능
        // 계정이 없다면 로그인 에러 페이지로 이동, 계정이 있다면 메인으로 이동
        try {
            User user = userRepository.findByUsernameAndPassword(loginDTO);
            // 로그인이 되면 session에 저장을 해둬야한다
            session.setAttribute("sessionUser", user);
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/exLogin";
        }
    }

    // 회원가입
    @PostMapping("/join")
    public String join(JoinDTO joinDTO) {

        // validation check (유효성 검사)
        // 프론트에서 막힌 사람들은 타지 않지만 공격자들을 걸러낸다
        if (joinDTO.getUsername() == null || joinDTO.getUsername().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getPassword() == null || joinDTO.getPassword().isEmpty()) {
            return "redirect:/40x";
        }
        if (joinDTO.getEmail() == null || joinDTO.getEmail().isEmpty()) {
            return "redirect:/40x";
        }

        // 핵심기능
        // 중복되는 계정으로 회원가입하는 경우 예외처리
        try {
            userRepository.save(joinDTO);
        } catch (Exception e) {
            return "redirect:/50x";
        }

        return "redirect:/loginForm";
    }

    // // 정상인
    // @PostMapping("/join")
    // public String join(String username, String password, String email){
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);
    // return "redirect:/loginForm";
    // }

    // // 정상이 아니다
    // @PostMapping("/join")
    // public String join(HttpServletRequest request) throws IOException{
    // // username=ssar&password=1234&email=ssar@nate.com
    // BufferedReader br = request.getReader();

    // // 버퍼가 소비됨
    // String body = br.readLine();

    // // 버퍼에 값이 없어서 꺼내지 않음
    // String username = request.getParameter("username");

    // System.out.println("body : " + body);
    // System.out.println("username : " + username);
    // return "redirect:/loginForm";
    // }

    // DS(컨트롤러 메서드 찾기, 바디데이터 파싱)
    // DS가 바디데이터를 파싱안하고 컨트로럴 메서드만 찾은 상황
    // @PostMapping("/join")
    // public String join(HttpServletRequest request){
    // // getParameter() : x-www-form 데이터를 파싱해주는 메서드
    // String username = request.getParameter("username");
    // String password = request.getParameter("password");
    // String email = request.getParameter("email");
    // System.out.println("username : " + username);
    // System.out.println("password : " + password);
    // System.out.println("email : " + email);
    // return "redirect:/loginForm";
    // }

    // ip주소 부여 : 10.5.9.200:포트번호 -> mtcoding.com:포트번호 (dns를 산다)
    // 포트번호 80은 브라우저에서 생략 가능하다
    // localhost, 127.0.0.1 : 루프백주소 (외부로 빠져나가지 않는다)
    // Get에 들어갈 수 있는 것 : a태그, form태그에 method=get
    // /joinForm : 앤드포인트

    @GetMapping("/joinForm")
    public String joinForm() {
        // templates/ 가 기본이라서 user앞에 /를 붙이지 않는다
        // => /viewresolver/src/main/resources/templates/user/joinForm.mustache
        return "user/joinForm"; // ViewResolver 는 templates 폴더를 찾아간다
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        // => /viewresolver/src/main/resources/templates/user/loginForm.mustache
        return "user/loginForm";
    }

    @GetMapping("/updateForm")
    public String updateForm() {
        // => /viewresolver/src/main/resources/templates/user/updateForm.mustache
        return "user/updateForm";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout() {
        // 로그아웃하면 세션을 비워버린다 (세션 무효화) (내 서랍을 비우는 것)
        session.invalidate();
        return "redirect:/";
    }
}

package shop.mtcoding.blog.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.model.Board;
import shop.mtcoding.blog.model.Reply;
import shop.mtcoding.blog.model.User;
import shop.mtcoding.blog.repository.ReplyRepository;

@Controller
public class ReplyController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ReplyRepository replyRepository;

    // 댓글 작성
    @PostMapping("/reply/save")
    public String save(ReplyWriteDTO replyWriteDTO){
        // comment 유효성 검사
        if (replyWriteDTO.getBoardId() == null) {
            return "redirect:/40x";
        }
        if (replyWriteDTO.getComment() == null || replyWriteDTO.getComment().isEmpty()) {
            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }

        // 핵심로직
        replyRepository.save(replyWriteDTO, sessionUser.getId());
        return "redirect:/board/"+replyWriteDTO.getBoardId();
    }

    // 댓글 삭제
    @PostMapping("/reply/{replyId}/delete")
    public String delete(@PathVariable Integer replyId, Integer boardId){
        // 유효성 검사(body로 받은 데이터는 유효성검사가 필요하다)
        if(boardId == null){
            return "redirect:/40x";
        }

        // 인증 검사
        User sessionUser = (User) session.getAttribute("sessionUser");
        if(sessionUser == null){
            return "redirect:/loginForm";
        }
        // 권한검사
        // 댓글을 작성한 id와 session의 id가 같은지 검사한다
        Reply reply = replyRepository.findById(replyId);
        if(reply.getUser().getId() != sessionUser.getId()){
            return "redirect:/40x"; // 403 권한없음
        }

        // 핵심로직
        replyRepository.deleteById(replyId);
        return "redirect:/board/" + boardId;
    }

}

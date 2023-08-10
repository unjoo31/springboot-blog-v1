package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.ReplyWriteDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Reply;

// 현재 IoC컨테이너에 띄워져있는 것
// 내가 띄운 것 : UserController, BoardController, ReplyController, ErrorControlelr
// 내가 띄운 것 : serRepository, BoardRepository, ReplyRepository
// 그냥 떠있는 것 : EntityManager, HttpSession
@Repository
public class ReplyRepository {
    
    @Autowired
    private EntityManager em;

    // 게시글 가져오기
    public List<Reply> findByBoardId(Integer boardId) {
        Query query = em.createNativeQuery("select * from reply_tb where board_id = :boardId", Reply.class);
        query.setParameter("boardId", boardId);
        return query.getResultList();
    }

    // 댓글 저장
    @Transactional
    public void save(ReplyWriteDTO replyWriteDTO, Integer userId) {
        Query query = em
                .createNativeQuery(
                        "insert into reply_tb(comment, board_id, user_id) values(:comment, :boardId, :userId)");

        query.setParameter("comment", replyWriteDTO.getComment());                
        query.setParameter("boardId", replyWriteDTO.getBoardId());
        query.setParameter("userId", userId);
        query.executeUpdate(); // 쿼리 전송
    }

    // 댓글 삭제
    @Transactional
    public void deleteById(Integer replyId){
        Query query = em.createNativeQuery("delete from reply_tb where id = :replyId");
        query.setParameter("replyId", replyId);
        query.executeUpdate();
    }
    
    // 댓글 조회
    public Reply findById(Integer id){
        Query query = em.createNativeQuery("select * from reply_tb where id = :id", Reply.class);
        query.setParameter("id", id);
        return (Reply) query.getSingleResult();
    }
}

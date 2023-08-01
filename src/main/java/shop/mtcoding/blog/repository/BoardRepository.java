package shop.mtcoding.blog.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

@Repository
public class BoardRepository {
    
    @Autowired
    private EntityManager em;

    // 게시글 id로 상세보기
    public Board findById(int id){
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

    // 게시글 목록 전체보기
    public List<Board> findAll(){
        Query query = em.createNativeQuery("select * from board_tb", Board.class);
        List<Board> boardList = query.getResultList();
        return boardList;
    }

    // 게시글 저장
    @Transactional
    public void save(WriteDTO writeDTO, Integer userId) {
        Query query = em
                .createNativeQuery(
                        "insert into board_tb(title, content, user_id, created_at) values(:title, :content, :userId, now())");

        query.setParameter("title", writeDTO.getTitle());
        query.setParameter("content", writeDTO.getContent());
        query.setParameter("userId", userId);
        query.executeUpdate();
    }
}

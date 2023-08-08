package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.qlrm.mapper.JpaResultMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.BoardDetailDTO;
import shop.mtcoding.blog.dto.UpdateDTO;
import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

@Repository
public class BoardRepository {
    
    @Autowired
    private EntityManager em;

    // 게시글 삭제
    @Transactional
    public void deleteById(int id){
        Query query = em.createNativeQuery("delete from board_tb where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    // 게시글 목록 전체보기
    // localhost:8080?page=0
    public List<Board> findAll(int page){
        // 상수를 만들기 (상수는 대문자)
        final int SIZE = 3;
        // 3개씩 페이징을해서 조회하기(page : 변수, 3 : 상수)
        Query query = em.createNativeQuery("select * from board_tb order by id desc limit :page, :size", Board.class);
        // 3개씩 페이징을 할거라서 0, 3, 이렇게 늘어나야한다
        query.setParameter("page", page * SIZE);
        query.setParameter("size", SIZE);
      
        return query.getResultList();
    }

    // select id, title from board_tb
    // resultClass 안붙이고 직접 파싱하려면 Object[]로 리턴됨
    // object[0] = 1
    // object[1] = 제목1

    // count 갯수
    public int count(){
        // Entity(Board, User) 타입만 가능함
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM BOARD_TB;");
        
        // 원래는 object 배열로 리턴 받는다, object 배열은 칼럼의 연속이다.
        // 그룹함수를 써서, 하나의 칼럼을 조회하면 object로 리턴된다.
        BigInteger count = (BigInteger) query.getSingleResult();
        return count.intValue();
    }

    // count 갯수 내가 만든거
    public int findCount() {
        Query query = em.createNativeQuery("SELECT COUNT(*) FROM BOARD_TB;");
        BigInteger boardCount = (BigInteger) query.getSingleResult();
        return boardCount.intValue();
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

    // 게시글 상세보기
    public Board findById(Integer id) {
        Query query = em.createNativeQuery("select * from board_tb where id = :id", Board.class);
        query.setParameter("id", id);
        Board board = (Board) query.getSingleResult();
        return board;
    }

    // 게시글 업데이트
    @Transactional
    public void update(Integer id, UpdateDTO updateDTO) {
        Query query = em.createNativeQuery("update board_tb set title = :title, content = :content where id = :id");
        query.setParameter("id", id);
        query.setParameter("title", updateDTO.getTitle());
        query.setParameter("content", updateDTO.getTitle());
        query.executeUpdate();
    }

    // 게시글과 댓글 조인
    public List<BoardDetailDTO> findByIdJoinReply(Integer boardId, Integer sessionUserId) {
        String sql = "select ";
        sql += "b.id board_id, ";
        sql += "b.content board_content, ";
        sql += "b.title board_title, ";
        sql += "b.user_id board_user_id, ";
        sql += "r.id reply_id, ";
        sql += "r.comment reply_comment, ";
        sql += "r.user_id reply_user_id, ";
        sql += "ru.username reply_user_username, ";
        if (sessionUserId == null) {
            sql += "false reply_owner ";
        } else {
            sql += "case when r.user_id = :sessionUserId then true else false end reply_owner ";
        }

        sql += "from board_tb b left outer join reply_tb r ";
        sql += "on b.id = r.board_id ";
        sql += "left outer join user_tb ru ";
        sql += "on r.user_id = ru.id ";
        sql += "where b.id = :boardId ";
        sql += "order by r.id desc";
        Query query = em.createNativeQuery(sql);
        query.setParameter("boardId", boardId);
        if (sessionUserId != null) {
            query.setParameter("sessionUserId", sessionUserId);
        }

        JpaResultMapper mapper = new JpaResultMapper();
        List<BoardDetailDTO> dtos = mapper.list(query, BoardDetailDTO.class);
        return dtos;
    }
}

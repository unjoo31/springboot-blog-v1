package shop.mtcoding.blog.repository;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.WriteDTO;
import shop.mtcoding.blog.model.Board;

@Repository
public class BoardRepository {
    
    @Autowired
    private EntityManager em;

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

    // board 카운트 갯수 내가 만든거
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
}

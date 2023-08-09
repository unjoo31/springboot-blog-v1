package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.dto.UserUpdateDTO;
import shop.mtcoding.blog.model.User;

// IoC 컨테이너에 떠있는 것들
// 직접 띄운것 : BoardController, UserController, UserRepository
// Spring 이 띄워주는 것 : EntityManager, HttpSession
@Repository
public class UserRepository {
    
    @Autowired
    private EntityManager em;

    // 회원가입 시 동일 계정잡기
    public User findByUsername(String username){
        try {
            Query query = em.createNativeQuery("select * from user_tb where username = :username", User.class);
            query.setParameter("username", username);
            return (User) query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
        
    }

    // 로그인
    public User findByUsernameAndPassword(LoginDTO loginDTO){
        Query query = em.createNativeQuery("select * from user_tb where username = :username AND password = :password", User.class);
        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());
        return (User) query.getSingleResult();
    }

    // 회원가입
    @Transactional // 1. 일의 최소 단위 2. 고립성
    public void save(JoinDTO joinDTO) {
        Query query = em
                .createNativeQuery(
                        "insert into user_tb(username, password, email) values(:username, :password, :email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate(); // 쿼리를 전송 (DBMS)
    }     

    // 정보수정
    @Transactional
    public void update(UserUpdateDTO userUpdateDTO){
        Query query = em.createNativeQuery("update user_tb set password = :password");
        query.setParameter("password", userUpdateDTO.getPassword());
        query.executeUpdate();
    }
}

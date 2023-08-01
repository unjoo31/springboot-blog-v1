package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;
import shop.mtcoding.blog.dto.LoginDTO;
import shop.mtcoding.blog.model.User;

// IoC 컨테이너에 떠있는 것들
// 직접 띄운것 : BoardController, UserController, UserRepository
// Spring 이 띄워주는 것 : EntityManager, HttpSession
@Repository
public class UserRepository {
    
    @Autowired
    private EntityManager em;

    // 로그인
    public User findByUsernameAndPassword(LoginDTO loginDTO){
        Query query = em.createNativeQuery("select * from user_tb where username = :username AND password = :password", User.class);
        query.setParameter("username", loginDTO.getUsername());
        query.setParameter("password", loginDTO.getPassword());
        return (User) query.getSingleResult();
    }

    // 회원가입
    @Transactional
    public void save(JoinDTO joinDTO){
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values(:username, :password, :email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }
}

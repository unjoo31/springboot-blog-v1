package shop.mtcoding.blog.repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import shop.mtcoding.blog.dto.JoinDTO;

// IoC 컨테이너에 떠있는 것들
// 직접 띄운것 : BoardController, UserController, UserRepository
// Spring 이 띄워주는 것 : EntityManager, HttpSession
@Repository
public class UserRepository {
    
    @Autowired
    private EntityManager em;

    // 
    @Transactional
    public void save(JoinDTO joinDTO){
        Query query = em.createNativeQuery("insert into user_tb(username, password, email) values(:username, :password, :email)");
        query.setParameter("username", joinDTO.getUsername());
        query.setParameter("password", joinDTO.getPassword());
        query.setParameter("email", joinDTO.getEmail());
        query.executeUpdate();
    }
}

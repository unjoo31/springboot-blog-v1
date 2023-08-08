package shop.mtcoding.blog.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

// user(1) - reply(n)
// board(1) - reply(n)
@Setter
@Getter
@Table(name = "reply_tb")
@Entity
public class Reply {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @Column(nullable = false, length = 100) // 댓글 제약조건 : null 허용하지않고 길이는 100자
    private String comment; // 댓글내용

    @JoinColumn(name = "user_id") // fk의 컬럼명을 지정할 수 있다
    @ManyToOne
    private User user; // fk (user_id)

    @ManyToOne
    private Board board; // fk (board_id)
}

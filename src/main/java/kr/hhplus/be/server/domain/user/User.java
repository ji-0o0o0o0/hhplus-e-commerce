package kr.hhplus.be.server.domain.user;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.common.entity.AuditableEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends AuditableEntity {
    /*유저 아이디*/
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    public static User of() {
        return new User();
    }
    public static User create(Long id){User user=new User(); user.id=id; return user;}
}
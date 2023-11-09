package ed.service.messaging.entity.jpa;

import ed.service.messaging.enums.UserE;
import ed.service.messaging.utils.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(columnDefinition = "VARCHAR(36)")
    private String id;

    @Column(name = "first_name", columnDefinition = "VARCHAR(75)")
    private String firstName;

    @Column(name = "last_name_first", columnDefinition = "VARCHAR(75)")
    private String lastNameFirst;

    @Column(name = "last_name_second", columnDefinition = "VARCHAR(75)")
    private String lastNameSecond;

    @Column(name = "email", columnDefinition = "VARCHAR(75)")
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(254)")
    private String password;

    @Column(name = "status", columnDefinition = "VARCHAR(75)")
    private String status;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "aes_key", length = 512)
    private String aesKey;

    public User(){
        id = UUID.randomUUID().toString();
        status = UserE.ACTIVE.getStatus();
        createdAt = DateUtil.date().getTime();
    }

}

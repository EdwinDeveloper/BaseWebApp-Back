package ed.service.messaging.entity.jpa;

import ed.service.messaging.dto.UserDTO;
import ed.service.messaging.enums.UserE;
import ed.service.messaging.enums.UserTypes;
import ed.service.messaging.listener.UserListener;
import ed.service.messaging.security.TFA.TFAService;
import ed.service.messaging.security.encrypt.EncryptorHandler;
import ed.service.messaging.utils.DateUtil;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@EntityListeners(UserListener.class)
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

    @Column(name = "email", columnDefinition = "VARCHAR(75)", unique = true)
    private String email;

    @Column(name = "password", columnDefinition = "VARCHAR(254)")
    private String password;

    @Column(name = "status", columnDefinition = "VARCHAR(75)")
    private String status;

    @Column(name = "type", columnDefinition = "VARCHAR(75)")
    private String type;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "updated_at")
    private Long updatedAt;

    @Column(name = "aes_key", length = 512)
    private String aesKey;

    @Column(name = "tfa")
    private String tfa;

    public User(){
        id = UUID.randomUUID().toString();
        status = UserE.ACTIVE.getStatus();
        createdAt = DateUtil.date().getTime();
    }

    public User(UserDTO userDTO, String encoderPassword){
        id = UUID.randomUUID().toString();
        status = UserE.ACTIVE.getStatus();
        createdAt = DateUtil.date().getTime();

        firstName = userDTO.getFirstName();
        lastNameFirst = userDTO.getLastNameFirst();
        lastNameSecond = userDTO.getLastNameSecond();
        email = userDTO.getEmail();
        password = encoderPassword;
        aesKey = EncryptorHandler.createAESKey();

        type = userDTO.getType() != null ? userDTO.getType() : UserTypes.END_USER.getType();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNameFirst() {
        return lastNameFirst;
    }

    public void setLastNameFirst(String lastNameFirst) {
        this.lastNameFirst = lastNameFirst;
    }

    public String getLastNameSecond() {
        return lastNameSecond;
    }

    public void setLastNameSecond(String lastNameSecond) {
        this.lastNameSecond = lastNameSecond;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }


    public String getTfa() {
        return tfa;
    }

    public void setTfa(String tfa) {
        this.tfa = tfa;
    }
}

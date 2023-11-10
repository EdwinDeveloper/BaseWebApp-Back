package ed.service.messaging.listener;

import ed.service.messaging.config.SpringApplicationContext;
import ed.service.messaging.entity.jpa.User;
import ed.service.messaging.services.EncryptionService;
import org.springframework.stereotype.Component;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Component
public class UserListener {

    private EncryptionService encryptionService;

    private void injectDependencies() {
        if (encryptionService == null) {
            encryptionService = SpringApplicationContext.getBean(EncryptionService.class);
        }
    }

    @PrePersist
    @PreUpdate
    public void prePersist(User user){
        injectDependencies();
        user.setPassword(encryptionService.encrypt(user.getPassword()));

    }

    @PostLoad
    @PostUpdate
    public void postLoad(User user) {
        injectDependencies();
        user.setPassword(encryptionService.decrypt(user.getPassword()));
    }

}

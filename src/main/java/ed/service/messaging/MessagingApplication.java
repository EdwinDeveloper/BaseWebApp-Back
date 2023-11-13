package ed.service.messaging;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = "ed.service.messaging")
public class MessagingApplication {
    public static void main(String[] args){
        SpringApplication.run(MessagingApplication.class, args);
    }
}

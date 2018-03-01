package homo.efficio.scratchpad.java8.springbootcompletablefuture.remote2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@SpringBootApplication
public class RemoteServer2Application {

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8082");
        System.setProperty("server.tomcat.max-threads", "1000");
        SpringApplication.run(RemoteServer2Application.class, args);
    }
}

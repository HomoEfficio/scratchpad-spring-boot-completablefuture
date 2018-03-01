package homo.efficio.scratchpad.java8.springbootcompletablefuture.remote1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@SpringBootApplication
public class RemoteServer1Application {

    public static void main(String[] args) {
        System.setProperty("SERVER_PORT", "8081");
        System.setProperty("server.tomcat.max-threads", "1000");
        SpringApplication.run(RemoteServer1Application.class, args);
    }
}

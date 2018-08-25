package homo.efficio.scratchpad.java8.springbootcompletablefuture.remote1;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@RequestMapping("/server1")
@RestController
@Slf4j
public class RemoteService1 {

    @GetMapping("/service1")
    public String service(int index) throws InterruptedException {
        Thread.sleep(500);
        return String.format("Result of Remote Service1-%03d", index);
    }

}

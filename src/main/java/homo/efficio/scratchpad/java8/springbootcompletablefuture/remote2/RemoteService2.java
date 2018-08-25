package homo.efficio.scratchpad.java8.springbootcompletablefuture.remote2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@RequestMapping("/server2")
@RestController
@Slf4j
public class RemoteService2 {

    @GetMapping("/service2")
    public String service(String str) throws InterruptedException {
        Thread.sleep(500);
//        throw new RuntimeException();  // 고의로 에러 유발
        return "*** " + str + " ***";
    }

}

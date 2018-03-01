package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@Configuration
public class ExecutorConfig {

    @Bean("threadTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        // 아래 값을 1 ~ 10 으로 변경해가면서 테스트
        tpte.setCorePoolSize(1);
        tpte.setMaxPoolSize(1);
        tpte.setQueueCapacity(9);
        return tpte;
    }
}

package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean
    @ConfigurationProperties("threadpool.taskexecutor.single")
    public ThreadPoolTaskExecutorProperties threadPoolTaskExecutorProperties() {
        return new ThreadPoolTaskExecutorProperties();
    }

    @Bean("threadTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        return new ThreadPoolTaskExecutorSingle(threadPoolTaskExecutorProperties())
                .getThreadPoolTaskExecutor();
    }

    @Bean("tenThreadTaskExecutor")
    public ThreadPoolTaskExecutor tenThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor tpte = new ThreadPoolTaskExecutor();
        // 아래 값을 1 ~ 10 으로 변경해가면서 테스트
        tpte.setCorePoolSize(10);
        tpte.setMaxPoolSize(10);
        tpte.setQueueCapacity(9);
        return tpte;
    }
}

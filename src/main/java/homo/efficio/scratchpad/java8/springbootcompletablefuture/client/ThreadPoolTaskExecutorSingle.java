package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PreDestroy;

/**
 * @author homo.efficio@gmail.com
 * created on 2018-03-02
 */
public class ThreadPoolTaskExecutorSingle {

    private ThreadPoolTaskExecutor tpte;

    public ThreadPoolTaskExecutorSingle(ThreadPoolTaskExecutorProperties tpteProps) {
        tpte = new ThreadPoolTaskExecutor();
        tpte.setCorePoolSize(tpteProps.getCorePoolSize());
        tpte.setMaxPoolSize(tpteProps.getMaxPoolSize());
        tpte.setQueueCapacity(tpteProps.getQueueCapacity());
        tpte.setKeepAliveSeconds(tpteProps.getKeepAliveSeconds());
        tpte.setThreadNamePrefix(tpteProps.getThreadNamePrefix());
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        return tpte;
    }

    @PreDestroy
    public void shutdown() {
        System.out.println("ThreadPoolTaskExecutor is being shutdown!!!");
        tpte.shutdown();
    }
}

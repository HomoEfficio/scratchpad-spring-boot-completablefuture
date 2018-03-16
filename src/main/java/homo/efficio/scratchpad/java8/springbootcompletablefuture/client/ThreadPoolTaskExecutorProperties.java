package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import lombok.Getter;
import lombok.Setter;

/**
 * @author homo.efficio@gmail.com
 * created on 2018-03-02
 */
@Getter
@Setter
public class ThreadPoolTaskExecutorProperties {

    private String threadNamePrefix;
    private int corePoolSize;
    private int maxPoolSize;
    private int queueCapacity;
    private int keepAliveSeconds;
}

package homo.efficio.scratchpad.java8.springbootcompletablefuture.loadtester;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@Slf4j
public class LoadTester {

    private static AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(100);

        RestTemplate rt = new RestTemplate();
        String urlSync = "http://localhost:8080/normal/sync?index=";
        String urlASync = "http://localhost:8080/normal/async?index=";

        StopWatch mainWatch = new StopWatch();
        mainWatch.start();

        for (int i = 0; i < 10; i++) {
            es.execute(() -> {
                int index = counter.addAndGet(1);

                StopWatch subWatch = new StopWatch();
                subWatch.start();

                // Sync와 Async 번갈아 가며 비교 테스트
//               String result = rt.getForObject(urlSync + index, String.class);
                String result = rt.getForObject(urlASync + index, String.class);

                subWatch.stop();
                log.info(String.format("%s %s Elapsed: %s", index, result, subWatch.getTotalTimeSeconds()));
            });
        }

        // es에 shutdown 신호를 보내며 shutdown 될 때까지 현재 스레드를 blocking 하지 않음
        es.shutdown();

        // 실제 shutdown 되거나 timeout 될 때까지 es 사용을 방지하며 현재 스레드를 blocking 하지 않음
        es.awaitTermination(100, TimeUnit.SECONDS);

        mainWatch.stop();
        log.info(String.format("Total: %s", mainWatch.getTotalTimeSeconds()));
    }
}

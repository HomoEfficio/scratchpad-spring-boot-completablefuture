package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.*;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@Service
public class NormalService {

    private RestTemplate rt;

    private AsyncRestTemplate art;

    private SpyAsyncRestTemplate spyArt;

    private TaskExecutor taskExecutor;

    @Autowired
    public NormalService(@Qualifier("threadTaskExecutor") AsyncListenableTaskExecutor executor) {
        this.rt = new RestTemplate();

        // AsyncRestTemplate이
        // 원래의 스레드를 blocking 하지 않고
        // AsyncListenableTaskExecutor에서 얻은 별도의 스레드를 통해 비동기로 외부에 HTTP 요청을 날리지만,
        // 별도의 스레드는 외부에서 HTTP 응답이 올 때까지 blocking 됨
        // 따라서 AsyncListenableTaskExecutor의 maxPoolSize가 1이면
        // 새로 별도의 스레드를 생성하지 못하므로
        // 외부로 HTTP 요청을 바로 보내지 못하고 AsyncListenableTaskExecutor의 큐에서 대기하므로
        // AsyncRestTemplate이라 하더라도 별다른 성능 향상 효과를 볼 수 없음
        // AsyncListenableTaskExecutor의 maxPoolSize가 1이면서 큐의 사이즈까지 1이라면
        // 1번째 요청은 HTTP 요청을 날리고 OK 응답을 받으나
        // 2번째 요청은 큐에서 대기하다가 CompletionException 발생 - 설명 보완 필요
        // 3번째 요청부터는 아예 큐에도 못 들어가고 바로 HttpServerErrorException 발생
        this.art = new AsyncRestTemplate(executor);

        // 아무 파라미터 없이 AsyncRestTemplate를 생성하면 SimpleAsyncTaskExecutor 사용.
        // SimpleAsyncTaskExecutor는 pool을 통해 스레드를 재사용하지 않고
        // 필요 할 때마다 스레드를 새로 생성/폐기하므로 스레드 생성 부하가 크며 실전에 사용하면 안 됨
//        this.art = new AsyncRestTemplate();


        this.spyArt = new SpyAsyncRestTemplate(executor);

        this.taskExecutor = executor;
    }

    public String getResultFromRemote1(int index) {
        final String url = "http://localhost:8081/server1/service1?index=" + index;
        return rt.getForEntity(url, String.class).getBody();
    }

    public ListenableFuture<ResponseEntity<String>> getResultFromRemoteAsync1(int index) {
        final String url = "http://localhost:8081/server1/service1?index=" + index;
        return art.getForEntity(url, String.class);
    }

    public String getResultFromRemote2(String str) {
        final String url = "http://localhost:8082/server2/service2?str=" + str;
        return rt.getForEntity(url, String.class).getBody();
    }

    public ListenableFuture<ResponseEntity<String>> getResultFromRemoteAsync2(String str) {
        final String url = "http://localhost:8082/server2/service2?str=" + str;
        return art.getForEntity(url, String.class);
    }

    public ListenableFuture<ResponseEntity<String>> getResultFromRemoteSpyArt(String str) {
        final String url = "http://localhost:8082/server2/service2?str=" + str;
        System.out.println("Thread name in Service method: " + Thread.currentThread().getName());
        return spyArt.getForEntity(url, String.class);
    }

    public Callable<String> returningCallable() {
        return () -> {
            System.out.println("Thread name returning Callable: " + Thread.currentThread().getName());
            return "Callable Returning Controller, thread-name: " + Thread.currentThread().getName();
        };
    }

    @Async
    public void showSpringAsyncWithoutValue() {
        System.out.println("Thread name of @Async without value attribute: " + Thread.currentThread().getName());
    }

    @Async("tenThreadTaskExecutor")
    public CompletableFuture<String> showFromCompletableFuture(int index) throws InterruptedException {
        System.out.println("Inside of @Async+CompletableFuture method, request index: " + index + ", thread name: " + Thread.currentThread().getName());
        Thread.sleep(2000);
        String info = "Result of @Async+CompletableFuture method, request index: " + index + ", thread name: " + Thread.currentThread().getName();
        System.out.println(info);
        return CompletableFuture.completedFuture(info);
    }

    @Async("tenThreadTaskExecutor")
    public String showFromCompletableFuture2(int index) throws InterruptedException {
        System.out.println("Inside of @Async+CompletableFuture method, request index: " + index + ", thread name: " + Thread.currentThread().getName());
        Thread.sleep(2000);
        String info = "Result of @Async+CompletableFuture method, request index: " + index + ", thread name: " + Thread.currentThread().getName();
        System.out.println(info);
        return info;
    }

    public String showRawInfo(int index) throws InterruptedException {
        System.out.println("Inside of normal Sync method, request index: " + index + ", thread name: " + Thread.currentThread().getName());
        Thread.sleep(2000);
        String info = "Result of normal Sync method, request index: " + index + ", thread name: " + Thread.currentThread().getName();
        System.out.println(info);
        return info;
    }

    public CompletableFuture<String> combineTest() {
        return CompletableFuture.supplyAsync(
                () -> buildNum(new int[]{3, 1, 4})
                , this.taskExecutor)
                .thenCombine(
                        CompletableFuture.supplyAsync(
                                () -> buildNum(new int[]{})
                                , this.taskExecutor)
                        , (a, b) -> a + b
                )
                .thenApply(
                        r -> "*** " + r + " ***"
                );
    }

    private int buildNum(int[] digits) {
        int result = 0;
//        for (int i = 0, len = digits.length ; i < len ; i++) {
//            result = result * 10 + digits[i];
//        }

//        result = Arrays.stream(digits)
//                .reduce(0, (acc, v) -> acc * 10 + v);

        Arrays.stream(digits)
                .findFirst()
                .ifPresent(r -> System.out.println("$$$ " + r + " $$$"));

        return result;
    }
}


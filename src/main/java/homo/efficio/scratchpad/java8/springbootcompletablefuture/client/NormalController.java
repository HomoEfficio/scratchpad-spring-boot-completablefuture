package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

/**
 * @author homo.efficio@gmail.com
 * Created on 2018-03-01.
 */
@RestController
@RequestMapping("/normal")
@Slf4j
public class NormalController {

    private final NormalService normalService;

    private final TaskExecutor taskExecutor;

    @Autowired
    public NormalController(NormalService normalService, @Qualifier("tenThreadTaskExecutor") TaskExecutor taskExecutor) {
        this.normalService = normalService;
        this.taskExecutor = taskExecutor;
    }

    @GetMapping("/sync")
    public ResponseEntity<String> getSync(@RequestParam("index") int index) {
        final String result1 = this.normalService.getResultFromRemote1(index);
        final String result2 = this.normalService.getResultFromRemote2(result1);
        return ResponseEntity.ok(result2);
    }

    @GetMapping("/async")
    public DeferredResult<ResponseEntity<String>> getAsync(int index) {
        DeferredResult<ResponseEntity<String>> dr = new DeferredResult<>();

//        this.normalService.getResultFromRemoteAsync1(index)
//                .addCallback(
//                        re1 -> {
//                            System.out.println("Thread name of art1: " + Thread.currentThread());
//                            this.normalService.getResultFromRemoteAsync2(re1.getBody())
//                                    .addCallback(
//                                            re2 -> {
//                                                dr.setResult(re2);
//                                                System.out.println("Thread name of art2: " + Thread.currentThread());
//                                            },  // callback hell
//                                            e -> dr.setErrorResult(e.toString())  // error 처리 중복
//                                    );
//                        },
//                        e -> dr.setErrorResult(e.toString())  // error 처리 중복
//                );



        getCfFromLf(this.normalService.getResultFromRemoteAsync1(index))
                .thenCompose(re -> {
                    System.out.println("Thread name of art1: " + Thread.currentThread());
                    return getCfFromLf(this.normalService.getResultFromRemoteAsync2(re.getBody()));
                })
                .thenAccept((re) -> {
                    System.out.println("Thread name of art2: " + Thread.currentThread());
                    dr.setResult(re);
                })
                .exceptionally(e -> {
                    dr.setErrorResult(e.toString());
                    return null;
                });

        return dr;
    }

    @GetMapping("/spyAsync")
    public CompletableFuture<ResponseEntity<String>> getCompletableFuture(String any) {

        ListenableFuture<ResponseEntity<String>> resultFromRemoteSpyArt = this.normalService.getResultFromRemoteSpyArt(any);

        System.out.println("Right after receiving ListenableFuture, thread name: " + Thread.currentThread().getName());

        return getCfFromLf(resultFromRemoteSpyArt)
                .thenApply(re -> {
                    System.out.println("Thread name of thenApply() of Controller: " + Thread.currentThread().getName());
                    System.out.println("Result: " + re);
                    return re;
                });
    }

    @GetMapping("/thread-name/async-annotation-without-value")
    public void getThreadNameAsyncWithoutValue() {
        this.normalService.showSpringAsyncWithoutValue();
    }

    @GetMapping("thread-name/callable")
    public Callable<String> returningCallable() {
        return this.normalService.returningCallable();
    }

    @GetMapping("/completablefuture-in-service")
    public CompletableFuture<String> returningCompletableFutureFromService(int index) throws InterruptedException {

        CompletableFuture<String> stringCompletableFuture = this.normalService.showFromCompletableFuture(index);

        System.out.println("Right after invoking CompletableFuture with Request index: " + index + " in thread: " + Thread.currentThread().getName());
        System.out.println("Right after invoking, result: " + stringCompletableFuture);

        return stringCompletableFuture;
    }

    @GetMapping("/completablefuture-in-service2")
    public CompletableFuture<String> returningCompletableFutureFromService2(int index) throws InterruptedException {

        // 이렇게 @Async가 있는 곳에서 Future가 아닌 결과를 받고
        String normalString = this.normalService.showFromCompletableFuture2(index);

        System.out.println("Right after invoking CompletableFuture with Request index: " + index + " in thread: " + Thread.currentThread().getName());
        System.out.println("Right after invoking, result: " + normalString);

        // 이렇게 @Async가 없는 곳에서 CompletableFuture.completedFuture를 만들어 반환하면
        // normalString에는 아무런 결과값이 없는 상태로 null을 반환하고 종료되버림
        return CompletableFuture.completedFuture(normalString);
    }

    @GetMapping("/completablefuture-in-controller")
    public CompletableFuture<String> returningCompletableFutureFromController(int index) {

        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(
                () -> {
                    String result = "";
                    try {
                        result = this.normalService.showRawInfo(index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return result;
                },
                this.taskExecutor
        );

        System.out.println("Right after invoking Async Service without CompletableFuture with Request index: " + index + " in thread: " + Thread.currentThread().getName());

        return stringCompletableFuture;
    }

    private <T> CompletableFuture<T> getCfFromLf(ListenableFuture<T> lf) {
        CompletableFuture<T> cf = new CompletableFuture<>();
        lf.addCallback(s -> cf.complete(s), e -> cf.completeExceptionally(e));
        return cf;
    }

    @GetMapping("/multivaluemap")
    public String mvm(@RequestParam MultiValueMap multiValueMap,
                      @RequestParam Map<String, String> paramMap) {

        return multiValueMap.toString();
    }

    @GetMapping("/combine")
    public CompletableFuture<String> combine() {
        return this.normalService.combineTest();
    }
}

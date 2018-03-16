package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

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
//                            this.normalService.getResultFromRemoteAsync2(re1.getBody())
//                                    .addCallback(
//                                            re2 -> dr.setResult(re2),  // callback hell
//                                            e -> dr.setErrorResult(e.toString())  // error 처리 중복
//                                    );
//                        },
//                        e -> dr.setErrorResult(e.toString())  // error 처리 중복
//                );

        getCfFromLf(this.normalService.getResultFromRemoteAsync1(index))
                .thenCompose(re -> getCfFromLf(this.normalService.getResultFromRemoteAsync2(re.getBody())))
                .thenAccept(re -> dr.setResult(re))
                .exceptionally(e -> {
                    dr.setErrorResult(e.toString());
                    return null;
                });

        return dr;
    }

    @GetMapping("/thread-name/async-without-value")
    public void getThreadNameAsyncwithoutValue() {
        this.normalService.showSpringAsyncWithoutValue();
    }

    @GetMapping("/callable")
    public Callable<String> returningCallable() {
        return this.normalService.returningCallable();
    }

    @GetMapping("/completable-future-service")
    public CompletableFuture<String> returningCompletableFutureFromService(int index) throws InterruptedException {

        CompletableFuture<String> stringCompletableFuture = this.normalService.showFromCompletableFuture(index);

        System.out.println("Right after invoking CompletableFuture with Request index: " + index + " in thread: " + Thread.currentThread().getName());

        return stringCompletableFuture;
    }

    @GetMapping("/completable-future-controller")
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
}

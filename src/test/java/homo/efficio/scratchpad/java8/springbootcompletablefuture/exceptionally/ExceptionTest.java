package homo.efficio.scratchpad.java8.springbootcompletablefuture.exceptionally;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author homo.efficio@gmail.com
 * created on 2018-08-25
 */
@Slf4j
public class ExceptionTest {

    @Test
    public void checked_exception_감싸던지기() {
        try {
            System.out.println(URLEncoder.encode("-&-", "utf_8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("그런 인코딩은 없습니다요~", e);
        }
    }

//    @Test
//    public void 다른_쓰레드_안에서_checked_exception_감싸던지기() {
//        System.out.println("Main thread: " + Thread.currentThread().getName());
//        new Thread(
//                () -> {
//                    try {
//                        System.out.println("Thread: " + Thread.currentThread().getName());
//                        System.out.println(URLEncoder.encode("-&-", "utf_8"));
//                    } catch (UnsupportedEncodingException e) {
//                        throw new RuntimeException(" 그런 인코딩은 없습니다요~", e);
//                    }
//                }
//        ).start();
//    }

    @Test
    public void main_쓰레드_살아계시는_동안_다른_쓰레드_안에서_checked_exception_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        new Thread(
                () -> {
                    try {
                        System.out.println("Thread: " + Thread.currentThread().getName());
                        System.out.println(URLEncoder.encode("-&-", "utf_8"));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(" 그런 인코딩은 없습니다요~", e);
                    }
                }
        ).start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(" 인터럽티드 예외 잡아 던집니다요~", e);
        }
    }

    @Test
    public void 다른_쓰레드에서_발생한_unchecked_exception_을_main_쓰레드에서_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        try {
            new Thread(
                    () -> {
                        throw new IllegalStateException();
                    }
            ).start();
        } catch (Exception e) {
            throw new RuntimeException(" 런타임 예외 잡아 던집니다요~", e);
        }
    }

    @Test
    public void 쓰레드_전용_예외핸들러를_장착하고_예외핸들러에서_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        try {
            Thread myThread = new Thread(
                    () -> {
                        throw new IllegalStateException();
                    }
            );
            myThread.setUncaughtExceptionHandler(
                    (thread, e) -> {
                        throw new RuntimeException(thread.getName() + " 에서 런타임 예외 잡아 던집니다요~", e);
                    }
            );
            myThread.start();
        } catch (Exception e) {
            throw new RuntimeException(" main 에서 런타임 예외 잡아 던집니다요~", e);
        }
    }

    @Test
    public void 쓰레드_전용_예외핸들러를_장착하고_예외핸들러에서_stacktrace_찍고_쫑내기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        try {
            Thread myThread = new Thread(
                    () -> {
                        throw new IllegalStateException();
                    }
            );
            myThread.setUncaughtExceptionHandler(
                    (thread, e) -> {
                        System.out.println(thread.getName() + " 에서 런타임 예외 잡아 던집니다요~");
                        e.printStackTrace();
                    }
            );
            myThread.start();
        } catch (Exception e) {
            throw new RuntimeException(" main 에서 런타임 예외 잡아 던집니다요~", e);
        }
    }


    @Test
    public void exceptionally_에서_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        CompletableFuture.runAsync(
                () -> {
                    throw new IllegalStateException();
                }
        ).exceptionally(e -> {
            throw new RuntimeException(" 런타임 예외 잡아 던집니다요~", e);
        });
    }

    @Test
    public void exceptionally_에서_stacktrace_찍고_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        CompletableFuture.runAsync(
                () -> {
                    throw new IllegalStateException();
                }
        ).exceptionally(e -> {
            e.printStackTrace();
            throw new RuntimeException(" 런타임 예외 잡아 던집니다요~", e);
        });
    }

    @Test
    public void 쓰레드에_exceptionally_에서_stacktrace_찍고_감싸던지기() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        CompletableFuture.runAsync(
                () -> {
                    throw new IllegalStateException();
                }
        ).exceptionally(e -> {
            e.printStackTrace();
            throw new RuntimeException(" 런타임 예외 잡아 던집니다요~", e);
        });
    }





    @Test
    public void catching_unchecked_exception_in_other_thread_while_main_thread_waiting() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        try {
            new Thread(
                    () -> {
                        throw new IllegalStateException();
                    }
            ).start();
        } catch (Exception e) {
            throw new RuntimeException(" 런타임 예외 잡아 던집니다요~", e);
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(" 인터럽티드 예외 잡아 던집니다요~", e);
        }
    }

    @Test
    public void exception_in_completable_futuer() {
        System.out.println("Main thread: " + Thread.currentThread().getName());
        CompletableFuture.runAsync(
                () -> {
                    try {
                        System.out.println("Thread: " + Thread.currentThread().getName());
                        System.out.println(URLEncoder.encode("-&-", "utf_8"));
                    } catch (UnsupportedEncodingException e) {
                        System.out.println("Thread: " + Thread.currentThread().getName() + " 예외 잡았습니다요~");
                        throw new RuntimeException("그런 인코딩은 없습니다요~", e);
                    }
                }
        );
    }

//    @Test(expected = RuntimeException.class)
    @Test
    public void exceptionally__does_not_throw_runtimeexception() {
        CompletableFuture.runAsync(
                () -> {
                    throw new IllegalArgumentException();
                }
        ).exceptionally(e -> {
            e.printStackTrace();
            throw new RuntimeException("인덱스가 넘칩니다요~", e);
        });
    }

    @Test
    public void exception_in_cf_runnable() {
        CompletableFuture.runAsync(
                () -> {
                    int[] ints = {1, 2, 3};
                    for (int i = 0 ; i <= 3 ; i++) {
                        System.out.println(ints[i] + " ");
                    }
                }
        );
    }

    @Test
    public void exception_in_runnable() {
        new Thread(
                () -> {
                    int[] ints = {1, 2, 3};
                    for (int i = 0 ; i <= 3 ; i++) {
                        System.out.println(ints[i] + " ");
                    }
                }
        ).start();
    }

    @Test
    public void exception_in_runnable_in_runnable() {
        new Thread(() -> {
            new Thread(
                    () -> {
                        int[] ints = {1, 2, 3};
                        for (int i = 0 ; i <= 3 ; i++) {
                            System.out.println(ints[i] + " ");
                        }
                    }
            ).start();
        }).start();
    }

    @Test
    public void exception_in_runnable_in_executorservice() {
        ExecutorService es = Executors.newSingleThreadExecutor();
        es.submit(() -> {
            int[] ints = {1, 2, 3};
            for (int i = 0 ; i <= 3 ; i++) {
                System.out.println(ints[i] + " ");
            }
        });
    }

    @Test
    public void catch_and_throw_runtimeexception() {
        try {
            int[] ints = {1, 2, 3};
            for (int i = 0 ; i <= 3 ; i++) {
                System.out.println(ints[i] + " ");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("인덱스가 넘칩니다요~", e);
        }
    }

    @Test
    public void throw_runtimeexception_in_lambda() {
        try {
            int[] ints = {1, 2, 3};
            Arrays.stream(ints)
                    .forEach(e -> {
                        Integer n = null;
                        System.out.println(e + n);
                    });
        } catch (NullPointerException e) {
            throw new RuntimeException("널포인터입니다요~", e);
        }
    }
}

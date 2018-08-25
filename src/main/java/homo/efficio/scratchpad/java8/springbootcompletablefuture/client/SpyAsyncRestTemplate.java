package homo.efficio.scratchpad.java8.springbootcompletablefuture.client;

import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestClientException;

/**
 * @author homo.efficio@gmail.com
 * created on 2018-04-03
 */
class SpyAsyncRestTemplate extends AsyncRestTemplate {

    public SpyAsyncRestTemplate(AsyncListenableTaskExecutor taskExecutor) {
        super(taskExecutor);
    }

    @Override
    public <T> ListenableFuture<ResponseEntity<T>> getForEntity(String url, Class<T> responseType, Object... uriVariables)
            throws RestClientException {

        System.out.println("Thread name in AsyncRestTemplate: " + Thread.currentThread().getName());
        return super.getForEntity(url, responseType, uriVariables);
    }
}

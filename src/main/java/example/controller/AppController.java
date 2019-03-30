package example.controller;

import example.dto.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@RestController
public class AppController {
    private final static Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private int serverPort;
    private final String url = "http://localhost:%d";
    private final String urlFirst = url + "/first";
    private final String urlSecond = url + "/second";

    private AtomicLong counter = new AtomicLong();

    @RequestMapping("/first")
    @ResponseBody
    public Data<Long> first() throws InterruptedException {
        final long first = counter.incrementAndGet();
        System.out.printf("first %d%n", first);
        TimeUnit.MILLISECONDS.sleep(3500);
        return new Data<>("first", first);
    }

    @RequestMapping("/second")
    @ResponseBody
    public Data<Long> second() throws InterruptedException {
        final long unixTimestamp = Instant.now().toEpochMilli();
        System.out.printf("second %d%n", unixTimestamp);
        TimeUnit.MILLISECONDS.sleep(3500);
        return new Data<>("first", unixTimestamp);
    }

    @RequestMapping("/third")
    public void third() throws InterruptedException, ExecutionException {
        final String first = String.format(urlFirst, serverPort);
        final String second = String.format(urlSecond, serverPort);
        System.out.println("Start: " + LocalTime.now());

        // LIST OF TASKS
        CompletableFuture<Data> f = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(first, Data.class).getBody(), Executors.newCachedThreadPool());
        CompletableFuture<Data> s = CompletableFuture.supplyAsync(() -> restTemplate.getForEntity(second, Data.class).getBody(), Executors.newCachedThreadPool());
        List<CompletableFuture<Data>> completableFutures = asList(f, s);

        // TASK after all task
        CompletableFuture<Integer> anotherFuture = CompletableFuture.supplyAsync(() -> 2);

        // perform the list of tasks
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]));
        CompletableFuture<List<Data>> listCompletableFuture = voidCompletableFuture
                .thenApplyAsync(v ->
                        completableFutures.stream()
                                .map(CompletableFuture::join)
                                .collect(toList())
                );

        // perform a task after it
        listCompletableFuture
                .thenCombineAsync(anotherFuture, (a, b) -> {
                    System.out.println("______");
                    System.out.println(a);
                    System.out.println(b);
                    return b;
                })
                .get();

        System.out.println("End: " + LocalTime.now());
    }
}

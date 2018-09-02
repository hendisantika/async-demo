package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.lang.Thread.sleep;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;

@RestController("/async")
@SpringBootApplication
@EnableAsync
public class CompletableFuturesRestApplication {

  // exercise using curl http://localhost:8080/async?input=lorem,ipsum,dolor,sit,amet
  @RequestMapping(method = RequestMethod.GET)
  public DeferredResult<String> get(@RequestParam List<String> input) {
    return concatenateAsync(input);
  }

  private DeferredResult<String> concatenateAsync(List<String> input) {
    // Create the collection of futures.
    List<CompletableFuture<String>> futures =
            input.stream()
                    .map(str -> supplyAsync(() -> callApi(str)))
                    .collect(toList());

    // Restructure as varargs because that's what CompletableFuture.allOf requires.
    CompletableFuture<?>[] futuresAsVarArgs = futures.toArray(new CompletableFuture[futures.size()]);

    // Create a new future to gather results once all of the previous futures complete.
    CompletableFuture<Void> ignored = CompletableFuture.allOf(futuresAsVarArgs);

    DeferredResult<String> defResult = new DeferredResult<>();

    // Once all of the futures have completed, build out the result string from results.
    ignored.thenAccept(i -> {
      StringBuilder stringBuilder = new StringBuilder();
      futures.forEach(f -> {
        try {
          stringBuilder.append(f.get());
        } catch (Exception e) {
        }
      });

      defResult.setResult(stringBuilder.toString());
    });

    return defResult;
  }

  String callApi(String str) {
    try {
      // restTemplate.invoke(...)
      sleep(1000);
    } catch (Exception e) {
    }
    return str.toUpperCase();
  }

  public static void main(String[] args) {
    SpringApplication.run(CompletableFuturesRestApplication.class, args);
  }
}
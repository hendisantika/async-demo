# Spring Async

This project is a simple Spring Boot application that demonstrates using Spring's `DeferredResult` combined with
any number of `CompletableFuture` to create an asynchronous REST controller. To use:

1. In the root directory run: `mvn spring-boot:run`
2. Test it with `curl http://localhost:8080/async?input=lorem,ipsum,dolor,sit,amet` or by visiting that URL in a browser.
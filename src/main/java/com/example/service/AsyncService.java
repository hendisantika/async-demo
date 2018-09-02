package com.example.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * Project : async-controller
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 02/09/18
 * Time: 13.39
 * To change this template use File | Settings | File Templates.
 */
@Service
public class AsyncService {
    private static Log log = LogFactory.getLog(AsyncService.class);

    @Async
    public void asyncHello() {
        log.info("hi,springboot async hello,"
                + Thread.currentThread().getName());
    }

    @Async("customFixedThreadPool")
    public void async1() {
        log.info("hi,springboot async,"
                + Thread.currentThread().getName());
    }

    @Async("customFixedThreadPool")
    public Future<String> asyncWithResult() {
        try {
            Thread.sleep(2 * 1000);
            log.info("hi,springboot async with return value,"
                    + Thread.currentThread().getName());
            return new AsyncResult<String>("hi springboot async");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

}
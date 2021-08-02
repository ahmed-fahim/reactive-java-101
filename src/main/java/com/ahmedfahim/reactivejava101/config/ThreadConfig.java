package com.ahmedfahim.reactivejava101.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadConfig {
    public static final Integer MAX_ACTIVE_THREAD_COUNT_BLOCKING = 95;
    public static final Integer MAX_RESERVE_THREAD_COUNT_BLOCKING = 100;
    public static final Long KEEP_ALIVE_TIME_AFTER_EXCEED = 0L;

    @Bean(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BlockingQueue<Runnable> blockingQueue() {
        return new LinkedBlockingQueue<>(MAX_RESERVE_THREAD_COUNT_BLOCKING);
    }

    @Bean(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ThreadPoolExecutor executorService(BlockingQueue<Runnable> blockingQueue) {
        return new ThreadPoolExecutor(
                MAX_ACTIVE_THREAD_COUNT_BLOCKING,
                MAX_RESERVE_THREAD_COUNT_BLOCKING,
                KEEP_ALIVE_TIME_AFTER_EXCEED,
                TimeUnit.MILLISECONDS,
                blockingQueue
        );
    }
}

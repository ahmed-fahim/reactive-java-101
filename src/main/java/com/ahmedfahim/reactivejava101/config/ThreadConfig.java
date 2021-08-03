package com.ahmedfahim.reactivejava101.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

@Configuration
@Slf4j
public class ThreadConfig {
    private static final Long PER_THREAD_MEMORY_IN_BYTES = 1024L * 1024L;
    private static final Double MEMORY_FRACTION_FOR_ACTIVE_THREAD = 1.0 / 8.0;
    private static final Long KEEP_ALIVE_TIME_AFTER_EXCEED = 30L;

    private final int maxActiveThreadCount;
    private final int maxReserveThreadCount;

    public ThreadConfig() {
        long maxJvmMemory = Runtime.getRuntime().maxMemory();
        log.info("maxJvmMemory = {}", maxJvmMemory);
        long memoryAllocationForActiveThread = (long) ((double) maxJvmMemory * MEMORY_FRACTION_FOR_ACTIVE_THREAD);
        this.maxActiveThreadCount = (int) (memoryAllocationForActiveThread / PER_THREAD_MEMORY_IN_BYTES);
        this.maxReserveThreadCount = this.maxActiveThreadCount * 2;
        log.info("maxActiveThreadCount = {}, maxReserveThreadCount = {}", maxActiveThreadCount, maxReserveThreadCount);
    }

    @Bean(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public BlockingQueue<Runnable> blockingQueue() {
        return new LinkedBlockingQueue<>(maxReserveThreadCount*100);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public ExecutorService executorService(BlockingQueue<Runnable> blockingQueue) {
        return new ThreadPoolExecutor(
                maxActiveThreadCount,
                maxReserveThreadCount,
                KEEP_ALIVE_TIME_AFTER_EXCEED,
                TimeUnit.MILLISECONDS,
                blockingQueue
        );
    }

    @Qualifier("THREAD_PER_SERVICE")
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Executor executor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }
}

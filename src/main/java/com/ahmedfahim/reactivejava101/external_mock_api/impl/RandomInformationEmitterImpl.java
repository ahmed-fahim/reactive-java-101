package com.ahmedfahim.reactivejava101.external_mock_api.impl;

import com.ahmedfahim.reactivejava101.external_mock_api.RandomInfoTaskGenerator;
import com.ahmedfahim.reactivejava101.external_mock_api.RandomInformationEmitter;
import com.ahmedfahim.reactivejava101.response.RandomInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Service
@Slf4j
public class RandomInformationEmitterImpl implements RandomInformationEmitter {
    private final Executor executor;
    private final ExecutorService executorService;
    private final RandomInfoTaskGenerator randomInfoTaskGenerator;

    public RandomInformationEmitterImpl(@Qualifier("THREAD_PER_SERVICE") Executor executor,
                                        ExecutorService executorService,
                                        RandomInfoTaskGenerator randomInfoTaskGenerator) {
        this.executor = executor;
        this.executorService = executorService;
        this.randomInfoTaskGenerator = randomInfoTaskGenerator;
    }

    @Override
    public CompletableFuture<RandomInformation> fetchInfoUsingFutureBlocking(String requestTime) {
        var completableFuture = new CompletableFuture<RandomInformation>();
        Runnable runnableTask = randomInfoTaskGenerator.infoGenerationBlockingRunnable(requestTime, completableFuture);
        executor.execute(runnableTask);
        return completableFuture;
    }

    @Override
    public CompletableFuture<RandomInformation> fetchInfoUsingFutureAsync(String requestTime) {
        var completableFuture = new CompletableFuture<RandomInformation>();
        Runnable runnableTask = randomInfoTaskGenerator.infoGenerationBlockingRunnable(requestTime, completableFuture);

        try {
            requestExecutor().execute(runnableTask);
        } catch (RejectedExecutionException rejectedExecutionException) {
            completableFuture.completeExceptionally(exceptionDueToResourceSaturation());
        } catch (Exception exception) {
            completableFuture.completeExceptionally(exception);
        }

        return completableFuture;
    }

    @Override
    public Mono<RandomInformation> fetchInfoUsingElasticScheduler(String requestTime) {
        var callableTask = randomInfoTaskGenerator.infoGenerationBlockingCallable(requestTime);
        return Mono.fromCallable(callableTask)
                .subscribeOn(Schedulers.elastic());
    }

    @Override
    public Mono<RandomInformation> fetchInfoUsingBoundedElasticScheduler(String requestTime) {
        var callableTask = randomInfoTaskGenerator.infoGenerationBlockingCallable(requestTime);
        return Mono.fromCallable(callableTask)
                .subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<RandomInformation> fetchInfoUsingExecutorServiceScheduler(String requestTime) {
        var callableTask = randomInfoTaskGenerator.infoGenerationBlockingCallable(requestTime);
        return Mono.fromCallable(callableTask)
                .subscribeOn(Schedulers.fromExecutorService(executorService));
    }

    private RuntimeException exceptionDueToResourceSaturation() {
        return new RuntimeException("Not enough resource available to handle request");
    }

    private ThreadPoolTaskExecutor requestExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(1);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}

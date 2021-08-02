package com.ahmedfahim.reactivejava101.external_mock_api.impl;

import com.ahmedfahim.reactivejava101.external_mock_api.RandomInformationEmitter;
import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

@Service
@Slf4j
public class RandomInformationEmitterImpl implements RandomInformationEmitter {
    private final DateTimeUtils dateTimeUtils;
    private final ThreadPoolExecutor threadPoolExecutor;


    public RandomInformationEmitterImpl(DateTimeUtils dateTimeUtils,
                                        ThreadPoolExecutor threadPoolExecutor) {
        this.dateTimeUtils = dateTimeUtils;
        this.threadPoolExecutor = threadPoolExecutor;
    }

    @Override
    public CompletableFuture<RandomInformation> fetchRandomInformation(String requestTime) {
        return fetchRandomInformationSafely(requestTime);
    }

    private CompletableFuture<RandomInformation> fetchRandomInformationSafely(String requestTime) {
        var completableFuture = new CompletableFuture<RandomInformation>();
        try {
            this.threadPoolExecutor.submit(randomInfoGenerationTask(requestTime, completableFuture));
        } catch (RejectedExecutionException rejectedExecutionException) {
            completableFuture.completeExceptionally(exceptionDueToResourceSaturation());
        } catch (Exception exception) {
            completableFuture.completeExceptionally(serviceUnavailableException());
        }
        return completableFuture;

    }

    private RuntimeException exceptionDueToResourceSaturation() {
        return new RuntimeException("Not enough resource available to handle request");
    }

    private ServiceUnavailableException serviceUnavailableException() {
        return new ServiceUnavailableException("Service Unavailable Due To Generic Exception");
    }

    private Runnable randomInfoGenerationTask(String requestTime,
                                              CompletableFuture<RandomInformation> completableFuture) {
        return () -> {
            try {
                //Mimicking a blocking/time-consuming operation
                Thread.sleep(5000); //5s sleep

                int randomDeterminant = Integer.parseInt(RandomStringUtils.randomNumeric(5));

                if (randomDeterminant % 2 == 0) { //if random is even, we complete properly
                    log.info("Completing Correctly");
                    completableFuture.complete(RandomInformation.builder()
                            .requestTime(requestTime)
                            .responseTime(dateTimeUtils.getCurrentTimestamp())
                            .information(RandomStringUtils.randomAlphanumeric(16))
                            .errorString(null)
                            .build());
                } else {
                    //if random is odd, we are throwing error, which will be completed exceptionally
                    throw new IllegalArgumentException("Odd Random Generation, resulted in exception");
                }
            } catch (Exception exception) {
                log.error("Completing Exceptionally");
                completableFuture.completeExceptionally(exception);
            }
        };
    }

    private CompletableFuture<RandomInformation> fetchRandomInformationTraditional(String requestTime) {
        var completableFuture = new CompletableFuture<RandomInformation>();
        requestExecutor().execute(randomInfoGenerationTask(requestTime, completableFuture));
        return completableFuture;
    }

    private ThreadPoolTaskExecutor requestExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(1);
        threadPoolTaskExecutor.setMaxPoolSize(1);
        threadPoolTaskExecutor.initialize();
        return threadPoolTaskExecutor;
    }
}

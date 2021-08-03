package com.ahmedfahim.reactivejava101.external_mock_api.impl;

import com.ahmedfahim.reactivejava101.external_mock_api.RandomInfoTaskGenerator;
import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class RandomInfoTaskGeneratorImpl implements RandomInfoTaskGenerator {
    private static final Long OPERATION_BLOCKING_DURATION = 5000L;
    private static final Integer RANDOM_INFORMATION_LENGTH = 16;

    private final DateTimeUtils dateTimeUtils;

    public RandomInfoTaskGeneratorImpl(DateTimeUtils dateTimeUtils) {
        this.dateTimeUtils = dateTimeUtils;
    }

    @Override
    public Callable<RandomInformation> infoGenerationBlockingCallable(String requestTime) {
        return () -> {
            //Mimicking a blocking/time-consuming operation
            Thread.sleep(OPERATION_BLOCKING_DURATION);
            return RandomInformation.builder()
                    .requestTime(requestTime)
                    .responseTime(dateTimeUtils.getCurrentTimestamp())
                    .information(RandomStringUtils.randomAlphanumeric(RANDOM_INFORMATION_LENGTH))
                    .build();
        };
    }

    @Override
    public Runnable infoGenerationBlockingRunnable(String requestTime,
                                                   CompletableFuture<RandomInformation> completionFuture) {
        return () -> {
            try {
                //Mimicking a blocking/time-consuming operation
                Thread.sleep(OPERATION_BLOCKING_DURATION);
                completionFuture.complete(RandomInformation.builder()
                        .requestTime(requestTime)
                        .responseTime(dateTimeUtils.getCurrentTimestamp())
                        .information(RandomStringUtils.randomAlphanumeric(RANDOM_INFORMATION_LENGTH))
                        .errorString(null)
                        .build());
            } catch (Exception exception) {
                log.error("Completing Exceptionally {}", exception.getLocalizedMessage());
                completionFuture.completeExceptionally(exception);
            }
        };
    }
}

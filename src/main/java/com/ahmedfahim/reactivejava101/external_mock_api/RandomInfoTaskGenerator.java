package com.ahmedfahim.reactivejava101.external_mock_api;

import com.ahmedfahim.reactivejava101.response.RandomInformation;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

public interface RandomInfoTaskGenerator {
    Callable<RandomInformation> infoGenerationBlockingCallable(String requestTime);

    Runnable infoGenerationBlockingRunnable(String requestTime, CompletableFuture<RandomInformation> completionFuture);
}

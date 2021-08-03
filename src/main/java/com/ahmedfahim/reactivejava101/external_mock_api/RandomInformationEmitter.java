package com.ahmedfahim.reactivejava101.external_mock_api;

import com.ahmedfahim.reactivejava101.response.RandomInformation;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface RandomInformationEmitter {
    CompletableFuture<RandomInformation> fetchInfoUsingFutureBlocking(String requestTime);

    CompletableFuture<RandomInformation> fetchInfoUsingFutureAsync(String requestTime);

    Mono<RandomInformation> fetchInfoUsingElasticScheduler(String requestTime);

    Mono<RandomInformation> fetchInfoUsingBoundedElasticScheduler(String requestTime);

    Mono<RandomInformation> fetchInfoUsingExecutorServiceScheduler(String requestTime);
}

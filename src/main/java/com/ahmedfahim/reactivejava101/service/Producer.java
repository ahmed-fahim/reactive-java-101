package com.ahmedfahim.reactivejava101.service;

import com.ahmedfahim.reactivejava101.response.RandomInformation;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public interface Producer {
    CompletableFuture<RandomInformation> getInfoUsingFutureBlocking(String requestTime);

    CompletableFuture<RandomInformation> getInfoUsingFuture(String requestTime);

    Mono<RandomInformation> getInfoUsingElasticScheduler(String requestTime);

    Mono<RandomInformation> getInfoUsingBoundedElasticScheduler(String requestTime);

    Mono<RandomInformation> getInfoUsingManagedScheduler(String requestTime);
}

package com.ahmedfahim.reactivejava101.service.impl;

import com.ahmedfahim.reactivejava101.external_mock_api.RandomInformationEmitter;
import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.service.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProducerImpl implements Producer {
    private final RandomInformationEmitter randomInformationEmitter;

    public ProducerImpl(RandomInformationEmitter randomInformationEmitter) {
        this.randomInformationEmitter = randomInformationEmitter;
    }

    @Override
    public CompletableFuture<RandomInformation> getInfoUsingFutureBlocking(String requestTime) {
        commonLog(requestTime);
        return randomInformationEmitter.fetchInfoUsingFutureBlocking(requestTime);
    }

    @Override
    public CompletableFuture<RandomInformation> getInfoUsingFuture(String requestTime) {
        commonLog(requestTime);
        return randomInformationEmitter.fetchInfoUsingFutureAsync(requestTime);
    }

    @Override
    public Mono<RandomInformation> getInfoUsingElasticScheduler(String requestTime) {
        commonLog(requestTime);
        return randomInformationEmitter.fetchInfoUsingElasticScheduler(requestTime);
    }

    @Override
    public Mono<RandomInformation> getInfoUsingBoundedElasticScheduler(String requestTime) {
        commonLog(requestTime);
        return randomInformationEmitter.fetchInfoUsingBoundedElasticScheduler(requestTime);
    }

    @Override
    public Mono<RandomInformation> getInfoUsingManagedScheduler(String requestTime) {
        commonLog(requestTime);
        return randomInformationEmitter.fetchInfoUsingExecutorServiceScheduler(requestTime);
    }

    private void commonLog(String requestTime) {
        log.info("Producer fetching for requestTime = {}", requestTime);
    }
}

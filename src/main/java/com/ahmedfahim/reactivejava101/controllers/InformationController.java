package com.ahmedfahim.reactivejava101.controllers;

import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.service.Producer;
import com.ahmedfahim.reactivejava101.utils.ResponseUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class InformationController {

    private final Producer producer;

    public InformationController(Producer producer) {
        this.producer = producer;
    }

    @PostMapping("/info-blocking-future")
    public Mono<ResponseEntity<RandomInformation>> infoBlockingFuture(@RequestBody String requestTime) {
        return Mono.defer(
                () -> {
                    try {
                        RandomInformation information = producer.getInfoUsingFutureBlocking(requestTime).join();
                        return Mono.just(new ResponseEntity<>(information, HttpStatus.OK));
                    } catch (Exception exception) {
                        return ResponseUtils.handleError(exception, requestTime)
                                .map(ResponseUtils::mapToResponseEntity);
                    }
                }
        );
    }

    @PostMapping("/info-completable-future")
    public Mono<ResponseEntity<RandomInformation>> infoCompletableFuture(@RequestBody String requestTime) {
        return Mono.fromFuture(producer.getInfoUsingFuture(requestTime))
                .onErrorResume(throwable -> ResponseUtils.handleError(throwable, requestTime))
                .map(ResponseUtils::mapToResponseEntity);
    }

    @PostMapping("/info-scheduler-elastic")
    public Mono<ResponseEntity<RandomInformation>> infoSchedulerElastic(@RequestBody String requestTime) {
        return producer.getInfoUsingElasticScheduler(requestTime)
                .onErrorResume(throwable -> ResponseUtils.handleError(throwable, requestTime))
                .map(ResponseUtils::mapToResponseEntity);
    }

    @PostMapping("/info-scheduler-bounded-elastic")
    public Mono<ResponseEntity<RandomInformation>> infoSchedulerBoundedElastic(@RequestBody String requestTime) {
        return producer.getInfoUsingBoundedElasticScheduler(requestTime)
                .onErrorResume(throwable -> ResponseUtils.handleError(throwable, requestTime))
                .map(ResponseUtils::mapToResponseEntity);
    }

    @PostMapping("/info-scheduler-executor-service")
    public Mono<ResponseEntity<RandomInformation>> infoSchedulerExecutorService(@RequestBody String requestTime) {
        return producer.getInfoUsingManagedScheduler(requestTime)
                .onErrorResume(throwable -> ResponseUtils.handleError(throwable, requestTime))
                .map(ResponseUtils::mapToResponseEntity);
    }
}

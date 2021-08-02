package com.ahmedfahim.reactivejava101.controllers;


import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.service.Producer;
import com.ahmedfahim.reactivejava101.utils.DateTimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@CrossOrigin("*")
@RestController
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class InformationController {

    private final Producer producer;
    private final DateTimeUtils dateTimeUtils;

    public InformationController(Producer producer,
                                 DateTimeUtils dateTimeUtils) {
        this.producer = producer;
        this.dateTimeUtils = dateTimeUtils;
    }

    @PostMapping("/fetch-new-information")
    public Mono<ResponseEntity<RandomInformation>> fetchNewInformation(@RequestBody String requestTime) {
        log.info("requestTime = {}", requestTime);
        return Mono.fromFuture(producer.getRandomInformation(requestTime))
                .onErrorResume(throwable -> this.handleError(throwable, requestTime))
                .map(this::mapToResponseEntity);
    }

    private Mono<RandomInformation> handleError(Throwable throwable, String requestTime) {
        log.error("handlingError = {}", throwable.getLocalizedMessage());
        var randomInfoWithError = RandomInformation.builder()
                .errorString(throwable.getLocalizedMessage())
                .requestTime(requestTime)
                .responseTime(dateTimeUtils.getCurrentTimestamp())
                .build();
        return Mono.justOrEmpty(randomInfoWithError);
    }

    private ResponseEntity<RandomInformation> mapToResponseEntity(RandomInformation randomInformation) {
        log.info("mapping {}", randomInformation);
        HttpStatus httpStatus;
        if(Objects.isNull(randomInformation.getErrorString())
                && Objects.nonNull(randomInformation.getInformation()) ) {
            httpStatus = HttpStatus.OK;
        }
        else {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return new ResponseEntity<>(randomInformation, httpStatus);
    }
}

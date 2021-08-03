package com.ahmedfahim.reactivejava101.utils;

import com.ahmedfahim.reactivejava101.response.RandomInformation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
public class ResponseUtils {
    private static final DateTimeUtils dateTimeUtils = new DateTimeUtils();

    private ResponseUtils() {
    }

    public static Mono<RandomInformation> handleError(Throwable throwable, String requestTime) {
        log.error("[Err] handlingError = {}", throwable.getLocalizedMessage());
        var randomInfoWithError = RandomInformation.builder()
                .errorString(throwable.getLocalizedMessage())
                .requestTime(requestTime)
                .responseTime(dateTimeUtils.getCurrentTimestamp())
                .build();
        return Mono.justOrEmpty(randomInfoWithError);
    }

    public static ResponseEntity<RandomInformation> mapToResponseEntity(RandomInformation randomInformation) {
        log.info("mapping {}", randomInformation);
        HttpStatus httpStatus;
        if (Objects.isNull(randomInformation.getErrorString())
                && Objects.nonNull(randomInformation.getInformation())) {
            httpStatus = HttpStatus.OK;
        } else {
            httpStatus = HttpStatus.SERVICE_UNAVAILABLE;
        }
        return new ResponseEntity<>(randomInformation, httpStatus);
    }
}

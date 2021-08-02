package com.ahmedfahim.reactivejava101.service.impl;

import com.ahmedfahim.reactivejava101.external_mock_api.RandomInformationEmitter;
import com.ahmedfahim.reactivejava101.response.RandomInformation;
import com.ahmedfahim.reactivejava101.service.Producer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class ProducerImpl implements Producer {
    private final RandomInformationEmitter randomInformationEmitter;

    public ProducerImpl(RandomInformationEmitter randomInformationEmitter) {
        this.randomInformationEmitter = randomInformationEmitter;
    }

    @Override
    public CompletableFuture<RandomInformation> getRandomInformation(String requestTime) {
        return randomInformationEmitter.fetchRandomInformation(requestTime);
    }
}

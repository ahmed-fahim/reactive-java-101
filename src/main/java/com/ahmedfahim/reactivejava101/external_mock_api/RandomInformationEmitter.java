package com.ahmedfahim.reactivejava101.external_mock_api;

import com.ahmedfahim.reactivejava101.response.RandomInformation;

import java.util.concurrent.CompletableFuture;

public interface RandomInformationEmitter {
    CompletableFuture<RandomInformation> fetchRandomInformation(String requestTime);
}

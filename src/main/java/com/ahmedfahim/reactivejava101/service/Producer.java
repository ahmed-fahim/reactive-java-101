package com.ahmedfahim.reactivejava101.service;

import com.ahmedfahim.reactivejava101.response.RandomInformation;

import java.util.concurrent.CompletableFuture;

public interface Producer {
    CompletableFuture<RandomInformation> getRandomInformation(String requestTime);
}

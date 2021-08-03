package com.ahmedfahim.reactivejava101.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RandomInformation {
    private String information;
    private String requestTime;
    private String responseTime;
    private String errorString;
}

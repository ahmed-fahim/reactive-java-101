package com.ahmedfahim.reactivejava101.response;

import lombok.*;

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

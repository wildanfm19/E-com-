package com.wildan.e_commerce.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseMessage {
    private int statusCode;
    private String message;
    private LocalDateTime timeStamp;
}

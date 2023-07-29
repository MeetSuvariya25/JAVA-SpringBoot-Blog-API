package com.example.BlogAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class APIResponse {
    private Integer statusCode;
    private Long timeStamp;
    private String message;
    private String path;
}

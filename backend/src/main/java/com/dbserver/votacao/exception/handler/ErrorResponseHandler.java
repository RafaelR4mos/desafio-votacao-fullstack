package com.dbserver.votacao.exception.handler;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;
import java.util.Map;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
public class ErrorResponseHandler {

    private int statusCode;
    private String error;
    private String message;
    private long timestamp;
    private Map<String, List<String>> fieldErrors;
}

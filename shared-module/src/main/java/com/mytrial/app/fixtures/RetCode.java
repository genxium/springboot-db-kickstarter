package com.mytrial.app.fixtures;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component("retCode")
public class RetCode {
    private int OK = 0;
    private int UNKNOWN_ERROR = 1;
    private int DUAL_AUTHORITY_REQUIRED = 2;
}

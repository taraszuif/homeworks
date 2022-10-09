package me.zuif.hw18.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Request {
    private String ip;
    private String userAgent;
    private LocalDateTime createdTime;
}

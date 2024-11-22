package com.victordev.shortUrl;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UrlData {

    private String originalUrl;
    private Long expirationTime;
}

package com.victordev.shortUrl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UrlData {

    private String originalUrl;
    private Long expirationTime;
}

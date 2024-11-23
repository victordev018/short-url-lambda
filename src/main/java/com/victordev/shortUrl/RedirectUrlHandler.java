package com.victordev.shortUrl;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class RedirectUrlHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    private final S3Client s3Client = S3Client.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

        String pathParameters = (String) input.get("rawPath");
        String shortUrlCode = pathParameters.replace("/", "");

        if(shortUrlCode == null || shortUrlCode.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required.");
        }

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket("jv-url-shortener-storage")
                .key(shortUrlCode+".json")
                .build();

        InputStream s3ObjectStream;

        try {
            s3ObjectStream = s3Client.getObject(request);
        }
        catch (Exception error) {
            throw new RuntimeException("Error fetching URL data from S3: " + error.getMessage(), error);
        }

        UrlData urlData;

        try {
            urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
        }
        catch (Exception error) {
            throw new RuntimeException("Error deserializing URL data: " + error.getMessage(), error);
        }

        Long currentTimeInSeconds = System.currentTimeMillis() / 1000;  // convert to seconds
        Map<String, Object> response = new HashMap<>();

        // case where URL has expired
        if(urlData.getExpirationTime() < currentTimeInSeconds) {
            response.put("statusCode", 410);
            response.put("body", "This URL has expired");

            return response;
        }

        response.put("Location", urlData.getOriginalUrl());     // url to be redirected
        response.put("statusCode", 302);                        // status code of the redirect
        Map<String, String> headers = new HashMap<>();
        headers.put("Location", urlData.getOriginalUrl());
        response.put("headers", headers);

        return response;
    }
}

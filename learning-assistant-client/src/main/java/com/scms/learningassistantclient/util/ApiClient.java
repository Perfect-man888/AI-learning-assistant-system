package com.scms.learningassistantclient.util;

import com.scms.learningassistantclient.config.AppConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    /**
     * 自动拼接完整 URL
     * 支持：
     * 1. "/api/login"
     * 2. "api/login"
     * 3. "https://xxx.cpolar.top/api/login"
     */
    private static URI buildUri(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("请求地址不能为空");
        }

        String finalUrl = url.trim();

        // 如果已经是完整网址，直接使用
        if (finalUrl.startsWith("http://") || finalUrl.startsWith("https://")) {
            return URI.create(finalUrl);
        }

        // 如果不是完整网址，就自动拼接 BASE_URL
        if (!finalUrl.startsWith("/")) {
            finalUrl = "/" + finalUrl;
        }

        return URI.create(AppConfig.BASE_URL + finalUrl);
    }

    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(url))
                .GET()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=utf-8")
                .build();

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }

        throw new RuntimeException(
                "GET 请求失败，状态码：" + response.statusCode()
                        + "，返回内容：" + response.body()
        );
    }

    public static String post(String url, String jsonBody) throws IOException, InterruptedException {
        if (jsonBody == null || jsonBody.trim().isEmpty()) {
            jsonBody = "{}";
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(url))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=utf-8")
                .build();

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }

        throw new RuntimeException(
                "POST 请求失败，状态码：" + response.statusCode()
                        + "，返回内容：" + response.body()
        );
    }

    public static String delete(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(buildUri(url))
                .DELETE()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=utf-8")
                .build();

        HttpResponse<String> response = CLIENT.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        }

        throw new RuntimeException(
                "DELETE 请求失败，状态码：" + response.statusCode()
                        + "，返回内容：" + response.body()
        );
    }
}
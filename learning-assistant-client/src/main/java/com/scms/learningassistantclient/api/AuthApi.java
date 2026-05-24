package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.ApiResponse;
import com.scms.learningassistantclient.model.LoginRequest;
import com.scms.learningassistantclient.model.LoginUser;
import com.scms.learningassistantclient.model.RegisterRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class AuthApi {

    // 统一认证接口前缀
    private static final String AUTH_BASE_URL = AppConfig.BASE_URL + "/api/auth";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LoginUser login(LoginRequest loginRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(loginRequest);

        String loginUrl = AUTH_BASE_URL + "/login";

        System.out.println("登录请求地址：" + loginUrl);
        System.out.println("登录请求参数：" + requestJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(loginUrl))
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        System.out.println("登录响应状态码：" + response.statusCode());
        System.out.println("登录响应内容：" + response.body());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "登录请求失败，状态码：" + response.statusCode()
                            + "，返回内容：" + response.body()
            );
        }

        ApiResponse<LoginUser> apiResponse = objectMapper.readValue(
                response.body(),
                new TypeReference<ApiResponse<LoginUser>>() {
                }
        );

        if (apiResponse.getCode() == null || apiResponse.getCode() != 200) {
            throw new RuntimeException(apiResponse.getMessage());
        }

        return apiResponse.getData();
    }

    public LoginUser register(RegisterRequest registerRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(registerRequest);

        String registerUrl = AUTH_BASE_URL + "/register";

        System.out.println("注册请求地址：" + registerUrl);
        System.out.println("注册请求参数：" + requestJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(registerUrl))
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        System.out.println("注册响应状态码：" + response.statusCode());
        System.out.println("注册响应内容：" + response.body());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    "注册请求失败，状态码：" + response.statusCode()
                            + "，返回内容：" + response.body()
            );
        }

        ApiResponse<LoginUser> apiResponse = objectMapper.readValue(
                response.body(),
                new TypeReference<ApiResponse<LoginUser>>() {
                }
        );

        if (apiResponse.getCode() == null || apiResponse.getCode() != 200) {
            throw new RuntimeException(apiResponse.getMessage());
        }

        return apiResponse.getData();
    }
}
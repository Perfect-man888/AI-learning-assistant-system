package com.scms.learningassistantclient.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scms.learningassistantclient.config.AppConfig;
import com.scms.learningassistantclient.model.ApiResponse;
import com.scms.learningassistantclient.model.Course;
import com.scms.learningassistantclient.model.CreateCourseRequest;
import com.scms.learningassistantclient.model.JoinCourseRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CourseApi {

    private static final String COURSE_BASE_URL = AppConfig.BASE_URL + "/api/courses";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Course createCourse(CreateCourseRequest createCourseRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(createCourseRequest);

        String url = COURSE_BASE_URL + "/create";
        System.out.println("创建课程请求地址：" + url);
        System.out.println("创建课程请求参数：" + requestJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        return sendAndParse(
                request,
                new TypeReference<ApiResponse<Course>>() {
                },
                "创建课程"
        );
    }

    public List<Course> listAllCourses() throws Exception {
        String url = COURSE_BASE_URL + "/list";
        System.out.println("全部课程请求地址：" + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return sendAndParse(
                request,
                new TypeReference<ApiResponse<List<Course>>>() {
                },
                "全部课程加载"
        );
    }

    public List<Course> listTeacherCourses(Long teacherId) throws Exception {
        String url = COURSE_BASE_URL + "/teacher/" + teacherId;
        System.out.println("教师课程请求地址：" + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return sendAndParse(
                request,
                new TypeReference<ApiResponse<List<Course>>>() {
                },
                "教师课程加载"
        );
    }

    public void joinCourse(JoinCourseRequest joinCourseRequest) throws Exception {
        String requestJson = objectMapper.writeValueAsString(joinCourseRequest);

        String url = COURSE_BASE_URL + "/join";
        System.out.println("加入课程请求地址：" + url);
        System.out.println("加入课程请求参数：" + requestJson);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestJson, StandardCharsets.UTF_8))
                .build();

        sendAndParse(
                request,
                new TypeReference<ApiResponse<Void>>() {
                },
                "加入课程"
        );
    }

    public List<Course> listMyCourses(Long studentId) throws Exception {
        String url = COURSE_BASE_URL + "/my/" + studentId;
        System.out.println("我的课程请求地址：" + url);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();

        return sendAndParse(
                request,
                new TypeReference<ApiResponse<List<Course>>>() {
                },
                "我的课程加载"
        );
    }

    private <T> T sendAndParse(
            HttpRequest request,
            TypeReference<ApiResponse<T>> typeReference,
            String actionName
    ) throws Exception {

        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)
        );

        System.out.println(actionName + "响应状态码：" + response.statusCode());
        System.out.println(actionName + "响应内容：" + response.body());

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new RuntimeException(
                    actionName + "失败，状态码：" + response.statusCode()
                            + "，返回内容：" + response.body()
            );
        }

        ApiResponse<T> apiResponse = objectMapper.readValue(response.body(), typeReference);
        checkResponse(apiResponse);

        return apiResponse.getData();
    }

    private void checkResponse(ApiResponse<?> apiResponse) {
        if (apiResponse == null) {
            throw new RuntimeException("后端无响应");
        }

        if (apiResponse.getCode() == null || apiResponse.getCode() != 200) {
            throw new RuntimeException(
                    apiResponse.getMessage() == null ? "请求失败" : apiResponse.getMessage()
            );
        }
    }
}
package com.scms.learningassistantclient.config;
import com.scms.learningassistantclient.config.AppConfig;
public class AppConfig {

    public static final String BASE_URL = "http://localhost:8080";

    public static final String COURSE_LIST_URL = BASE_URL + "/api/courses";
    public static final String COURSE_CREATE_URL = BASE_URL + "/api/courses";

    public static String getChapterListUrl(Long courseId) {
        return BASE_URL + "/api/courses/" + courseId + "/chapters";
    }

    public static String getChapterCreateUrl(Long courseId) {
        return BASE_URL + "/api/courses/" + courseId + "/chapters";
    }

    public static String getTaskQuestionListUrl(Long taskId) {
        return BASE_URL + "/api/task-questions/task/" + taskId;
    }

    public static String getSectionListUrl(Long chapterId) {
        return BASE_URL + "/api/chapters/" + chapterId + "/sections";
    }

    public static String getSectionCreateUrl(Long chapterId) {
        return BASE_URL + "/api/chapters/" + chapterId + "/sections";
    }

    public static String getStudyProgressSectionUrl() {
        return BASE_URL + "/api/study-progress/section";
    }

    public static String getStudentCourseProgressUrl(Long studentId, Long courseId) {
        return BASE_URL + "/api/study-progress/student/" + studentId + "/course/" + courseId;
    }

    public static String getCourseProgressSummaryUrl(Long courseId) {
        return BASE_URL + "/api/study-progress/course/" + courseId + "/summary";
    }

    public static String getQuestionListUrl(Long courseId) {
        return BASE_URL + "/api/questions/course/" + courseId;
    }

    public static String getCreateQuestionUrl() {
        return BASE_URL + "/api/questions";
    }

    public static String getTaskListUrl(Long courseId) {
        return BASE_URL + "/api/tasks/course/" + courseId;
    }

    public static String getCreateTaskUrl() {
        return BASE_URL + "/api/tasks";
    }

    public static String getSubmitAnswerUrl() {
        return BASE_URL + "/api/answers/submit";
    }

    public static String getTaskAnswerRecordsUrl(Long taskId) {
        return BASE_URL + "/api/answers/task/" + taskId;
    }

    public static String getStudentAnswerRecordsUrl(Long studentId) {
        return BASE_URL + "/api/answers/student/" + studentId;
    }

    public static String getTaskAnswerRecordDetailsUrl(Long taskId) {
        return BASE_URL + "/api/answers/task/" + taskId + "/detail";
    }

    public static String getStudentWrongAnswerDetailsUrl(Long studentId) {
        return BASE_URL + "/api/answers/student/" + studentId + "/wrong";
    }

    public static String getStudentAnswerDetailsByCourseUrl(Long studentId, Long courseId) {
        return BASE_URL + "/api/answers/student/" + studentId + "/course/" + courseId + "/detail";
    }

    public static String getStudentWrongAnswerDetailsByCourseUrl(Long studentId, Long courseId) {
        return BASE_URL + "/api/answers/student/" + studentId + "/course/" + courseId + "/wrong";
    }

    public static String getClassLearningReportUrl(Long courseId) {
        return BASE_URL + "/api/class-report/course/" + courseId;
    }

    public static String getDeleteTaskUrl(Long taskId) {
        return BASE_URL + "/api/tasks/" + taskId;
    }

    public static String getAiGenerateQuestionsUrl() {
        return BASE_URL + "/api/ai/questions/generate";
    }

    private AppConfig() {
    }
}
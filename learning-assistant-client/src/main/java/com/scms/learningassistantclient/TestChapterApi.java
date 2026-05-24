package com.scms.learningassistantclient;

import com.scms.learningassistantclient.api.ChapterApi;
import com.scms.learningassistantclient.model.CourseChapter;

import java.util.List;

public class TestChapterApi {

    public static void main(String[] args) {
        ChapterApi chapterApi = new ChapterApi();

        Long courseId = 2L;

        List<CourseChapter> chapters = chapterApi.getChaptersByCourseId(courseId);

        System.out.println("章节数量：" + chapters.size());

        for (CourseChapter chapter : chapters) {
            System.out.println(
                    chapter.getId() + " - "
                            + chapter.getChapterTitle()
                            + " - 排序：" + chapter.getSortOrder()
            );
        }
    }
}
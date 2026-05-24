package com.scms.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.Chapter;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChapterMapper extends BaseMapper<Chapter> {
}
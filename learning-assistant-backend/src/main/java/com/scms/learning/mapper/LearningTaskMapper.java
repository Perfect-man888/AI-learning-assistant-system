package com.scms.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.LearningTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LearningTaskMapper extends BaseMapper<LearningTask> {
}
package com.scms.learning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.KnowledgePoint;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KnowledgePointMapper extends BaseMapper<KnowledgePoint> {
}
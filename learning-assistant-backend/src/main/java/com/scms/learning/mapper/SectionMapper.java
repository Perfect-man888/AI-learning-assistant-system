package com.scms.learning.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.scms.learning.entity.Section;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SectionMapper extends BaseMapper<Section> {
}
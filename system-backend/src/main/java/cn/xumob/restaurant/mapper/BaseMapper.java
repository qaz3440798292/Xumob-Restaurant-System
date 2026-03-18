package cn.xumob.restaurant.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 基础 Mapper 接口
 */
@Mapper
public interface BaseMapper<T> {
    
    T selectById(Long id);
    
    List<T> selectList();
    
    int insert(T entity);
    
    int updateById(T entity);
    
    int deleteById(Long id);
}

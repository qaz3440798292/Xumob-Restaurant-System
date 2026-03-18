package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.Position;
import org.apache.ibatis.annotations.Mapper;

/**
 * 职位 Mapper
 */
@Mapper
public interface PositionMapper {

    /**
     * 根据ID查询职位
     */
    Position selectById(Long id);

    /**
     * 根据职位代码查询职位
     */
    Position selectByCode(String code);
}

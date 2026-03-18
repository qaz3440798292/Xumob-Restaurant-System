package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.Rider;
import org.apache.ibatis.annotations.Mapper;

/**
 * 骑手 Mapper
 */
@Mapper
public interface RiderMapper {

    /**
     * 根据骑手ID查询骑手信息
     */
    Rider selectByRiderId(Long id);

    /**
     * 根据用户名查询骑手信息
     */
    Rider selectByUsername(String username);

    /**
     * 根据用户ID查询骑手信息
     */
    Rider selectByUserId(Long userId);

    /**
     * 创建骑手
     */
    int createRider(Rider rider);

    /**
     * 查询所有骑手
     */
    java.util.List<Rider> selectAllRiders();
}

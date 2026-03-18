package cn.xumob.restaurant.mapper;

import cn.xumob.restaurant.entity.Rider;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 骑手 Mapper 接口
 */
@Mapper
public interface RiderMapper {

    /**
     * 根据骑手ID查询骑手信息
     *
     * @param id 骑手ID
     * @return 骑手信息
     */
    Rider selectByRiderId(Long id);

    /**
     * 根据用户ID查询骑手信息
     *
     * @param userId 用户ID
     * @return 骑手信息
     */
    Rider selectByRiderUserId(Long userId);

    /**
     * 创建骑手
     *
     * @param rider 骑手信息
     * @return 影响的行数
     */
    int createRider(Rider rider);

    /**
     * 查询所有骑手
     *
     * @return 骑手列表
     */
    List<Rider> selectAllRiders();

    /**
     * 根据用户名查询骑手
     *
     * @param username 用户名
     * @return 骑手信息
     */
    Rider selectRiderByUsername(String username);
}

package cn.xumob.restaurant.service;

import cn.xumob.restaurant.entity.Rider;

import java.util.List;

/**
 * 骑手服务接口
 */
public interface RiderService {

    /**
     * 根据骑手ID查询骑手信息
     *
     * @param id 骑手ID
     * @return 骑手信息
     */
    Rider getRiderById(Long id);

    /**
     * 创建骑手
     *
     * @param rider 骑手信息
     * @return 是否创建成功
     */
    boolean createRider(Rider rider);

    /**
     * 查询所有骑手
     *
     * @return 骑手列表
     */
    List<Rider> getAllRiders();

    /**
     * 根据用户名查询骑手
     *
     * @param username 用户名
     * @return 骑手信息
     */
    Rider getRiderByUsername(String username);
}

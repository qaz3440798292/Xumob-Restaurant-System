package cn.xumob.restaurant.service.impl;

import cn.xumob.restaurant.entity.Rider;
import cn.xumob.restaurant.mapper.RiderMapper;
import cn.xumob.restaurant.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 骑手服务实现类
 */
@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final RiderMapper riderMapper;

    @Override
    public Rider getRiderById(Long id) {
        return riderMapper.selectByRiderId(id);
    }

    @Override
    public Rider getRiderByUserId(Long userId) {
        return riderMapper.selectByUserId(userId);
    }

    @Override
    @Transactional
    public boolean createRider(Rider rider) {
        int result = riderMapper.createRider(rider);
        return result > 0;
    }

    @Override
    public List<Rider> getAllRiders() {
        return riderMapper.selectAllRiders();
    }

    @Override
    public Rider getRiderByUsername(String username) {
        return riderMapper.selectByUsername(username);
    }
}

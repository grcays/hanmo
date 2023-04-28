package com.example.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.Follow;
import com.example.mapper.FollowMapper;
import com.example.service.IFollowService;
import com.example.service.IUserService;
import com.example.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.utils.RedisConstants.FOLLOW_USER_ID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements IFollowService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Override
    public Result followSomebody(Long followUserId, Boolean isFollow) {
        UserDTO user = UserHolder.getUser();
        if (isFollow) {
            Follow follow = new Follow();
            follow.setUserId(user.getId());
            follow.setFollowUserId(followUserId);
            save(follow);
        }else {
//            remove(new QueryWrapper<Follow>().eq("user_id", user.getId()).eq("follow_user_id", followUserId));
            LambdaQueryWrapper<Follow> lqw = new LambdaQueryWrapper<>();
            lqw.eq(Follow::getUserId,user.getId()).eq(Follow::getFollowUserId,followUserId);
            remove(lqw);
        }
        return Result.ok();
    }

    @Override
    public Result isFollow(Long followUserId) {
        UserDTO user = UserHolder.getUser();
        Long userId = user.getId();
        LambdaQueryWrapper<Follow> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Follow::getUserId,user.getId()).eq(Follow::getFollowUserId,followUserId);
        Follow one = getOne(lqw);
//        Integer count = query().eq("user_id", userId).eq("follow_user_id", followUserId).count();
//        return Result.ok(count>0);
        return Result.ok(one != null);
    }

//    @Override
//    public Result followCommon(Long id) {
//        return null;
//    }

    @Override
    public Result followCommon(Long id) {
        Long userId = UserHolder.getUser().getId();
        String key1 = FOLLOW_USER_ID + userId;  //当前登录用户的关注列表集合
        String key2 = FOLLOW_USER_ID + id;  //点击查看的用户的关注列表集合
        //求交集
        Set<String> intersect = stringRedisTemplate.opsForSet().intersect(key1, key2);
        if (intersect == null || intersect.isEmpty()){
            //无交集
            return Result.ok(Collections.emptyList());
        }
        //解析id集合
        List<Long> ids = intersect.stream().map(Long::valueOf).collect(Collectors.toList());
        //批量查询用户并转换为userDTO对象
        List<UserDTO> userDTOList = userService.listByIds(ids).stream().map(user ->
                        BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());
        return Result.ok(userDTOList);
    }
}

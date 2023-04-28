package com.example.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.Blog;
import com.example.entity.User;
import com.example.mapper.BlogMapper;
import com.example.service.IBlogService;
import com.example.service.IUserService;
import com.example.utils.RedisConstants;
import com.example.utils.SystemConstants;
import com.example.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper,Blog> implements IBlogService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Override
    public Result likeBlog(Long id) {
        Long userId = UserHolder.getUser().getId();
        //判断用户是否已经点赞
        String key = "blog:liked:" + id;
        Boolean member = stringRedisTemplate.opsForSet().isMember(key, userId.toString());
        // LambdaUpdateWrapper<Blog> luw = new LambdaUpdateWrapper<>();
        if (BooleanUtil.isFalse(member)) {
            //是false说明不是成员,可以添加
            // luw.eq(Blog::getId,id).setSql("liked = liked + 1");
            boolean update = update().setSql("liked = liked + 1").update();
            if (update) {
                stringRedisTemplate.opsForSet().add(key,userId.toString());
            }
        }else {
            boolean update = update().setSql("liked = liked - 1").update();
            stringRedisTemplate.opsForSet().remove(key,userId.toString());
        }
        return Result.ok();
    }

    @Override
    public Result queryBlogLikes(String id) {
        String key = RedisConstants.BLOG_LIKED_KEY + id;
        // 查询 top5 的点赞用户
        Set<String> top5 = stringRedisTemplate.opsForZSet().range(key, 0, 4);
        if(top5 == null){
            return Result.ok(Collections.emptyList());
        }
        // 解析出其中的用户id
        List<Long> ids = top5.stream().map(Long::valueOf).collect(Collectors.toList());
        String join = StrUtil.join(",", ids);
        // 根据用户id查询用户
        List<UserDTO> userDTOS = userService.query().in("id", ids).last("order by filed(id, "+join+")").list()
                .stream()
                .map(user -> BeanUtil.copyProperties(user, UserDTO.class))
                .collect(Collectors.toList());

        return Result.ok(userDTOS);
    }


    private void queryBlogUser(Blog blog) {
        Long userId = blog.getUserId();
        User user = userService.getById(userId);
        blog.setName(user.getNickName());
        blog.setIcon(user.getIcon());
    }

    @Override
    public Result queryBlogById(String id) {
        Blog blog = getById(id);

        if(blog == null){
            return Result.fail("笔记不存在！");
        }

        queryBlogUser(blog);
        // 查询 Blog 是否被点赞
        isBlogLiked(blog);

        return Result.ok(blog);
    }

    @Override
    public Result queryHotBlog(Integer current) {
        // 根据用户查询
        Page<Blog> HotBlogPage = new Page<>(current, SystemConstants.MAX_PAGE_SIZE);

        LambdaQueryWrapper<Blog> lqw = new LambdaQueryWrapper<>();
        lqw.orderByDesc(Blog::getLiked);
//        Page<Blog> page = query()
//                .orderByDesc("liked")
//                .page(new Page<>(current, SystemConstants.MAX_PAGE_SIZE));
//        // 获取当前页数据
        //List<Blog> records = page.getRecords();
        // 查询用户
//        records.forEach(blog -> {
//            this.queryBlogUser(blog);
//            this.isBlogLiked(blog);
//        });
        return Result.ok();
    }
    private void isBlogLiked(Blog blog) {
        UserDTO user = UserHolder.getUser();
        if(user == null){
            return;
        }
        Long userId = user.getId();
        String key = RedisConstants.BLOG_LIKED_KEY + blog.getId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        blog.setIsLike(score != null);
    }
}

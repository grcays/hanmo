package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.Result;
import com.example.entity.WordPost;
import com.example.mapper.WordPostMapper;
import com.example.service.IWordPostService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WordPostServiceImpl extends ServiceImpl<WordPostMapper, WordPost> implements IWordPostService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result listWordPost() {
       // stringRedisTemplate.opsForValue().set(LIST_KEY + );
        LambdaQueryWrapper<WordPost> lqw = new LambdaQueryWrapper<>();
        lqw.orderByAsc(WordPost::getSort);
        List<WordPost> list = this.list(lqw);
        return Result.ok(list);
    }
}

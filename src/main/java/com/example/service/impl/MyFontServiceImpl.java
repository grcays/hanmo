package com.example.service.impl;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.Result;
import com.example.dto.UserDTO;
import com.example.entity.MyFont;
import com.example.mapper.MyFontMapper;
import com.example.service.IMyFontService;
import com.example.utils.UserHolder;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class MyFontServiceImpl extends ServiceImpl<MyFontMapper, MyFont> implements IMyFontService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;



    @Override
    public Result addFont(Long fontId) {
        UserDTO user = UserHolder.getUser();
        Long id = user.getId();
        String key = "myFont:add:" + fontId;
        Boolean member = stringRedisTemplate.opsForSet().isMember(key, id.toString());
        if (BooleanUtil.isFalse(member)) {
            //我的字帖里面为空,在数据库中添加此字帖
            MyFont myFont = new MyFont();
            myFont.setFontId(fontId);
            myFont.setUserId(id);
            myFont.setCreateTime(LocalDateTime.now());
            myFont.setUpdateTime(LocalDateTime.now());
            save(myFont);
            stringRedisTemplate.opsForSet().add(key,id.toString());
        }else {
            return Result.fail("已添加,请勿重复添加");
        }
        return Result.ok();
    }

    @Override
    public Result deleteFont(Long fontId) {
        Long id = UserHolder.getUser().getId();
        LambdaQueryWrapper<MyFont> lqw = new LambdaQueryWrapper<>();
        lqw.eq(MyFont::getFontId,fontId).eq(MyFont::getUserId,id);
        remove(lqw);
        return Result.ok();
    }
}

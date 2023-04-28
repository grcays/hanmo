package com.example.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.Result;
import com.example.entity.Font;
import com.example.mapper.FontMapper;
import com.example.service.IFontService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

import static com.example.utils.RedisConstants.FONT_TYPE_KEY;

@Service
public class FontServiceImpl extends ServiceImpl<FontMapper, Font> implements IFontService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result fontList() {
        String font = stringRedisTemplate.opsForValue().get(FONT_TYPE_KEY);
        if(!StrUtil.isBlank(font)){
            List<Font> list = JSONObject.parseArray(font,Font.class);
            return Result.ok(list);
        }
        LambdaQueryWrapper<Font> lqw = new LambdaQueryWrapper<>();
        //lqw.eq(Font::getWordpostId,wordPostId);
        lqw.orderByAsc(Font::getUpdateTime);
        List<Font> list = this.list(lqw);
        String json = JSONUtil.toJsonStr(list);
        stringRedisTemplate.opsForValue().set(FONT_TYPE_KEY ,json);
        return Result.ok(list);
    }
}

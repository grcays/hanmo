package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.Result;
import com.example.entity.WordPost;

public interface IWordPostService extends IService<WordPost> {
    Result listWordPost();
}

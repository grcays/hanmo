package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.Result;
import com.example.entity.MyFont;


public interface IMyFontService extends IService<MyFont> {
    Result addFont(Long fontId);

    Result deleteFont(Long fontId);

}

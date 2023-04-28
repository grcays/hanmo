package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.Result;
import com.example.entity.Font;

public interface IFontService extends IService<Font> {
    Result fontList();
}

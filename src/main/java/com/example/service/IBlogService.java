package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.Result;
import com.example.entity.Blog;

public interface IBlogService extends IService<Blog> {
    Result likeBlog(Long id);

    Result queryBlogById(String id);

    Result queryHotBlog(Integer current);

    Result queryBlogLikes(String id);
}

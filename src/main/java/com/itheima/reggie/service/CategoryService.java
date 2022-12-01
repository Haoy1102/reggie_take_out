package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @author haoy
 * @description
 * @date 2022/12/1 19:15
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}

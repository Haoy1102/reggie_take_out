package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author haoy
 * @description
 * @date 2022/12/1 20:24
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐和与setmeal_dish表中的关联数据
     * @param ids
     */
    public void deleteWithDishs(List<Long> ids);

    /**
     * 批量更改套餐的起售和停售状态
     * @param status
     * @param ids
     */
    public void changeStatus(int status,List<Long> ids);
}

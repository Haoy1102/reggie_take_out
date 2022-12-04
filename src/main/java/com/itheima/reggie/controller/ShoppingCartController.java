package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author haoy
 * @description
 * @date 2022/12/3 21:52
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     *
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置传来的id信息
        Long currentId = BaseContext.getCurrentId();

        Long dishId = shoppingCart.getDishId();
        Long setmealId = shoppingCart.getSetmealId();
        shoppingCart.setUserId(currentId);

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);
        //如果同一个菜品写了多次，则number++，不新建项
        //添加的是菜品
        if (null != dishId) {
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }//添加的是套餐
        else {
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }

        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
        //如果已经存在，就在原来数量基础上加1
        if (null!=cart){
            Integer number = cart.getNumber();
            cart.setNumber(number+1);
            shoppingCartService.updateById(cart);
        }//如果不存在，则添加到购物车，数量默认就是1
        else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now() );
            shoppingCartService.save(shoppingCart);
            cart=shoppingCart;
        }
        return R.success(cart);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime) ;
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空成功");
    }
}

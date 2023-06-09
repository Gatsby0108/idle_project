package com.idle.controller;

import com.idle.entity.RestBean;
import com.idle.entity.goods.Goods;
import com.idle.entity.user.AccountUser;
import com.idle.service.GoodsService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/goods")
public class GoodsController{

    @Resource
    GoodsService goodsService;

    @PostMapping("/set-goods")
    public RestBean<String> setGoods(Long id, Long publisher_id, String product_name, BigDecimal price,
                                     String category, String introduce, String picture){
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = formatter.format(date);
        boolean goods = goodsService.createGoods(id, publisher_id, product_name, price, category, introduce,picture,time);
        if(goods) return RestBean.success("商品发布成功");
        else return RestBean.failure(400,"商品发布失败，请检查商品信息是否完整");
    }

    @GetMapping("/get-goods")
    public RestBean<?> getAllGoods(){
        List<Goods> goods = goodsService.getAllGoods();
        if(goods == null) return RestBean.failure(401,"当前已无上架的商品");
        else return RestBean.success(goods);
    }

    @PostMapping("get-category-good")
    public RestBean<?> getGoodsByCategory(String category){
        List<Goods> goods = goodsService.getGoodsByCategory(category);
        if(goods == null || goods.size() == 0) return RestBean.failure(401,"该分类不存在");
        else return RestBean.success(goods);
    }

    @PostMapping("get-good-name")
    public RestBean<?> getGoodsByProductName(String search){
        List<Goods> goods = goodsService.getGoodsByProductNameOrIntroduce(search);
        if(goods == null || goods.size() == 0) return RestBean.failure(401,"未查询到相关商品");
        else return RestBean.success(goods);
    }

    @PostMapping("/find-goods")
    public RestBean<?>findGoodsById(Long id) throws Exception {
        List<Goods> goods = goodsService.findGoodsById(id);
        if( goods == null || goods.size() == 0) return RestBean.failure(401,"当前商品不存在");
        else return RestBean.success(goods);
    }

    @PostMapping("/get-publisher-goods")
    public RestBean<?> getGoodsByPublisherId(Long publisher_id){
        List<Goods> goods = goodsService.getGoodsByPublisherId(publisher_id);
        if(goods == null || goods.size() == 0) return RestBean.failure(401, "该用户还未上架商品");
        else return RestBean.success(goods);
    }

    @PostMapping("/get-publisher")
    public RestBean<?> getUsernameByPublisherId(Long publisher_id){
        AccountUser user = goodsService.getUserByPublisherId(publisher_id);
        if(user == null) return RestBean.failure(401,"该用户不存在");
        else return RestBean.success(user);
    }

    @PostMapping("/update-goods")
    public RestBean<String> updateGoods(Long id, String product_name, BigDecimal price, String category,
                                        String introduce, String picture){
       boolean goods = goodsService.updateGoods(id, product_name, price, category, introduce, picture);
       if(goods) return RestBean.success("商品修改成功");
       else return RestBean.failure(401,"当前商品不存在");
    }

    @PostMapping("/update-status")
    public RestBean<String> updateStatus(Integer status, Long id){
        boolean goods = goodsService.updateStatus(status, id);
        if(goods) return RestBean.success("商品状态更新成功");
        else return RestBean.failure(500,"内部错误，请联系管理员");
    }

    @PostMapping("/get-price")
    public RestBean<String> getPriceById(Long id){
        String price = goodsService.getPriceById(id);
        return RestBean.success(price);
    }
}

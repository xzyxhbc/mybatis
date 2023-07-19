package pers.xz.mybatis.dto;

import pers.xz.mybatis.entity.Category;
import pers.xz.mybatis.entity.Goods;
// Data Transfer Object--数据传输对象
// 对原始的数据进行扩展  用于数据保存或传递
public class GoodsDTO {
    // DTO类中的每一个变量都对应多表查询返回的结果
    private Goods goods = new Goods();
    private Category category = new Category();
    private String test;


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }


    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}

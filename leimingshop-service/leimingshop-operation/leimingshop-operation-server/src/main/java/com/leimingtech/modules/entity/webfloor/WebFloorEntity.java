/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.webfloor;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * h5楼层设置表
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_web_floor")
public class WebFloorEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 楼层名称
     */
    private String floorName;

    /**
     * 展示风格
     */
    private String showType;

    /**
     * 楼层主题图片
     */
    private String floorImg;

    /**
     * 楼层按钮的操作类型字段（link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索
     * goodsDetail:商品详情，xianshiqiangList:限时抢购列表）
     */
    private String actionType;

    /**
     * action_type对应的参数值（若actionType=link,这个字段就是连接地址...）
     */
    private String actionParams;

    /**
     * 楼层图标
     */
    private String nameIcon;

    /**
     * 楼层按钮名称
     */
    private String actionName;

    /**
     * 是否显示 0否 1是
     */
    private Integer isShow;

    /**
     * 楼层类型 1 h5 2 pc
     */
    private Integer floorType;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 广告位
     */
    private String advKey;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类别名称
     */
    private String goodsClassName;

    /**
     * 展示类别ids
     */
    private String classIds;

    /**
     * 店铺ID
     */
    private Long storeId;

    /**
     * 楼层样式
     */
    private Integer floorStyle;
}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.adv;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * h5楼层图片链接实体
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_adv_link_config")
public class AdvLinkConfigEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 广告ID
     */
    private Long advId;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subTitle;

    /**
     * 描述
     */
    private String description;

    /**
     * 跳转到不同的页面所需要的字段
     */
    private String typeKeyWord;

    /**
     * 图片url
     */
    private String imgUrl;

    /**
     * 链接类型：link:链接，searchGoodsByClass:分类查询结构列表界面，searchByKeyWord:按关键字检索，goodsDetail:商品详情，xianshiqiangList:限时抢购列表
     */
    private String linkType;

    /**
     * 位置标识(第几张图片)
     */
    private String imgMarking;

    /**
     * 图片所属的样式标识
     */
    private String typeShowMarking;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品类别名称
     */
    private String gcName;

    /**
     * 展示类别ids
     */
    private String classIds;

    /**
     * 是否删除 0否 1是
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date updateDate;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

}

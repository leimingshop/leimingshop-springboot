/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.search;

import com.leimingtech.modules.utils.FieldInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;


/**
 * 商品主图
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-06-04
 */
@Data
@ApiModel(description = "GoodsPictureDTO")
public class GoodsPictureDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 图片地址
     */
    @FieldInfo(type = "text")
    private String pictureUrl;

    /**
     * 是否主图（默认0否，1是）
     */
    @FieldInfo(type = "integer")
    private Integer isMainPicture;
    /**
     * 排序
     */
    @FieldInfo(type = "integer")
    private Integer sort;

}
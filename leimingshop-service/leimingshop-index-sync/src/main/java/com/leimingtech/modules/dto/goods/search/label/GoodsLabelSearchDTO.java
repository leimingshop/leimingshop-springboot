/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods.search.label;

import com.leimingtech.modules.utils.FieldInfo;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: weixianchun
 * @Description: 商品标签搜索实体
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelSearchDTO")
public class GoodsLabelSearchDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    @FieldInfo(type = "long")
    private Long labelId;

    /**
     * 标签名称
     */
    @FieldInfo(type = "text")
    private String labelName;

}

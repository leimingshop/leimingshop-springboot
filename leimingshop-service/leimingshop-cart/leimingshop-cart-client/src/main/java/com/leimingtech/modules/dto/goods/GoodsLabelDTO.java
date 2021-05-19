/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.goods;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: chengqian
 * @Description: 商品标签搜索实体
 * @Date :2019/5/20 14:28
 * @Version V1.0
 **/
@Data
@ApiModel(description = "GoodsLabelDTO")
public class GoodsLabelDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 标签id
     */
    private Long labelId;

    /**
     * 标签名称
     */
    private String labelName;

}

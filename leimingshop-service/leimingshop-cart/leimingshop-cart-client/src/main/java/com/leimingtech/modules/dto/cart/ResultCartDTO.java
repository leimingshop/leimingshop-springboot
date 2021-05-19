/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import com.leimingtech.modules.vo.DisabledCartVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 分页结果集
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-12
 */
@Data
@ApiModel(description = "ResultCartDTO")
public class ResultCartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "购物车商品总价钱")
    private BigDecimal totalPrice;
    @ApiModelProperty(value = "实际支付金额")
    private BigDecimal payPrice;
    @ApiModelProperty(value = "discountAmount")
    private BigDecimal discountAmount;
    @ApiModelProperty(value = "分页结果集")
    private List<FindCartDTO> findCartDTO;
    @ApiModelProperty(value = "是否全选(0：未选 1 全选)")
    private Integer isSelectAll;
    @ApiModelProperty("购物车选中数量")
    private Integer selectNum;
    @ApiModelProperty("购物车总数量")
    private Integer totalNum;

    @ApiModelProperty("失效购物车集合")
    private List<DisabledCartVO> cartVOList;
}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import com.leimingtech.modules.dto.evaluate.FindEvaluateStoreDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 店铺表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-06
 */
@Data
@ApiModel(description = "StoreInfoDTO")
public class StoreInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "店铺名称")
    private String storeName;

    @ApiModelProperty(value = "店铺认证（默认0未认证，1已认证）")
    private Integer storeAuth;

    @ApiModelProperty(value = "店铺等级ID")
    private Long gradeId;

    @ApiModelProperty(value = "店铺关注人数")
    private Integer favoriteCount;

    @ApiModelProperty(value = "店铺logo")
    private String storeLogo;

    @ApiModelProperty(value = "店铺类型（1:自营商户，2:普通商户）")
    private Integer storeType;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "是否关注（1:已收藏，0:未收藏）")
    private Integer isFavorite;

    @ApiModelProperty(value = "营业执照电子版")
    private String electronicBusinessLicense;

    @ApiModelProperty(value = "公司电话")
    private String companyPhone;

    @ApiModelProperty(value = "店铺地址")
    private String storeAddress;

    @ApiModelProperty(value = "店铺评分")
    private FindEvaluateStoreDTO findEvaluateStoreDTO;

    @ApiModelProperty(value = "店铺简介")
    private String storeIntro;

    @ApiModelProperty(value = "店铺综合评分")
    private Double synthesizeEvaluate;
}
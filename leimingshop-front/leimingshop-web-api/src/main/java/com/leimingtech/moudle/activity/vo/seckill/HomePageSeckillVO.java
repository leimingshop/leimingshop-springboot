/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.activity.vo.seckill;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 首页秒杀活动VO实体
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 9:51
 **/
@Data
@ApiModel(value = "首页秒杀活动商品信息")
public class HomePageSeckillVO implements Serializable {

    private static final long serialVersionUID = 532228016851242025L;

    @ApiModelProperty("当前活动场次字符串")
    private String currentDateStr;

    @ApiModelProperty("活动结束时间")
    private Date activityEndDate;

    @ApiModelProperty("下一场活动场次字符串")
    private String nextDateStr;

    @ApiModelProperty("秒杀活动商品集合")
    private List<SeckillGoodsVO> seckillGoodsList;
}

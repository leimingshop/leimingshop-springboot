/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.member;

import com.leimingtech.modules.constants.CollectionName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * 会员统计保存对象
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
@Document(collection = CollectionName.MEMBER_STATISTICS)
public class MemberStatisEntity implements Serializable {

    @ApiModelProperty(value = "主键ID")
    @Id
    private Long id;

    @ApiModelProperty(value = "会员增长总数")
    private Integer memberIncreaseNumber;

    @ApiModelProperty(value = "Android会员增长数")
    private Integer androidIncreaseNumber;

    @ApiModelProperty(value = "iOS会员增长数")
    private Integer iosIncreaseNumber;

    @ApiModelProperty(value = "PC会员增长数")
    private Integer pcIncreaseNumber;

    @ApiModelProperty(value = "h5/其他会员增长数")
    private Integer otherIncreaseNumber;

    @ApiModelProperty(value = "微信会员增长数")
    private Integer wechatIncreaseNumber;

    @ApiModelProperty(value = "小程序会员增长数")
    private Integer appletsIncreaseNumber;

    @ApiModelProperty(value = "创建时间（以小时结尾）")
    private Date createHourTime;

    @ApiModelProperty(value = "创建时间（以天为单位）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（以月为结尾）")
    private Date createMonthTime;


}

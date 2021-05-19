/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.store;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuzhch
 * @className MessageStoreReceiverDTO
 * @description 站内信接收店铺信息
 * @date 2020年4月22日15:59:59
 **/
@ApiModel(description = "MessageStoreReceiverDTO")
@Data
public class MessageStoreReceiverDTO {
    @ApiModelProperty(value = "店铺ID")
    private Long id;
    @ApiModelProperty(value = "昵称")
    private String storeName;
}

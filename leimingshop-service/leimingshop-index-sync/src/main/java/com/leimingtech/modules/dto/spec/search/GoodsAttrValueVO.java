/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/10 15:59
 * @Description
 **/
@Data
public class GoodsAttrValueVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldInfo
    private String attrValueName;
}

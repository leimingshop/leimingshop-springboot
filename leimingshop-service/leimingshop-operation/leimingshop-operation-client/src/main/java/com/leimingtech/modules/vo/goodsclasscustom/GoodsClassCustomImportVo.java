/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo.goodsclasscustom;

import lombok.Data;

import java.util.List;

/**
 * @author: lishuo
 * @Date: 20/7/2020 11:30
 * @email: lishuo@leimingtech.com
 * @Description: 展示分类导入vo
 */
@Data
public class GoodsClassCustomImportVo {
    private List<String> errorMessage;
    private Integer totalNum;
}
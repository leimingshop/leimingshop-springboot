/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.vo;

import com.leimingtech.modules.excel.goods.GoodsTemplateExcel;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author: lishuo
 * @Date: 30/6/2020 16:47
 * @email: lishuo@leimingtech.com
 * @Description: 商品导入结果实体
 */
@Data
public class GoodsImportResultVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 导入失败的商品名称
     */
    private List<String> errorGoodsNameList;
    /**
     * 导入失败的商品
     */
    private List<GoodsTemplateExcel> errorGoodsList;
    /**
     * 失效的商品分类
     */
    private List<String> errorGcName;
    /**
     * 总的商品数量
     */
    private Integer goodsSum;
    /**
     * 保存失败的商品名称和失败原因
     */
    private List<Map<String, String>> errorMessage;


}
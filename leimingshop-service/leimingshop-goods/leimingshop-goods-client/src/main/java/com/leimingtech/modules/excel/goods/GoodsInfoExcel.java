/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.excel.goods;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.util.Date;

/**
 * 商品附加信息表
 *
 * @author dy 1197793912@qq.com
 * @since 1.0.0 2019-06-04
 */
@Data
public class GoodsInfoExcel {
    @ExcelProperty(value = "主键")
    private Long id;
    @ExcelProperty(value = "商品ID")
    private Long goodsId;
    @ExcelProperty(value = "商品详细内容")
    private String goodsBoby;
    @ExcelProperty(value = "商品详细内容(手机版)")
    private String mobileBody;
    @ExcelProperty(value = "商品运费承担方式(默认 0为买家承担 1为卖家承担)")
    private Integer goodsTransfeeCharge;
    @ExcelProperty(value = "售后服务")
    private String afterSale;
    @ExcelProperty(value = "商品所在地(市)")
    private Long cityId;
    @ExcelProperty(value = "市名称")
    private String cityName;
    @ExcelProperty(value = "商品所在地(省)")
    private Long provinceId;
    @ExcelProperty(value = "省名称")
    private String provinceName;
    @ExcelProperty(value = "评论次数")
    private Integer commentNum;
    @ExcelProperty(value = "售出数量")
    private Integer saleNum;
    @ExcelProperty(value = "商品收藏数量")
    private Integer goodsCollectNum;
    @ExcelProperty(value = "商品浏览数")
    private Integer goodsBrowseNum;
    @ExcelProperty(value = "创建人")
    private String creator;
    @ExcelProperty(value = "创建时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;
    @ExcelProperty(value = "更新人")
    private String updater;
    @ExcelProperty(value = "更新时间")
    @DateTimeFormat(value = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

}
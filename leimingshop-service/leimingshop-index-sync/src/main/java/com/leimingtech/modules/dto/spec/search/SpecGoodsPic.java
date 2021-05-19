/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 商品详情页返回商品默认图集合（保存编辑商品第一行图片）
 * @Date: 2019/7/24 13:54
 * @Version: V1.0
 */
@Data
public class SpecGoodsPic implements Serializable {

    private static final long serialVersionUID = 1L;

    @FieldInfo
    private String pictureUrl;
    /**
     * 100*100图片地址
     */
    @FieldInfo
    private String oneHundredPxPictureUrl;
    /**
     * 200*200图片地址
     */
    @FieldInfo
    private String twoHundredPxPictureUrl;
    /**
     * 400*400图片地址
     */
    @FieldInfo
    private String fourHundredPxPictureUrl;
    /**
     * 800*800图片地址
     */
    @FieldInfo
    private String eightHundredPxPictureUrl;
    @FieldInfo(type = "integer")
    private Integer isMainPicture;
}

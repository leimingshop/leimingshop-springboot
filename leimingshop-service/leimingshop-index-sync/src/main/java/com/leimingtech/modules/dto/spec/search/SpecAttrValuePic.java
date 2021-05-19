/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.spec.search;

import com.leimingtech.modules.utils.FieldInfo;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author tyl
 * @Date 2019/7/3 18:43
 * @Description
 **/
@Data
public class SpecAttrValuePic implements Serializable {

    private static final long serialVersionUID = 1L;
    @FieldInfo(type = "long")
    private Long id;
    @FieldInfo(type = "long")
    private Long specAttrValueId;
    @FieldInfo(type = "long")
    private Long specAttrId;
    @FieldInfo(type = "long")
    private Long goodsId;
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

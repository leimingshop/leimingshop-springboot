/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.entity.upload;

import com.baomidou.mybatisplus.annotation.TableName;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 文件上传记录表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_upload_record")
public class UploadRecordEntity extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 文件路径url
     */
    private String fileUrl;

    /**
     * 文件真实名称
     */
    private String fileRealName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件大小（KB为单位）
     */
    private Integer fileSize;

    /**
     * 业务分类（在什么场景下上传）
     */
    private String uploadType;
}
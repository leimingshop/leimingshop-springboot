/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.uploadrecord;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.upload.UploadRecordEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传记录
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-24
 */
@Mapper
public interface UploadRecordDao extends BaseDao<UploadRecordEntity> {

}
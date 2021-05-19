/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.article;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.article.DocumentEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统文章表
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-20
 */
@Mapper
public interface DocumentDao extends BaseDao<DocumentEntity> {

}
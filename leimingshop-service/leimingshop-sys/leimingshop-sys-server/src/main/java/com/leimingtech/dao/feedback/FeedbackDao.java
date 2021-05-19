/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.feedback;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.entity.feedback.FeedbackEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */
@Mapper
public interface FeedbackDao extends BaseDao<FeedbackEntity> {

}
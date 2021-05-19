/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.dao.user.function;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.dto.index.IndexUserFunctionDTO;
import com.leimingtech.dto.user.function.UserFunctionInfoDTO;
import com.leimingtech.entity.user.function.UserFunctionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-03-16
 */
@Mapper
public interface UserFunctionDao extends BaseDao<UserFunctionEntity> {

    List<UserFunctionInfoDTO> selectUserFunctionByUserId(Long userId);

    List<IndexUserFunctionDTO> selectListByUserId(@Param("userId") Long userId, @Param("language") String language);
}
/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.index;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.index.StoreUserFunctionDTO;
import com.leimingtech.modules.entity.index.StoreUserFunctionEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 用户首页按钮配置
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-03-16
 */
@Mapper
public interface StoreUserFunctionDao extends BaseDao<StoreUserFunctionEntity> {
    /**
     * 条件查询
     *
     * @param params
     * @return
     */
    List<StoreUserFunctionDTO> getList(Map<String, Object> params);

    /**
     * 删除用户关联首页菜单按钮
     *
     * @param userId
     */
    void deleteByUserId(Long userId);
}
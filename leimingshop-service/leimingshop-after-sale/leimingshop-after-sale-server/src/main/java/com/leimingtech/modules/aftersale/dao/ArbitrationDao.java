/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.ArbitrationPageDTO;
import com.leimingtech.modules.aftersale.entity.ArbitrationEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 仲裁表
 *
 * @author xuzhch x170518@163.com
 * @since v1.0.0 2020-04-08
 */
@Mapper
public interface ArbitrationDao extends BaseDao<ArbitrationEntity> {

    /**
     * 查询仲裁分页列表
     *
     * @param params 查询条件
     * @param page   分页条件
     * @return 分页数据
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<ArbitrationPageDTO> selectPageList(@Param("params") Map<String, Object> params, IPage<ArbitrationEntity> page);

}

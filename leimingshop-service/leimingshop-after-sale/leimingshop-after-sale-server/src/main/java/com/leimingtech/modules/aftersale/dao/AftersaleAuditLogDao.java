/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleAuditLogPageDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleLogListDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleAuditLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 售后审核记录表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface AftersaleAuditLogDao extends BaseDao<AftersaleAuditLogEntity> {
    /**
     * 根据售后单号查询售后日志
     *
     * @param aftersaleSn 售后单号
     * @return list 售后日志列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleLogListDTO> findLog(@Param("aftersaleSn") Long aftersaleSn);

    /**
     * 审核列表查询
     *
     * @param params 查询条件
     * @param page   分页条件
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     * @Author weixianchun
     * @Description
     */
    List<AftersaleAuditLogPageDTO> findPage(@Param("params") Map<String, Object> params, IPage page);


    /**
     * 取消售后申请
     *
     * @param aftersaleSn 售后单号
     * @author xuzhch
     * @date 2020年09月21日
     */
    void updateCancel(@Param("aftersaleSn") Long aftersaleSn);
}

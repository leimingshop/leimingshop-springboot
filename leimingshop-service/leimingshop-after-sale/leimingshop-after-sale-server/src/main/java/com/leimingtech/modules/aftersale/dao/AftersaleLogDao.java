/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.dao;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.aftersale.dto.AftersaleLogDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleLogListDTO;
import com.leimingtech.modules.aftersale.entity.AftersaleLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 售后日志表
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */
@Mapper
public interface AftersaleLogDao extends BaseDao<AftersaleLogEntity> {

    /**
     * 查询H5售后日志记录
     *
     * @param aftersaleSn 售后单号
     * @param sort        排序规则（ASC、DESC）
     * @return list
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleLogDTO> findAfterFrontLog(@Param("aftersaleSn") Long aftersaleSn, @Param("sort") String sort);

    /**
     * 查询售后日志列表
     *
     * @param aftersaleSn 售后单号
     * @return list 售后日志列表
     * @author xuzhch
     * @date 2020年09月21日
     */
    List<AftersaleLogListDTO> findLog(@Param("aftersaleSn") Long aftersaleSn);

    /**
     * 根据售后流程状态查询售后日志
     *
     * @param aftersaleSn 售后单号
     * @param value       售后单状态
     * @return 售后日志信息
     * @author xuzhch
     * @date 2020年09月21日
     */
    AftersaleLogDTO findlogByStatus(@Param("aftersaleSn") Long aftersaleSn, @Param("value") Integer value);
}

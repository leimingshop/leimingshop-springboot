/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.log;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.log.MemberLoginLogDTO;
import com.leimingtech.modules.entity.log.MemberLoginLogEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 用户登录日志表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-30
 */
@Mapper
public interface MemberLoginLogDao extends BaseDao<MemberLoginLogEntity> {

    /**
     * 查询用户登录日志列表
     *
     * @param page   分页参数
     * @param params 查询条件
     * @return list 用户登录日志列表
     * @author xuzhch
     * @date 2020年09月18日
     */
    List<MemberLoginLogDTO> selectMemberList(Page page, @Param("params") Map<String, Object> params);

}

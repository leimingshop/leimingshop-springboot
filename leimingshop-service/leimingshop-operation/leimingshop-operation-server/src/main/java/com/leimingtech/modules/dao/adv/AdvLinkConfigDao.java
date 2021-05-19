/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.adv;

import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.adv.AdvLinkConfigDTO;
import com.leimingtech.modules.entity.adv.AdvLinkConfigEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * h5楼层图片链接
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-10
 */
@Mapper
public interface AdvLinkConfigDao extends BaseDao<AdvLinkConfigEntity> {

    /**
     * 删除广告配置
     *
     * @param advId
     */
    void logicDelete(@Param("ids") List<Long> advId);

    /**
     * 查询广告配置
     *
     * @param id
     * @return
     */
    List<AdvLinkConfigDTO> getLinkList(Long id);
}

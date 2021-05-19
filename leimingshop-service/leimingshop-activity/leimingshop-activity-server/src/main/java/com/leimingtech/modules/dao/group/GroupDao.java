/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.group;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.group.GroupDTO;
import com.leimingtech.modules.entity.group.GroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 拼团
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */
@Mapper
public interface GroupDao extends BaseDao<GroupEntity> {

    /**
     * 查询所有拼团预热活动
     *
     * @return
     * @date 2020-03-16 16:59
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupEntity> selectPreheatList();

    /**
     * 查询拼团活动列表分页
     *
     * @param
     * @return
     * @date 2020-04-15 10:08
     * @author huangkeyuan@leimingtech.com
     **/
    List<GroupDTO> sellerPage(IPage page, @Param("params") Map<String, Object> params);
}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.label;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goodslable.LabelGroupDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupUpdateDTO;
import com.leimingtech.modules.entity.label.LabelGroupEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 标签分组
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */
@Mapper
public interface LabelGroupDao extends BaseDao<LabelGroupEntity> {

    /**
     * 修改时校验分组名称是否重复
     *
     * @param dto
     * @return 返回重复数量
     * @Author weixianchun
     * @Description 修改时校验分组名称是否重复
     * @Date 2019/12/11 15:02
     * @version 1.0
     */
    int checkGroupNameUpate(LabelGroupUpdateDTO dto);

    /**
     * 保存时校验分组名称是否重复
     *
     * @param groupName 分组名称
     * @return 返回重复数量
     * @Author weixianchun
     * @Description 保存时校验分组名称是否重复
     * @Date 2019/12/11 15:02
     * @version 1.0
     */
    int checkGroupNameSave(@Param("groupName") String groupName);

    /**
     * 分页查询所有分组信息
     *
     * @param params
     * @return java.util.List<com.leimingtech.modules.dto.goodslable.LabelGroupDTO>
     * @Author weixianchun
     * @Description 分页查询所有分组信息
     * @Date 2019/12/13 10:32
     * @version 1.0
     */
    List<LabelGroupDTO> findListPage(Map<String, Object> params);

    /**
     * 获取分组信息
     *
     * @param labelId 标签id
     * @return 返回标签分组名称
     */
    List<LabelGroupDTO> getByLabelId(Long labelId);
}

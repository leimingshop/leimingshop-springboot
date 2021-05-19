/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.label;


import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.goodslable.LabelNumDTO;
import com.leimingtech.modules.entity.label.LabelGroupRelEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 标签与标签分组关联管理
 *
 * @author weixianchun ${email}
 * @since v1.0.0 2019-12-09
 */
@Mapper
public interface LabelGroupRelDao extends BaseDao<LabelGroupRelEntity> {

    /**
     * 根据分组ID查询标签数量
     *
     * @param groupId 分组id
     * @return com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO
     * @Author weixianchun
     * @Description 根据分组ID查询标签数量
     * @Date 2019/12/11 12:08
     * @version 1.0
     */
    LabelNumDTO findLabelNum(@Param("groupId") Long groupId);

    /**
     * 根据分组ID删除数据
     *
     * @param groupId 分组id
     * @return 返回删除成功数量
     * @Author weixianchun
     * @Description 根据分组ID删除数据
     * @Date 2019/12/12 10:52
     * @version 1.0
     */
    int deleteByGroupId(Long groupId);

    /**
     * 根据标签ID删除数据
     *
     * @param labelId 分组id
     * @return 返回删除成功数量
     * @Author weixianchun
     * @Description 根据标签ID删除数据
     * @Date 2019/12/12 10:52
     * @version 1.0
     */
    int deleteByLabelId(Long labelId);
}

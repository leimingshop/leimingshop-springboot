/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodslabel;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goodslable.GoodsGroupFindDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupSaveDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标签分组
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */

public interface LabelGroupService {
    /**
     * 分页查询分组信息
     *
     * @param params 查询参数
     * @return com.leimingtech.commons.tools.page.PageData<com.leimingtech.modules.dto.goodslable.LabelGroupDTO>
     * @Author weixianchun
     * @Description 分页查询分组信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    PageData<LabelGroupDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有分组信息
     *
     * @param params 查询参数
     * @return java.util.List<com.leimingtech.modules.dto.goodslable.LabelGroupDTO>
     * @Author weixianchun
     * @Description 查询所有分组信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    List<LabelGroupDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID获取分组信息
     *
     * @param id
     * @return com.leimingtech.modules.dto.goodslable.LabelGroupDTO
     * @Author weixianchun
     * @Description 根据ID获取分组信息
     * @Date 2019/12/11 9:27
     * @version 1.0
     */

    LabelGroupDTO get(Long id);

    /**
     * 分组标签关联回显
     *
     * @param id
     * @return com.leimingtech.modules.dto.goodslable.GoodsGroupFindDTO
     * @Author weixianchun
     * @Description 分组标签关联回显
     * @Date 2019/12/12 21:38
     * @version 1.0
     */

    GoodsGroupFindDTO findByGroupId(Long id);

    /**
     * 保存分组信息
     *
     * @param dto
     * @Author weixianchun
     * @Description 保存分组信息
     * @Date 2019/12/11 9:27
     * @version 1.0
     * @deprecated void
     */

    void save(@RequestBody LabelGroupSaveDTO dto);

    /**
     * 批量保存分组信息
     *
     * @param dtoList
     * @return 返回是否保存成功
     * @Author weixianchun
     * @Description 批量保存分组信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    boolean insertBatch(@RequestBody List<LabelGroupSaveDTO> dtoList);

    /**
     * 修改分组信息
     *
     * @param dto
     * @return 返回修改成功数量
     * @Author weixianchun
     * @Description 修改分组信息
     * @Date 2019/12/11 9:28
     * @version 1.0
     */

    int update(@RequestBody LabelGroupUpdateDTO dto);

    /**
     * 修改分组状态(启用, 禁用)
     *
     * @param id          分组id
     * @param groupStatus 分组状态
     * @return 返回修改成功数量
     * @Author weixianchun
     * @Description 修改分组状态(启用, 禁用)
     * @Date 2019/12/11 9:28
     * @version 1.0
     */

    int updateGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id);

    /**
     * 删除分组信息
     *
     * @param ids
     * @Author weixianchun
     * @Description 删除分组信息
     * @Date 2019/12/11 9:28
     * @version 1.0
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 修改时校验分组名称是否重复
     *
     * @param dto 查询参数
     * @return 返回重复数量
     * @Author weixianchun
     * @Description 修改时校验分组名称是否重复
     * @Date 2019/12/11 15:02
     * @version 1.0
     */

    int checkGroupNameUpate(@RequestBody LabelGroupUpdateDTO dto);

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

    int checkGroupNameSave(@RequestParam("groupName") String groupName);

    /**
     * 获取标签分组信息
     *
     * @param labelId 标签id
     * @return
     */

    List<LabelGroupDTO> getByLabelId(Long labelId);
}

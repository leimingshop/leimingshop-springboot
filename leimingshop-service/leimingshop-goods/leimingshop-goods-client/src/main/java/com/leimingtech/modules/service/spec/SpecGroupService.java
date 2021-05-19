/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.spec.InsertSpecGroupDTO;
import com.leimingtech.modules.dto.spec.SpecGroupAndSpecDTO;
import com.leimingtech.modules.dto.spec.SpecGroupDTO;
import com.leimingtech.modules.dto.spec.UpdateSpecGroupDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */

public interface SpecGroupService {

    /**
     * 功能描述:
     * 〈规格分组分页查询〉
     *
     * @param params 查询条件
     * @return 返回规格 分组
     * @author : 刘远杰
     */

    PageData<SpecGroupDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈查询规格分组及规格列表〉
     *
     * @param params 查询条件
     * @return 返回规格 分组
     * @author : 刘远杰
     */

    List<SpecGroupAndSpecDTO> listSpec(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈保存规格分组及关联规格〉
     *
     * @param dto 新增规格分组实体
     * @return 返回保存成功结果
     * @author : 刘远杰
     */

    Map<String, Object> saveSpecGroup(@RequestBody InsertSpecGroupDTO dto);

    /**
     * 功能描述:
     * 〈修改规格分组及关联规格〉
     *
     * @param dto 更新规格分组实体
     * @return 返回修改成功结果
     * @author : 刘远杰
     */

    Map<String, Object> updateSpecGroup(@RequestBody UpdateSpecGroupDTO dto);

    /**
     * 功能描述:
     * 〈查询分组详情及其关联的规格〉
     *
     * @param id 规格分组id
     * @return 返回规格分组信息
     * @author : 刘远杰
     */

    SpecGroupAndSpecDTO findSpecGroupAndSpec(@RequestParam("id") Long id);

    /**
     * 功能描述:
     * 〈新增规格分组校验规格分组名称重复性〉
     *
     * @param specGroupName 规格分组名称
     * @return 返回分组名称是否重复
     * @author : 刘远杰
     */

    Map<String, Object> checkSpecGroupNameWhenAdd(@RequestParam("specGroupName") String specGroupName);

    /**
     * 功能描述:
     * 〈修改规格分组校验规格分组名称重复性〉
     *
     * @param specGroupName 规格分组名称
     * @param id            规格分组id
     * @return 返回是否重复
     * @author : 刘远杰
     */

    Map<String, Object> checkSpecGroupNameWhenUpdate(@RequestParam("specGroupName") String specGroupName, @RequestParam("id") Long id);

    /**
     * 功能描述:
     * 〈删除规格分组〉
     *
     * @param id 规格分组id
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    int deleteSpecGroup(Long id);

    /**
     * 功能描述:
     * 〈批量删除规格分组〉
     *
     * @param ids 规格id数组
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    int deleteSpecGroupBatch(@RequestBody Long[] ids);

    /**
     * 根据id获取规格分组详情
     *
     * @param id 主键id
     * @return 返回规格分组信息
     */

    SpecGroupDTO get(Long id);

    /**
     * 功能描述:
     * 〈分组状态改变〉
     *
     * @param groupStatus 改变后状态
     * @param id          分组id
     * @return : int
     * @author : 刘远杰
     */

    int updateGroupStatus(@RequestParam("groupStatus") Integer groupStatus, @RequestParam("id") Long id);

}

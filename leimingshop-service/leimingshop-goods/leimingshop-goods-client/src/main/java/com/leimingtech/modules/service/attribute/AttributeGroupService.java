/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.attribute.AttrGroupAndAttrDTO;
import com.leimingtech.modules.dto.attribute.AttributeGroupDTO;
import com.leimingtech.modules.dto.attribute.InsertAttributeGroupDTO;
import com.leimingtech.modules.dto.attribute.UpdateAttributeGroupDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 属性分组管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */

public interface AttributeGroupService {

    /**
     * 保存属性分组数据
     *
     * @param dto 实体
     * @return 返回保存结果
     * @author 刘远杰
     * @Description 保存属性分组数据
     * @Data 2019/5/20 16:39
     */

    Map<String, Object> saveAttrGroup(@RequestBody InsertAttributeGroupDTO dto);


    /**
     * 功能描述:
     * 〈查询属性分组及属性列表〉
     *
     * @param params 查询条件
     * @return 返回属性分组及属性列表
     * @author : 刘远杰
     */

    List<AttrGroupAndAttrDTO> listAttr(@RequestParam Map<String, Object> params);

    /**
     * 修改属性分组数据
     *
     * @param dto 实体
     * @return 返回修改结果
     * @author 刘远杰
     * @Description 修改属性分组数据
     * @Date 2019/5/20 16:39
     */

    Map<String, Object> updateAttrGroup(@RequestBody UpdateAttributeGroupDTO dto);

    /**
     * 查询属性分组详情及关联属性
     *
     * @param id
     * @return 返回属性分组详情和关联属性
     * @author 刘远杰
     * @Description 查询属性分组详情及关联属性
     * @Date 2019/5/20 17:17
     */

    AttrGroupAndAttrDTO findAttrGroupAndAttr(Long id);

    /**
     * 修改属性分组校验属性分组名称重复性
     *
     * @param attrGroupName 属性分组名称
     * @return 返回是否重复
     * @author 刘远杰
     * @Description 修改属性分组校验属性分组名称重复性
     * @Date 2019/5/20 16:19
     */

    Map<String, Object> checkAttrGroupNameWhenAdd(@RequestParam("attrGroupName") String attrGroupName);

    /**
     * 修改属性分组校验属性分组名称重复性
     *
     * @param attrGroupName 属性分组名称
     * @param id            属性分组id
     * @return 返回是否重复
     * @author 刘远杰
     * @Description 修改属性分组校验属性分组名称重复性
     * @Date 2019/5/20 16:20
     */

    Map<String, Object> checkAttrGroupNameWhenUpdate(@RequestParam("arrtGroupName") String attrGroupName, @RequestParam("id") Long id);

    /**
     * 删除属性分组
     *
     * @param id
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 删除属性分组
     * @Date 2019/5/20 17:40
     */

    int deleteAttrGroup(Long id);

    /**
     * 删除属性分组
     *
     * @param ids 属性分组数组
     * @return 返回删除成功数量
     * @author 刘远杰
     * @Description 删除属性分组
     * @Date 2019/5/20 17:40
     */

    int deleteAttrGroupBatch(@RequestBody Long[] ids);

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

    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return 返回分页信息
     */

    PageData<AttributeGroupDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据分组id查询分组〉
     *
     * @param id
     * @return 返回详情信息
     * @author : 刘远杰
     */

    AttributeGroupDTO get(Long id);
}

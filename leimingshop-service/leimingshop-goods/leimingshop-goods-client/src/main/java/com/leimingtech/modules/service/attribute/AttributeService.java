/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attribute;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.attribute.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 属性管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-20
 */

public interface AttributeService {
    /**
     * 功能描述:
     * 〈保存属性、属性值〉
     *
     * @param dto 属性实体
     * @return 返回保存结果
     * @author : 刘远杰
     */

    Map<String, Object> saveAttr(@RequestBody InsertAttributeDTO dto);

    /**
     * 功能描述:
     * 〈保存属性、属性值〉
     *
     * @param dto 属性实体
     * @return 返回保存结果
     * @author : 刘远杰
     */

    Map<String, Object> updateAttr(@RequestBody UpdateAttributeDTO dto);

    /**
     * 功能描述:
     * 〈查询属性及属性值详情〉
     *
     * @param id 属性id
     * @return 返回属性值详情
     * @author : 刘远杰
     */

    AttributeAndAttributeValueDTO findAttrAndAttrValueByAttrId(Long id);

    /**
     * 功能描述:
     * 〈判断属性是否关联后台分类〉
     *
     * @param id 属性id
     * @return 返回已经关联的后台分类
     * @author : 刘远杰
     */

    List<AttrClassDTO> checkClass(@RequestBody Long[] id);

    /**
     * 功能描述:
     * 〈删除属性及属性值〉
     *
     * @param id 属性id
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    Integer deleteAttr(Long id);

    /**
     * 功能描述:
     * 〈批量删除属性及属性值〉
     *
     * @param ids 属性id数组
     * @return : int
     * @author : 刘远杰
     */

    int deleteAttrBatch(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈新增属性校验属性名称是否重复〉
     *
     * @param attrName 属性名称
     * @return 返回是否重复
     * @author : 刘远杰
     */

    Map<String, Object> checkAttrNameWhenAdd(@RequestParam("attrName") String attrName);

    /**
     * 功能描述:
     * 〈修改规格校验规格名称是否重复〉
     *
     * @param attrName 属性名称
     * @param id       属性id
     * @return 返回是否重复
     * @author : 刘远杰
     */

    Map<String, Object> checkAttrNameWhenUpdate(@RequestParam("attrName") String attrName, @RequestParam("id") Long id);

    /**
     * 功能描述:
     * 〈属性分页查询〉
     *
     * @param params 查询条件
     * @return 返回属性分页信息
     * @author : 刘远杰
     */

    PageData<AttributeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈属性列表查询〉
     *
     * @param params 查询条件
     * @return 返回属性列表信息
     * @author : 刘远杰
     */

    List<AttributeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈批量查询〉
     *
     * @param ids 属性id数组
     * @return 返回属性信息
     * @author : 刘远杰
     */

    List<AttributeDTO> selectBatchByids(@RequestBody List<Long> ids);

    /**
     * 功能描述:
     * 〈批量查询属性名称〉
     *
     * @param ids 查询的属性ids
     * @return 返回属性信息
     * @author : 刘远杰
     */

    List<AttributeIdAndNameDTO> selectNameBatchByids(@RequestBody Long[] ids);
}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.spec;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.spec.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 规格管理
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 7.0 2019-05-16
 */
public interface SpecService {

    /**
     * 功能描述:
     * 〈规格条件分页查询〉
     *
     * @param params 查询条件
     * @return 返回规格信息
     * @author : 刘远杰
     */

    PageData<SpecDTO> findSpecPageList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈规格条件查询〉
     *
     * @param params 查询条件
     * @return 返回规格信息
     * @author : 刘远杰
     */

    List<SpecDTO> findSpecList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈保存规格及规格值〉
     *
     * @param dto 规格实体
     * @return 返回保存结果
     * @author : 刘远杰
     */

    Map<String, Object> saveSpec(@RequestBody InsertSpecDTO dto);

    /**
     * 功能描述:
     * 〈修改规格及规格值〉
     *
     * @param dto 规格实体
     * @return 返回修改结果信息
     * @author : 刘远杰
     */

    Map<String, Object> updateSpec(@RequestBody UpdateSpecDTO dto);

    /**
     * 功能描述:
     * 〈判断规格是否关联后台分类〉
     *
     * @param ids 规格id
     * @return 返回规格关联分类信息
     * @author : 刘远杰
     */

    List<SpecClassDTO> checkClass(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈逻辑删除规格、规格值〉
     *
     * @param id 规格id
     * @author : 刘远杰
     */

    void deleteSpec(Long id);

    /**
     * 功能描述:
     * 〈批量删除规格数组〉
     *
     * @param ids 规格id数组
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    int deleteSpecBatch(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈根据规格id子查询规格及规格值〉
     *
     * @param id 规格id
     * @return 返回规格值
     * @author : 刘远杰
     */

    SpecAndSpecValueDTO findSpecAndSpecValueBySpecId(Long id);

    /**
     * 功能描述:
     * 〈查询规格列表〉
     *
     * @return 返回规格信息
     * @author : 刘远杰
     */

    List<SpecDTO> findSpecList();


    /**
     * 功能描述:
     * 〈新增规格校验规格名称是否重复〉
     *
     * @param specName 规格名称
     * @return 返回保存结果信息
     * @author : 刘远杰
     */

    Map<String, Object> checkSpecNameWhenAdd(@RequestParam("specName") String specName);

    /**
     * 功能描述:
     * 〈修改规格校验规格名称是否重复〉
     *
     * @param specName 规格名称
     * @param id       主键id
     * @return 返回规格名称是否重复信息
     * @author : 刘远杰
     */

    Map<String, Object> checkSpecNameWhenUpdate(@RequestParam("specName") String specName, @RequestParam("id") Long id);

    /**
     * 功能描述:
     * 〈批量查询〉
     *
     * @param ids 查询的规格ids
     * @return 返回规格信息
     * @author : 刘远杰
     */

    List<SpecDTO> selectBatchByids(@RequestBody List<Long> ids);

    /**
     * 功能描述:
     * 〈批量查询规格名称〉
     *
     * @param ids 查询的规格ids
     * @return 返回规格信息
     * @author : 刘远杰
     */

    List<SpecIdAndNameDTO> selectNameBatchByids(@RequestBody Long[] ids);

}

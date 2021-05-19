/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.grade;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.grade.StoreGradeDTO;
import com.leimingtech.modules.dto.grade.StoreGradeSaveDTO;
import com.leimingtech.modules.dto.grade.StoreGradeUpdateDTO;
import com.leimingtech.modules.dto.grade.StoreShowFlagUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 店铺等级
 *
 * @author xuzhch
 * @email x170518@163.com
 * @since 1.0.0 2019-05-30
 */

public interface StoreGradeService {
    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<StoreGradeDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出
     *
     * @param params
     * @return
     */

    List<StoreGradeDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据ID查询店铺等级详情
     *
     * @param id
     * @return
     */

    StoreGradeDTO get(Long id);

    /**
     * 根据ID查询店铺等级名称
     *
     * @param id
     * @return
     */

    String getNameById(Long id);

    /**
     * 保存店铺等级
     *
     * @param dto
     */

    void save(@RequestBody StoreGradeSaveDTO dto);

    /**
     * 修改店铺等级
     *
     * @param dto
     */

    void update(@RequestBody StoreGradeUpdateDTO dto);

    /**
     * 根据ID删除店铺等级
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 启用/禁用店铺
     *
     * @param storeShowFlagUpdateDTO
     */

    void updateShowFlag(@RequestBody StoreShowFlagUpdateDTO storeShowFlagUpdateDTO);


    /**
     * 校验等级名称是否重复
     *
     * @param params
     * @return
     */

    Integer checkGradeName(@RequestParam Map<String, Object> params);

    /**
     * 获取发布商品数量最少的店铺等级
     *
     * @return
     */

    Long selectGradeId();

}
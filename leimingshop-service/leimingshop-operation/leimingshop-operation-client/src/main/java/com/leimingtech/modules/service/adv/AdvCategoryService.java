/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.adv.AdvCategoryDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 广告类别管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */

public interface AdvCategoryService {

    /**
     * 功能描述:
     * 〈广告类别分页查询〉
     *
     * @param params 查询条件
     * @return 返回广告分类分页
     * @author : 刘远杰
     */

    PageData<AdvCategoryDTO> findAdvCategoryPageList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈广告类别查询〉
     *
     * @param params 查询条件
     * @return : java.util.List<com.leimingtech.modules.adv.dto.AdvCategoryDTO>
     * @author : 刘远杰
     */

    List<AdvCategoryDTO> findAdvCategoryList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据id查询广告类别〉
     *
     * @param id 广告类别id
     * @return 返回广告分类详情
     * @author : 刘远杰
     */

    AdvCategoryDTO get(Long id);

    /**
     * 功能描述:
     * 〈保存广告类别〉
     *
     * @param dto 广告类别实体
     * @return : void
     * @author : 刘远杰
     */

    void save(@RequestBody AdvCategoryDTO dto);

    /**
     * 功能描述:
     * 〈校验广告类别标识是否重复〉
     *
     * @param advKey 广告类别标识
     * @return 返回校验结果
     * @author : 刘远杰
     */

    Map<String, Object> checkAdvKey(@RequestParam("advKey") String advKey);

    /**
     * 功能描述:
     * 〈广告分类逻辑删除〉
     *
     * @param ids 广告分类ids
     * @return 返回删除结果
     * @author : 刘远杰
     */

    Map<String, Object> logicDeleteAdvCategory(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * 〈更新非系统定义的广告分类〉
     *
     * @param dto 广告分类实体
     * @return 返回更新结果
     * @author : 刘远杰
     */

    Map<String, Object> updateAdvCategory(@RequestBody AdvCategoryDTO dto);

    /**
     * 功能描述:
     * 〈根据广告分类标识查询广告分类〉
     *
     * @param advKey 广告分类标识
     * @return 返回广告分类
     * @author : 刘远杰
     */

    AdvCategoryDTO findAdvCategoryByAdvKey(String advKey);

    /**
     * 功能描述:
     * 〈更新广告类别状态〉
     *
     * @param status 更改后状态
     * @param id     广告类别id
     * @return : int
     * @author : 刘远杰
     */

    int updateStatus(@RequestParam("status") Integer status, @RequestParam("id") Long id);

}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.reduce;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleSaveDTO;

import java.util.List;
import java.util.Map;

/**
 * 满减活动规则表
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-26
 */

public interface ReduceRuleService {


    PageData<ReduceRuleDTO> page(Map<String, Object> params);


    List<ReduceRuleDTO> list(Map<String, Object> params);


    ReduceRuleDTO get(Long id);


    void save(ReduceRuleDTO dto);


    void update(ReduceRuleDTO dto);


    void delete(Long[] ids);

    /**
     * @Author weixianchun
     * @Description 批量保存满减规则信息
     * @Param dtoList
     * @Date 2019/12/26 18:45
     * @Return java.lang.Boolean
     * @version 1.0
     */

    Boolean saveBatch(List<ReduceRuleSaveDTO> dtoList);

    /**
     * 功能描述:
     * 〈获取满减活动关联的规则集合〉
     *
     * @param activityId 满减活动id
     * @author : 刘远杰
     */

    List<ReduceRuleDTO> getByActivityId(Long activityId);

    /**
     * @Author weixianchun
     * @Description 根据活动ID删除商品关联信息
     * @Param activityId
     * @Date 2019/12/26 14:11
     * @Return void
     * @version 1.0
     */

    void deleteByActivityId(Long activityId);
}

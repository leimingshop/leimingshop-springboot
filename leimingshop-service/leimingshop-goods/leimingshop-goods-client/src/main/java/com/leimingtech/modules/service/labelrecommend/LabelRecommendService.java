/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.labelrecommend;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.labelrecommend.LabelRecommendDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标签推荐表
 *
 * @author weixianchun weixianchun@leimingtech.com
 * @since v1.0.0 2020-01-09
 */

public interface LabelRecommendService {
    /**
     * 分页查询推荐标签
     *
     * @param params 查询参数
     * @return 返回推荐标签信息
     */

    PageData<LabelRecommendDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询推荐标签
     *
     * @param params 查询参数
     * @return 返回推荐标签
     */

    List<LabelRecommendDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询推荐标签
     *
     * @param id 主键id
     * @return 返回推荐标签信息
     */

    LabelRecommendDTO get(Long id);

    /**
     * 保存推荐标签
     *
     * @param dto 保存参数
     */

    void save(@RequestBody LabelRecommendDTO dto);

    /**
     * 修改推荐标签
     *
     * @param dto 修改参数
     */

    void update(@RequestBody LabelRecommendDTO dto);

    /**
     * 删除推荐标签
     *
     * @param ids 主键id
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 功能描述:
     * <修改标签状态>
     *
     * @param labelStatus 标签状态(启用,禁用)
     * @param id          标签推荐ID
     * @return 根据数量判断是否修改成功
     * @date 2020/1/9 15:29
     * @author weixianchun
     **/

    int updateLabelStatus(@RequestParam("labelStatus") Integer labelStatus, @RequestParam("id") Long id);

    /**
     * 功能描述:
     * <保存时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @return 返回推荐标签
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/

    LabelRecommendDTO findByName(@RequestParam("labelName") String labelName, @RequestParam("labelFlag") String labelFlag);

    /**
     * 功能描述:
     * <修改时-根据标签名称 标识查询标签推荐信息>
     *
     * @param labelName 标签名称
     * @param labelFlag 标识
     * @param id        标签id
     * @return 返回数量
     * @date 2020/1/9 16:23
     * @author weixianchun
     **/

    int findByLabelNameUpdate(@RequestParam("id") Long id, @RequestParam("labelName") String labelName, @RequestParam("labelFlag") String labelFlag);
}

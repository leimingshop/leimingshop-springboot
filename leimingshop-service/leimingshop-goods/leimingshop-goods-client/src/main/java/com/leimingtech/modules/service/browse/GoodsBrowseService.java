/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.browse;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.browse.BrowseCmsDTO;
import com.leimingtech.modules.dto.browse.FindGoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.GoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.MqGoodsBrowseDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 足迹记录表
 *
 * @author chengqian
 * @email 543826372@qq.com
 * @since 1.0.0 2019-06-03
 */

public interface GoodsBrowseService {
    /**
     * 分页查询信息
     *
     * @param params 查询参数
     * @return
     */

    PageData<FindGoodsBrowseDTO> pageList(@RequestParam Map<String, Object> params);

    /**
     * 新增记录
     *
     * @param goodsBrowseDTO
     */

    void save(@RequestBody MqGoodsBrowseDTO goodsBrowseDTO);

    /**
     * 更新浏览记录
     *
     * @param dto 参数实体
     */

    void update(@RequestBody GoodsBrowseDTO dto);

    /**
     * 删除浏览记录
     *
     * @param ids      主键ID
     * @param state    1 清空记录
     * @param memberId 用户id
     */

    void delete(@RequestBody(required = false) Long[] ids, @RequestParam(value = "state", required = false) Integer state,
                @RequestParam("memberId") Long memberId);


//    /**
//     * 查询是否有记录，判断更新或者保存记录
//     *
//     * @param specId 主键ID
//     */
//
//    void findById(@RequestParam("specId") Long specId);

    /**
     * 查询用户足迹数量
     *
     * @param id
     * @return
     */

    Integer findNum(Long id);

    /**
     * 用户浏览商品ID
     *
     * @param memberId 用户ID
     * @return
     * @Author: yuhaifeng
     */

    List<BrowseCmsDTO> getBrowseGoodIds(@RequestParam("memberId") Long memberId);

    /**
     * pc端查询用户浏览记录
     *
     * @param params 查询条件
     * @return 返回用户浏览记录
     */

    List<MqGoodsBrowseDTO> pcList(@RequestParam Map<String, Object> params);
}
/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goods.check;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goods.check.GoodsCheckDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 商品审核表
 *
 * @author dy
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-06-13
 */

public interface GoodsCheckService {

    /**
     * 分页
     *
     * @param params
     * @return
     */

    PageData<GoodsCheckDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 列表
     *
     * @param params
     * @return
     */

    List<GoodsCheckDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 查询详情
     *
     * @param id
     * @return
     */

    GoodsCheckDTO get(Long id);

    /**
     * 保存
     *
     * @param dto
     */

    void save(@RequestBody GoodsCheckDTO dto);

    /**
     * 修改
     *
     * @param dto
     */

    void update(@RequestBody GoodsCheckDTO dto);

    /**
     * 删除
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据商品id查询原因
     *
     * @param goodsId
     * @return
     */

    GoodsCheckDTO selectByGoodsId(Long goodsId);


    /**
     * 根据商品id查询操作记录
     *
     * @param goodsId 商品id
     * @return 返回商品操作记录
     */

    List<GoodsCheckDTO> selectByGoodsIdTocheck(Long goodsId);

    /**
     * 保存商品发布纪律 并保存自动审核记录
     *
     * @param dto    保存实体
     * @param status 状态
     */

    void saveBatch(@RequestBody GoodsCheckDTO dto, @RequestParam("status") int status);

    /**
     * 功能描述 批量保存 foreach
     *
     * @param goodsCheckDTOList
     * @return void
     * @author lishuo
     * @date 24/7/2020
     */

    void insertBatch(@RequestBody List<GoodsCheckDTO> goodsCheckDTOList);
}
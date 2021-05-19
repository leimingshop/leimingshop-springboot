/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.service;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AftersaleGoodsDTO;
import com.leimingtech.modules.aftersale.dto.AftersaleGoodsSaveDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 售后商品
 *
 * @author huangkeyuan
 * @email huangkeyuan@leimingtech.dao
 * @since 1.0.0 2019-06-13
 */

public interface AftersaleGoodsService {
    /**
     * 保存售后商品记录
     *
     * @param dto 售后商品信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void save(@RequestBody AftersaleGoodsSaveDTO dto);

    /**
     * 修改售后商品记录
     *
     * @param dto 售后商品信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    void update(@RequestBody AftersaleGoodsDTO dto);

    /**
     * 删除售后商品记录
     *
     * @param ids 售后商品信息ID数组
     * @author xuzhch
     * @date 2020年09月21日
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID查询售后商品记录
     *
     * @param id 售后商品信息ID
     * @return goods dto
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleGoodsDTO get(Long id);

    /**
     * 查询所有的售后商品记录列表
     *
     * @param params 查询条件
     * @return java.util.List<dao.leimingtech.dto.reason.AftersaleGoodsDTO>
     * @author xuzhch
     * @date 2020年09月21日
     */

    List<AftersaleGoodsDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 分页查询所有的售后商品记录列表
     *
     * @param params 查询条件
     * @return dao.leimingtech.commons.tools.page.PageData<dao.leimingtech.dto.reason.AftersaleGoodsDTO> data
     * @author xuzhch
     * @date 2020年09月21日
     */

    PageData<AftersaleGoodsDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 批量保存售后商品信息
     *
     * @param aftersaleGoodsSaveList :保存售后商品实体
     * @return boolean 保存结果
     * @author xuzhch
     * @date 2020年09月21日
     */

    boolean batchSave(@RequestBody List<AftersaleGoodsSaveDTO> aftersaleGoodsSaveList);

    /**
     * 查询商品信息
     *
     * @param map 查询条件
     * @return 售后商品信息
     * @author xuzhch
     * @date 2020年09月21日
     */

    AftersaleGoodsSaveDTO getAfterGoods(@RequestParam Map<String, Object> map);
}

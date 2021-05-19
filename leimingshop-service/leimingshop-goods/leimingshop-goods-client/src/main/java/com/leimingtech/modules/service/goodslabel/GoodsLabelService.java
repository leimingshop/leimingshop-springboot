/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodslabel;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.goodslable.GoodsLabelDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelFindDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelSaveDTO;
import com.leimingtech.modules.dto.goodslable.GoodsLabelUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: 商品标签GoodsLabelService
 * @Date :2019/5/20 14:17
 * @Version V1.0
 **/

public interface GoodsLabelService {
    /**
     * 根据id查询标签信息
     *
     * @param id 主键id
     * @return 返回标签信息
     * @Author: weixianchun
     * @Description: 根据id查询标签信息
     * @Date :2019/5/29 14:59
     * @Version V1.0
     **/

    GoodsLabelDTO get(Long id);

    /**
     * 标签, 分组回显
     *
     * @param id 主键id
     * @return 返回标签信息
     * @Author weixianchun
     * @Description 标签, 分组回显
     * @Date 2019/12/12 20:12
     * @version 1.0
     */

    GoodsLabelFindDTO findByLabelId(Long id);

    /**
     * 保存标签信息
     *
     * @param dto 保存参数
     * @Author: weixianchun
     * @Description: 保存标签信息
     * @Date :2019/5/29 14:59
     * @Version V1.0
     **/

    void save(@RequestBody GoodsLabelSaveDTO dto);

    /**
     * 修改标签信息
     *
     * @param dto 修改参数
     * @Author: weixianchun
     * @Description: 修改标签信息
     * @Date :2019/5/29 14:59
     * @Version V1.0
     **/

    void update(@RequestBody GoodsLabelUpdateDTO dto);

    /**
     * 根据id删除标签信息
     *
     * @param ids 主键id
     * @Author: weixianchun
     * @Description: 根据id删除标签信息
     * @Date :2019/5/29 15:00
     * @Param ids
     * @Version V1.0
     **/

    void delete(@RequestBody Long[] ids);

    /**
     * 分页查询标签
     *
     * @param params 查询条件
     * @return 返回标签信息
     * @Author: weixianchun
     * @Description: 分页
     * @Date :2019/5/29 15:00
     * @Version V1.0
     **/

    PageData<GoodsLabelDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有标签信息
     *
     * @param params 查询条件
     * @return 返回标签信息
     * @Author: weixianchun
     * @Description: 查询所有标签信息
     * @Date :2019/5/29 15:01
     * @Version V1.0
     **/

    List<GoodsLabelDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 修改时校验标签名称是否重复
     *
     * @param labelUpdateDTO 查询条件
     * @return 返回标签名称数量
     * @Author weixianchun
     * @Description 修改时校验标签名称是否重复
     * @Date 2019/12/11 15:02
     * @version 1.0
     */

    int checkLabelNameUpate(@RequestBody GoodsLabelUpdateDTO labelUpdateDTO);

    /**
     * 保存时校验标签名称是否重复
     *
     * @param labelName 标签名称
     * @return 返回标签名称数量
     * @Author weixianchun
     * @Description 保存时校验标签名称是否重复
     * @Date 2019/12/11 15:02
     * @version 1.0
     */

    int checkLabelNameSave(@RequestParam("labelName") String labelName);

    /**
     * 批量保存标签信息
     *
     * @param dtoList 保存参数
     * @return 返回是否保存成功
     * @Author weixianchun
     * @Description 批量保存标签信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    boolean insertBatch(@RequestBody List<GoodsLabelSaveDTO> dtoList);

    /**
     * 功能描述 根据标签 查找id
     *
     * @param goodsLabel 标签名
     * @return java.lang.Long id
     * @author lishuo
     * @date 24/6/2020
     */

    Long findByLabelName(@RequestParam("goodsLabel") String goodsLabel);

    /**
     * 功能描述 根据Id 查找标签名字
     *
     * @param goodsId Id
     * @return java.lang.String 标签名字
     * @author lishuo
     * @date 27/6/2020
     */

    String findByGoodsId(@RequestParam("goodsId") Long goodsId);
}

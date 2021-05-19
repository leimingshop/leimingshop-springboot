/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.adv;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.adv.AdvDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 广告管理
 *
 * @author 刘远杰
 * @email 2634443725@gmail.com
 * @since 7.0 2019-05-13
 */

public interface AdvService {

    /**
     * 功能描述:
     * 〈admin广告分页查询〉
     *
     * @param params 查询条件
     * @return 返回广告分页信息
     * @author : 刘远杰
     */

    PageData<AdvDTO> findAdvShowList(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈根据id查询广告〉
     *
     * @param id 广告id
     * @return 返回广告详情
     * @author : 刘远杰
     */

    AdvDTO findAdvById(Long id);

    /**
     * 功能描述:
     * 〈查询展示中的广告〉
     *
     * @param params 查询条件
     * @return 返回广告信息
     * @author : 刘远杰
     */

    List<AdvDTO> findIngAdv(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈前端展示广告查询〉
     *
     * @param params 查询条件
     * @return 返回广告信息
     * @author : 刘远杰
     */

    List<AdvDTO> listIngAdv(@RequestParam Map<String, Object> params);

    /**
     * 功能描述:
     * 〈保存广告〉
     *
     * @param dto 广告实体
     * @return : java.util.Map<java.lang.String,java.lang.Object>
     * @author : 刘远杰
     */

    Map<String, Object> saveAdv(@RequestBody AdvDTO dto);

    /**
     * 功能描述:
     * 〈更新广告〉
     *
     * @param dto 广告实体
     * @return : void
     * @author : 刘远杰
     */

    void updateAdv(@RequestBody AdvDTO dto);

    /**
     * 功能描述:
     * 〈广告逻辑删除〉
     *
     * @param ids
     * @return 返回删除成功数量
     * @author : 刘远杰
     */

    Integer logicDeleteAdv(@RequestBody Long[] ids);

    /**
     * 根据展示类目ID查询广告信息
     *
     * @param gcId: 展示类目ID
     * @return 广告位集合
     * @date 2019/7/15 21:14
     * @author lixiang
     **/

    List<AdvDTO> findByGcId(Long gcId);

    /**
     * 查询店铺商品分类广告
     *
     * @return
     */

    void stoeGoodsClassAdv();

    /**
     * 查询已关联的pc展示分类
     *
     * @return
     */

    List<Long> relevanceClass();
}

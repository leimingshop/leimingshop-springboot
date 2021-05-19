/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.goodslabel;


import com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO;
import com.leimingtech.modules.dto.goodslable.LabelGroupRelSaveDTO;
import com.leimingtech.modules.dto.goodslable.LabelNumDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 标签与标签分组关联管理
 *
 * @author weixianchun
 * @since v1.0.0 2019-12-09
 */

public interface LabelGroupRelService {

    /**
     * 查询所有关联信息
     *
     * @param params 查询参数
     * @return java.util.List<com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO>
     * @Author weixianchun
     * @Description 查询所有关联信息
     * @Date 2019/12/11 10:00
     * @version 1.0
     */

    List<LabelGroupRelDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 批量保存关联信息
     *
     * @param dtoList 保存参数
     * @return 是否保存成功
     * @Author weixianchun
     * @Description 批量保存关联信息
     * @Date 2019/12/11 9:26
     * @version 1.0
     */

    boolean insertBatch(@RequestBody List<LabelGroupRelSaveDTO> dtoList);

    /**
     * 根据分组ID删除数据
     *
     * @param groupId 分组id
     * @return 返回删除成功数量
     * @Author weixianchun
     * @Description 根据分组ID删除数据
     * @Date 2019/12/12 10:43
     * @version 1.0
     */

    int deleteByGroupId(Long groupId);

    /**
     * 根据标签ID删除数据
     *
     * @param labelId 标签id
     * @return 返回删除成功数量
     * @Author weixianchun
     * @Description 根据标签ID删除数据
     * @Date 2019/12/12 10:43
     * @version 1.0
     */

    int deleteByLabelId(Long labelId);

    /**
     * 根据分组ID查询标签数量
     *
     * @param groupId 分组id
     * @return com.leimingtech.modules.dto.goodslable.LabelGroupRelDTO
     * @Author weixianchun
     * @Description 根据分组ID查询标签数量
     * @Date 2019/12/11 12:08
     * @version 1.0
     */

    LabelNumDTO findLabelNum(@RequestParam("groupId") Long groupId);
}

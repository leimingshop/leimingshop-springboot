/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.group;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.group.GroupDTO;

import java.util.List;
import java.util.Map;

/**
 * 拼团
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */

public interface GroupService {

    /**
     * 分页查询拼团活动列表
     *
     * @param params
     * @return
     */

    PageData<GroupDTO> page(Map<String, Object> params);

    /**
     * 分页查询拼团活动列表
     *
     * @param params 查询参数
     * @return
     */

    PageData<GroupDTO> sellerPage(Map<String, Object> params);

    /**
     * 查询所有拼团活动列表
     *
     * @param params
     * @return
     */

    List<GroupDTO> list(Map<String, Object> params);

    /**
     * 分页查询拼团活动详情
     *
     * @param
     * @return
     */

    GroupDTO get(Long id);

    /**
     * 新增保存拼团活动
     *
     * @param
     * @return
     */

    Boolean save(GroupDTO dto);

    /**
     * 编辑拼团活动
     *
     * @param
     * @return
     */

    Boolean update(GroupDTO dto);

    /**
     * 支付完成后修改拼团数据
     *
     * @param dto: 修改的拼团信息
     * @date 2020/4/8 15:41
     * @author lixiangx@leimingtech.com
     **/

    void updateByPayEnd(GroupDTO dto);

    /**
     * 删除拼团活动列表
     *
     * @param
     * @return
     */

    Boolean delete(Long[] ids);

    /**
     * 拼团活动定时开始
     *
     * @param time 时间
     * @return
     * @date 2020-03-12 10:30
     * @author huangkeyuan@leimingtech.com
     **/

    void startGroupActivityTiming(Long time);

    /**
     * 拼团活动定时结束
     *
     * @param time 时间
     * @return
     * @date 2020-03-12 10:42
     * @author huangkeyuan@leimingtech.com
     **/

    void stopGroupActivityTiming(Long time);

    /**
     * 拼团活动定时预热开始
     *
     * @param time 时间
     * @return
     * @date 2020-03-16 10:30
     * @author huangkeyuan@leimingtech.com
     **/

    void preheatGroupActivityTiming(Long time);

    /**
     * 根据活动id和店铺id进行查询
     *
     * @param activityId: 活动ID
     * @param storeId:    店铺ID
     * @return 拼团活动实体
     * @date 2020-03-16 10:30
     * @author huangkeyuan@leimingtech.com
     */

    GroupDTO findByIdAndStoreId(Long activityId,
                                Long storeId);

    /**
     * 停止拼团活动
     *
     * @param id      拼团id
     * @param storeId 店铺id
     * @return
     * @date 2020-04-07 17:01
     * @author huangkeyuan@leimingtech.com
     **/

    Boolean stop(Long id,
                 Long storeId);
}

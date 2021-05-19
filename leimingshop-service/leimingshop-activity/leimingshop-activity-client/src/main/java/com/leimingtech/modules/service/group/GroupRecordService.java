/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.group;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.group.GroupRecordDTO;
import com.leimingtech.modules.dto.group.GroupRecordOrderDTO;
import com.leimingtech.modules.dto.group.H5GroupDetailDTO;

import java.util.List;
import java.util.Map;


/**
 * 拼团记录
 *
 * @author keyuan huangkeyuan@leimingtech.com
 * @since v1.0.0 2020-03-09
 */

public interface GroupRecordService {


    PageData<GroupRecordDTO> page(Map<String, Object> params);


    List<GroupRecordDTO> list(Map<String, Object> params);


    GroupRecordDTO get(Long id);


    void save(GroupRecordDTO dto);


    void update(GroupRecordDTO dto);


    void delete(Long[] ids);

    /**
     * 查询拼团记录详情
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团活动记录id
     * @return
     * @date 2020-03-17 11:09
     * @author huangkeyuan@leimingtech.com
     **/

    GroupRecordOrderDTO getGroupOrderDetail(Long storeId, Long groupRecordId);

    /**
     * (拼团中)快速成团
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团记录id
     * @return
     * @date 2020-03-17 17:13
     * @author huangkeyuan@leimingtech.com
     **/

    Boolean groupComposition(Long storeId, Long groupRecordId);

    /**
     * 拼团成团超时定时任务
     *
     * @param time
     * @return
     * @date 2020-03-30 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    void overTimeGroupActivityTiming(Long time);

    /**
     * 取消订单
     *
     * @param storeId       店铺id
     * @param groupRecordId 拼团记录id
     * @param reasonId      取消订单原因id
     * @return
     * @date 2020-03-18 15:48
     * @author huangkeyuan@leimingtech.com
     **/

    Boolean cancelOrder(Long storeId, Long groupRecordId, Long reasonId);

    /**
     * 查询用户参加拼团活动的次数
     *
     * @param memberId 会员id
     * @param groupId  拼团活动id
     * @return
     * @date 2020-03-20 15:03
     * @author huangkeyuan@leimingtech.com
     **/

    List<GroupRecordDTO> getMemberRecord(Long memberId, Long groupId, Long goodsId);

    /**
     * h5查询拼团记录详情
     *
     * @param memberId      用户id
     * @param groupRecordId 拼团活动记录id
     * @return
     * @date 2020-03-17 11:09
     * @author huangkeyuan@leimingtech.com
     **/

    H5GroupDetailDTO getH5GroupOrderDetail(Long groupRecordId, Long memberId);

    /**
     * h5查询拼团列表
     *
     * @param params 拼团活动id，商品spuid，拼团状态
     * @return
     * @date 2020-03-30 15:00
     * @author huangkeyuan@leimingtech.com
     **/

    List<GroupRecordDTO> groupList(Map<String, Object> params);

    /**
     * 更新涉及的拼团记录
     *
     * @param groupIds 需要更新的拼团id集合
     * @return
     * @date 2020-04-07 15:33
     * @author huangkeyuan@leimingtech.com
     **/

    void updateRecord(List<Long> groupIds);
}

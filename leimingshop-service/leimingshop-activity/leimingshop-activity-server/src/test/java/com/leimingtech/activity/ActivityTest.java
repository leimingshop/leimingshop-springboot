/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.activity;

import com.leimingtech.modules.service.coupons.CouponsActivityService;
import com.leimingtech.modules.service.group.GroupRecordService;
import com.leimingtech.modules.service.group.GroupService;
import com.leimingtech.modules.service.seckill.SeckillActivitySearchService;
import com.leimingtech.modules.service.seckill.SeckillActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @ClassName ActivityTest
 * @Description
 * @Author huangkeyuan
 * @Date 2020-03-04 16:41
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityTest {

    @Resource
    private GroupService groupService;

    @Resource
    private GroupRecordService groupRecordService;

    @Resource
    private SeckillActivitySearchService seckillActivitySearchService;

    @Resource
    private SeckillActivityService seckillActivityService;

    @Resource
    private CouponsActivityService couponsActivityService;


    @Test
    public void test() {

//        Long time = System.currentTimeMillis();
//        groupService.stopGroupActivityTiming(time);
//        List<Long>  groupIds = new ArrayList<>();
//        groupIds.add(123445L);
//        groupRecordService.updateRecord(groupIds);

//        groupService.stop(1248527656830418944L, 1199222428123791362L);
        // 测试拼团超时，是否模拟成团和取消
//        Long time = System.currentTimeMillis();
//        groupRecordService.overTimeGroupActivityTiming(time);

        // 测试获取拼团详情
//        groupRecordService.getH5GroupOrderDetail(1248517634692005888L,1234408545600933889L);

        // 测试拼团推荐列表
//        Map<String, Object> queryMap = new HashMap<>();
//        queryMap.put("goodsId",1247692821947781122L);
//        queryMap.put("groupId",1249516105259597824L);
//        queryMap.put("groupStatus", 0);
//        queryMap.put("memberId", 1191648973471985665L);
//        groupRecordService.groupList(queryMap); 1249962023909900289L

        //1249980426603249665

//        H5GroupDetailDTO h5GroupDetailDTO = groupRecordService.getH5GroupOrderDetail(1249962023909900289L, 1191648973471985665L);

        // 测试秒杀场次
//        List<PrestartSeckillSessionDTO> prestartSeckillSessionList = seckillActivitySearchService.prestartSeckillSessionList();

        // 查询即将进行秒杀场次
//        Map<String, Object> queryMap = new HashMap<>();
//        queryMap.put("page","1");
//        queryMap.put("limit","10");
//        queryMap.put("sessionId","1296114703738634246");
//        PageData<PrestartSeckillGoodsPageDTO> page = seckillActivitySearchService.seckillGoodsPage(queryMap, 1234408545600933889L);

//        // 保存活动
//        SeckillActivityDTO dto = seckillActivityService.get(1298140135820390402L);
//        dto.setSessionId(1296114703738634241L);
//        Boolean flag = seckillActivityService.editSeckillActivity(dto);

        // 领取优惠券 1306050351700426752
        couponsActivityService.receivedCoupons(1306050351700426752L, 1191648973471985665L);

    }

}

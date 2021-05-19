/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.order;

import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.DataMaskingUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.commons.tools.utils.ShareCodeUtils;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.modules.service.order.OrderGoodsService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.vo.order.PCOrderPageVO;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户测试类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/11/11 10:30
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberTest {

    @Resource
    private RabbitMqSendService rabbitMqSendService;

    @Resource
    private OrderService orderService;

    @Resource
    private OrderGoodsService orderGoodsService;

    @Autowired
    private NosqlService nosqlService;

//    @Autowired
//    private OrderGoodsService orderGoodsService;

    @Test
    public void test() {
        // 测试

//        Map<String, Object> memberMap = new HashMap<>(10);
//        memberMap.put("memberId", 1192710996511174657L);
//        memberMap.put("lastLoginDate", new Date());
//        memberMap.put("lastLoginIp", "192.168.1.10");
//        memberMap.put("memberLoginIp", "192.168.1.10");
//        memberMap.put("memberLoginTime", new Date());
//        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE, JSON.toJSONString(memberMap));

//        Map<String, Object> groupMap = new HashMap<>();
//        groupMap.put("groupRecordId",1249966024491581441L);
//        groupMap.put("storeId",1191626469961400321L);
//        groupMap.put("orderStatus", OrderStatusEnum.WAITTING_SHIPPED.value());
////        log.info("当前记录是否有其他订单，groupMap:{}",groupMap);
//        List<GroupOrderDetailDTO> groupOrderList = orderService.findGroupOrderList(groupMap);

//        GoodsDTO goodsDTO = goodsService.get(1261135936213753857L);
//        List<OrderGoodsDTO> orderGoodsDTOList = orderGoodsService.getByPaySn(1261553605725437953L);
//        UpdateOrderDTO updateOrderDTO = new UpdateOrderDTO();
//        updateOrderDTO.setPaymentId(1261585183180636160L);
//        updateOrderDTO.setPaymentName("weixin");
//        updateOrderDTO.setPaySn(1261585183176441857L);
//        updateOrderDTO.setTransactionId("4200000563202005167877437695");
//        orderService.updateOrderStatePayFinish(updateOrderDTO);
        Map<String, Object> groupMap = new HashMap<>();
        groupMap.put("page", "1");
        groupMap.put("limit", "10");
        groupMap.put("keyWord", "1282583578405814272");
        PageData<PCOrderPageVO> pagePC = orderService.pagePC(groupMap, 1234408545600933889L);

        // 从mongodb中查询数据
//        Map<String, Object> map = new HashMap<>();
//        map.put("_id", "YT4494550315053");
//        List<String> logistics_message = nosqlService.queryData("logistics_message", map);
//            OrderLogisticsDTO orderLogisticsDTO = JSONObject.parseObject(logistics_message.get(0), OrderLogisticsDTO.class);
//
//
//        List<LogisticsProcessDTO> logisticsProcessDTOList = orderLogisticsDTO.getData();
//        Map<String, List<LogisticsProcessDTO>> listLinkedHashMap = new LinkedHashMap<>();
//        for (LogisticsProcessDTO logisticsProcessDTO : logisticsProcessDTOList) {
//            String time = logisticsProcessDTO.getFtime();
//            String weekname = this.getWeek(time);
//            String finalDate =  time.substring(0,10);
//            finalDate = finalDate + " "+ weekname;
//            if (listLinkedHashMap.containsKey(finalDate)) {
//                listLinkedHashMap.get(finalDate).add(logisticsProcessDTO);
//            }else {
//                List<LogisticsProcessDTO> logisticsProcessDTOList1 = new ArrayList<>();
//                logisticsProcessDTOList1.add(logisticsProcessDTO);
//                listLinkedHashMap.put(finalDate,logisticsProcessDTOList1);
//            }
//
//        }
//
//        System.out.println(listLinkedHashMap);


//        PcOrderDetailVO pcOrderDetailVO = orderService.find (1269088454405308416L, 1234408545600933889L);
//        OrderCancelDTO orderCancelDTO = new OrderCancelDTO();
//        orderCancelDTO.setReasonId(1191657044986335233L);
//        orderCancelDTO.setId(1282493327964422144L);
//        orderService.cancelOrderSeller(orderCancelDTO, 1199222428123791362L);

//        orderService.findPCOrderDetail(1282584072912125954L,1234408545600933889L);

    }


    public String getWeek(String dates) {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = f.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        cal.setTime(d);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;

        String week = null;
        switch (w) {
            case 0:
                week = "周日";
                break;
            case 1:
                week = "周一";
                break;
            case 2:
                week = "周二";
                break;
            case 3:
                week = "周三";
                break;
            case 4:
                week = "周四";
                break;
            case 5:
                week = "周五";
                break;
            case 6:
                week = "周六";
                break;

            default:
                break;
        }
        return week;

    }

    // 测试确认收货方法积分增加是否正常
    @Test
    public void testPoint() {
        String codeNumber2 = DataMaskingUtils.idEncrypt("wertvfgtyhgty");
        String codeNumber = ShareCodeUtils.idToCode(IdGenerator.defaultSnowflakeId());

//        orderService.confirmReceipt(1234492075611832320L,1234408545600933889L);
    }
}

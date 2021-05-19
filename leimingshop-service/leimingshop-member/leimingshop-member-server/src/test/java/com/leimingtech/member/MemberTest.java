/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.member;

import com.leimingtech.modules.service.log.point.PointLogService;
import com.leimingtech.modules.service.member.MemberBankAccountService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
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
    private MemberService memberService;

    @Resource
    private PointLogService pointLogService;

    @Resource
    private MemberBankAccountService memberBankAccountService;

    @Test
    public void test() {
//        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE, "1192710996511174657");
        // 测试新手奖励
//        Map<String, Object> params = new HashMap<>();
//        params.put("memberId", 1234L);
//        params.put("memberName", "18601320411");
//        params.put("sourceType", PointLogEnum.WELCOME_SOURCE_TYPE.value());
//        params.put("pointDesc", PointLogDescEnum.WELCOME_SOURCE_DESC.value());
//        memberService.savePoint(params);

        // 测试修改昵称
//        MemberDTO memberDTO = new MemberDTO();
//        memberDTO.setId(1192326496660873218L);
//        memberDTO.setNickName("kekekekkeke");
//        memberService.updatememberById(memberDTO);

//        // 测试登录成功
        Map<String, Object> memberParams = new HashMap<>();
//        memberParams.put("memberId",1192326496660873218L);
//        memberParams.put("memberName","xxx");
//        memberParams.put("sourceType", PointLogEnum.LOGIN_SOURCE_TYPE.value());
//        memberParams.put("pointDesc", PointLogDescEnum.LOGIN_SOURCE_DESC.value());
//        memberParams.put("repeat", 1);
//        memberService.savePoint(memberParams);

//        memberParams.put("memberId",1216539338385453058L);
//        memberParams.put("sourceType", PointLogEnum.LOGIN_SOURCE_TYPE.value());
//        // 获取当前日期的0点时间
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        Date zero = calendar.getTime();
//        memberParams.put("endTime", new Date());
//        memberParams.put("startTime", zero);

//        List<PointLogDTO> pointLogDTOList = pointLogService.queryByTime(memberParams);
//        if (CollectionUtils.isEmpty(pointLogDTOList)){
//            Map<String , Object> timeParams = new HashMap<>();
//            timeParams.put("memberId",1216539338385453058L);
//            timeParams.put("memberName","xxx");
//            timeParams.put("sourceType", PointLogEnum.LOGIN_SOURCE_TYPE.value());
//            timeParams.put("pointDesc", PointLogDescEnum.LOGIN_SOURCE_DESC.value());
//            timeParams.put("repeat", 1);
//            memberService.savePoint(timeParams);
//        }
    }

    @Test
    public void testMemberinfo() {
//        MemberVoDTO memberVoDTO = memberService.selectMemberById(1216539338385453058L);
//        System.out.println(memberVoDTO);
//        6217000010039790343
        String bankName = memberBankAccountService.getBankName("6217");
        System.out.println(bankName);
    }

    @Test
    public void testAvatar() {
//        Map<String, Object> params = new HashMap<>();
//        params.put("memberId", 1216539338385453058L);
//        params.put("memberName", "18601320411");
//        params.put("sourceType", PointLogEnum.AVATAR_SOURCE_TYPE.value());
//        params.put("pointDesc", PointLogDescEnum.AVATAR_SOURCE_DESC.value());
//        memberService.savePoint(params);
    }

    @Test
    public void testFirstOrder() {
        // 首次下单奖励积分和成长值
//        Map<String, Object> memberParams = new HashMap<>();
//        memberParams.put("memberId", 1216539338385453058L);
//        memberParams.put("memberName", "18601320411");
//        memberParams.put("sourceType", PointLogEnum.FIRST_ORDER_SOURCE_TYPE.value());
//        memberParams.put("pointDesc", PointLogDescEnum.FIRST_ORDER_SOURCE_DESC.value());
//        memberService.savePoint(memberParams);
    }

//    @Test
//    public void testRegister(){
//        PointLogPackDTO pointLogPackDTO = new PointLogPackDTO(
//                1234363873947226114L,
//                "18601320411",
//                10,
//                10,
//                "新手欢迎奖励",
//                1,
//                PointLogEnum.INSERT_ALL.value());
//
//        pointLogService.packPointLog(pointLogPackDTO);
//    }

}

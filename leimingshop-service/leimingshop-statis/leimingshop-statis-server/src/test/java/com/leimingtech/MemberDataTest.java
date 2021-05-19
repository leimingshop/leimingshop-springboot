/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.leimingtech.modules.service.member.MemberStatisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 同步会员数据
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020年3月20日10:24:04
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberDataTest {

    @Autowired
    private MemberStatisService memberStatisService;

    /**
     * 测试单个保存索引
     *
     * @date 2020/1/10 14:40
     * @author lixiangx@leimingtech.com
     **/
    @Test
    public void syncMemberData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = new Date();
        for (int i = 1; i <= 150 * 24; i++) {
            Date reqDate = DateUtil.offsetHour(date, -i);
            String format = dateFormat.format(reqDate);
            String url = "http://localhost:30216/statis/member/member";
//            String url = "curl -H \\\"Content-Type: application/json\\\"  http://localhost:30216/statis/member/member -X POST -d '{\\\"params\\\":\\\"" + format + "\\\"}'";
//            String s = HttpUtil.get(url);
            Map<String, Object> map = new HashMap<>();
            map.put("params", format);
            String s = HttpUtil.post(url, map);
            System.out.println(url);
        }

    }
}

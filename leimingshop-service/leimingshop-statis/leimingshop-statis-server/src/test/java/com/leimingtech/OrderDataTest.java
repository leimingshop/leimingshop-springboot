/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xuzhch
 * @className OrderDataTest
 * @description 订单数据同步
 * @date 2020/3/20/020
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OrderDataTest {
    @Test
    public void syncOrderData() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 1; i < 150; i++) {
            Date dateTime = DateUtil.offsetDay(date, -i);
            String format = dateFormat.format(dateTime);
            //例子： //http://url:port/statis/goods/goods?params=2020-02-28
            //商品、交易统计以天做维度统计----> params=2019-12-20
            //会员统计以小时做维度统计例子----> params=2019-12-20 14
            //商品统计保存
            //http://localhost:30216/statis/goods/goods?params=
            //交易统计保存
            //http://localhost:30216/statis/order/save/transaction?params=
            //会员统计
            //http://localhost:30216/statis/member/member?params=
//            String s = "curl http://localhost:30216/statis/goods/goods?params=" + format;
//            String s = "curl http://localhost:30216/statis/order/save/transaction?params=" + format;
            String s = HttpUtil.get("http://localhost:30216/statis/order/save/transaction?params=" + format);
            System.out.println(s);


        }

    }
}

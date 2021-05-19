/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.after.sale;

import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.aftersale.dto.AftersaleReturnPageDTO;
import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AftersaleTest
 * @Description
 * @Author huangkeyuan
 * @Date 2020-03-04 16:41
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AftersaleTest {

    @Resource
    private AftersaleReturnService aftersaleReturnService;

    @Test
    public void test() {

//        aftersaleReturnService.confirmGoods(123L,4);

//        aftersaleReturnService.wxRefund(1281843008939016192L);
        Map<String, Object> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
        PageData<AftersaleReturnPageDTO> pageDTOPageData = aftersaleReturnService.pageData(params, 1199222428123791362L);
        pageDTOPageData.getList();
    }

}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.aftersale;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:退款服务
 * @Date: 2019/6/24 16:59
 * @Version: V1.0
 */
@RestController
@RequestMapping("refund")
@Api(tags = "退款服务管理")
public class RefundController {

    @Autowired
    private AftersaleReturnService aftersaleReturnService;

    /**
     * @param id:退款id
     * @Author: SWH ab4856812@163.com
     * @Description:微信退款
     * @Date: 2019/6/24 17:42
     * @Version: V1.0
     */
    @PutMapping(value = "wx/refund")
    @ApiOperation("微信退款")
    @LogOperation("微信退款")
    public Map<String, Object> wxRefund(@RequestParam("id") Long id, @ApiIgnore SellerDetail sellerDetail) {

        Map<String, Object> resultMap = new HashMap<>(16);
        if (null == sellerDetail || null == sellerDetail.getId()) {
            resultMap.put("code", ErrorCode.UNAUTHORIZED);
            resultMap.put("msg", "用户未登录");
            return resultMap;
        }
        resultMap = aftersaleReturnService.wxRefund(id);
        //根据返回状态修改记录状态

        return resultMap;

    }


}

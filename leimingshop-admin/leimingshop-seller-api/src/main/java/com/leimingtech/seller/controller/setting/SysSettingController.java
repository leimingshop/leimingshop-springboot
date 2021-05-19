/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.setting;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.setting.SettingSeniorDTO;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 系统设置API接口
 *
 * @author xuzhch xuzhch@leimingtech.com
 * @since v1.0.0 2020年10月21日
 */
@RestController
@RequestMapping("sys/setting")
@Api(tags = "系统设置API接口")
public class SysSettingController {
    @Autowired
    public SettingService settingService;


    @GetMapping("order/setting")
    @ApiOperation("订单有效支付时间")
    public Result<String> page(@ApiIgnore SellerDetail sellerDetail) {
        if (sellerDetail == null || sellerDetail.getStoreId() == null) {
            return new Result<String>().error(ErrorCode.UNAUTHORIZED, "用户未登录");
        }
        SettingSeniorDTO dto = settingService.getSeniorSet();
        String cancelTime = dto.getCancelOrder();
        return new Result<String>().ok(cancelTime);
    }
}

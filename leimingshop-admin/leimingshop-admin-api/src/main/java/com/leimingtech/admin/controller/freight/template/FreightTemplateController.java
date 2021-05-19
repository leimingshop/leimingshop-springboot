/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.freight.template;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.freight.template.AdminFreightTemplatePageDTO;
import com.leimingtech.modules.service.freight.template.FreightTemplateService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <运费模板管理>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/21
 */
@RestController
@RequestMapping("freight/template")
@Api(tags = "运费模板管理")
public class FreightTemplateController {

//    private Long storeId = 1196266068939710466L;

    @Autowired
    private FreightTemplateService freightTemplateService;

    @GetMapping("list")
    @ApiOperation("店铺运费模板列表")
    public Result<List<AdminFreightTemplatePageDTO>> list(@RequestParam("storeId") Long storeId) {
        // 查询店铺运费模板
        List<AdminFreightTemplatePageDTO> list = freightTemplateService.storeList(storeId);
        return new Result<List<AdminFreightTemplatePageDTO>>().ok(list, "查询成功");
    }

}

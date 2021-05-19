/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.adv;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.constants.adv.AdvResultCodeConstants;
import com.leimingtech.modules.dto.adv.AdvCategoryDTO;
import com.leimingtech.modules.enums.adv.StatusEnum;
import com.leimingtech.modules.enums.adv.SysFlagEnum;
import com.leimingtech.modules.service.adv.AdvCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 广告类别管理
 *
 * @author 刘远杰
 * @since 7.0 2019-05-13
 */
@Slf4j
@RestController
@RequestMapping("adv/category")
@Api(tags = "广告类别管理")
public class AdvCategoryController {

    @Autowired
    private AdvCategoryService advCategoryService;

    /**
     * 广告类型
     *
     * @return
     */
    @GetMapping("list/enabled")
    @ApiOperation("查询可用普通广告类别列表")
    public Result<List<AdvCategoryDTO>> listEnadbled() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("orderField", "sort");
        params.put("order", "desc");
        params.put("sysFlag", SysFlagEnum.STORE_NOMARL.value());
        params.put("status", StatusEnum.ENABLED.value());
        List<AdvCategoryDTO> advCategoryDTOList = advCategoryService.findAdvCategoryList(params);

        if (CollectionUtils.isNotEmpty(advCategoryDTOList)) {
            return new Result<List<AdvCategoryDTO>>().ok(advCategoryDTOList, "查询广告类别成功");
        } else {
            return new Result<List<AdvCategoryDTO>>().error(AdvResultCodeConstants.ERR_NO_RESULT, "查询广告类别失败");
        }
    }
}

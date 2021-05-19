/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.area;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.area.AreaAndRegionTreeDTO;
import com.leimingtech.service.area.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @Author: 刘远杰
 * @Description: 地区管理controller
 * @Date :2020/4/22
 * @Version V1.0
 **/
@RestController
@RequestMapping("area")
@Api(tags = "地区管理")
public class AreaController {

    @Autowired
    private AreaService areaService;

    @GetMapping("region/area/tree")
    @ApiOperation("大区及地区分层")
    public Result<List<AreaAndRegionTreeDTO>> findAreasByParentId() {
        List<AreaAndRegionTreeDTO> tree = areaService.regionAndAreaTree();
        return new Result<List<AreaAndRegionTreeDTO>>().ok(tree, "查询成功");
    }

}

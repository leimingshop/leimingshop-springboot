/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.icon;

import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.icon.BottomIconDTO;
import com.leimingtech.dto.icon.BottomIconUpdateDTO;
import com.leimingtech.service.icon.BottomIconService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * @author chengqian
 * @date 2019/12-9
 * @since v1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/icon")
@Api(tags = "底部icon")
public class ButtonIconController {

    @Autowired
    private BottomIconService bottomIconService;


    /**
     * 底部icon配置列表
     *
     * @param params
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("底部icon配置列表")
    public Result<List<BottomIconDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        List<BottomIconDTO> all = bottomIconService.all();
        return new Result<List<BottomIconDTO>>().ok(all, "查询icon列表成功");
    }

    /**
     * 更新icon信息
     *
     * @param updateDTO
     * @return
     */
    @PutMapping
    @ApiOperation("更新icon信息")
    public Result update(@RequestBody BottomIconUpdateDTO updateDTO) {
        //校验参数
        ValidatorUtils.validateEntity(updateDTO, UpdateGroup.class, DefaultGroup.class);

        bottomIconService.update(updateDTO);
        return new Result().ok("更新成功");
    }

}

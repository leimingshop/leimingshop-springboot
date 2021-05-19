/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.controller.member.grade;

import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.grade.MemberGradeDTO;
import com.leimingtech.modules.service.grade.MemberGradeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * 会员等级表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@RestController
@RequestMapping("membergrade")
@Api(tags = "会员等级管理")
public class MemberGradeController {
    @Autowired
    private MemberGradeService memberGradeService;

    @GetMapping("list")
    @ApiOperation("会员等级列表")
    @LogOperation("会员等级列表")
    public Result list() {
        List<MemberGradeDTO> list = memberGradeService.list(new HashMap<>(16));
        return new Result<>().ok(list, "查询成功");
    }

}

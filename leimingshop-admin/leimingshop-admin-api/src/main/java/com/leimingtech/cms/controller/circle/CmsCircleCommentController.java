/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.cms.controller.circle;

import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.AssertUtils;
import com.leimingtech.modules.dto.comment.CmsCommentDTO;
import com.leimingtech.modules.dto.comment.CmsCommentStatusUpdateDTO;
import com.leimingtech.modules.enums.articleclass.CmsEnum;
import com.leimingtech.modules.service.comment.CmsCommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


/**
 * 圈子评论管理
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@RestController
@RequestMapping("cms/circlecomment")
@Api(tags = "CMS圈子评论管理")
@Slf4j
public class CmsCircleCommentController {

    @Autowired
    private CmsCommentService cmsCommentService;

    @GetMapping("page")
    @ApiOperation("资讯评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "文章标题/内容", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "creator", value = "创建人", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "commentContent", value = "评论内容", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态标识（0：屏蔽 1：展示（默认））", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", paramType = "query", required = true, dataType = "String")
    })
    public Result<PageData<CmsCommentDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("acCode", CmsEnum.QZCODE.value());
        PageData<CmsCommentDTO> page = cmsCommentService.commentList(params);
        return new Result<PageData<CmsCommentDTO>>().ok(page);
    }

    @DeleteMapping("delete")
    @ApiOperation("删除")
    public Result delete(@RequestBody Long[] ids) {
        //效验数据
        AssertUtils.isArrayEmpty(ids, "id");
        cmsCommentService.delete(ids);
        return new Result();
    }

    @PutMapping("statusUpdate")
    @ApiOperation("评论状态修改")
    public Result statusUpdate(@RequestBody CmsCommentStatusUpdateDTO dto) {
        cmsCommentService.statusUpdate(dto);
        return new Result();
    }

}

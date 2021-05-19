/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.topic;


import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.modules.dto.topic.*;
import com.leimingtech.modules.service.topic.TopicRelService;
import com.leimingtech.modules.service.topic.TopicService;
import com.leimingtech.modules.service.webfloor.WebFloorLinkConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 专题页
 *
 * @author kuangweiguo kuangweiguo
 * @since v1.0.0 2020-11-19
 */
@RestController
@RequestMapping("topic")
@Api(tags = "专题页")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private TopicRelService topicRelService;

    @Autowired
    private WebFloorLinkConfigService webFloorLinkConfigService;

    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "topicName", value = "专题名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<TopicPageDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<TopicPageDTO> page = topicService.page(params);

        return new Result<PageData<TopicPageDTO>>().ok(page);
    }

    /**
     * 专题商品分页信息
     *
     * @param params
     * @return
     */
    @GetMapping("goods/page")
    @ApiOperation("专题商品分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "goodsName", value = "商品名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "topicId", value = "专题页id", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = "gcId", value = "分类ID", paramType = "query", dataType = "Long"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    public Result<PageData<TopicRelGoodsPageDTO>> goodsPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<TopicRelGoodsPageDTO> page = topicRelService.page(params);

        return new Result<PageData<TopicRelGoodsPageDTO>>().ok(page);
    }

    /**
     * 专题页详情
     *
     * @param id 专题页id
     * @return
     */
    @GetMapping("{id}")
    @ApiOperation("信息")
    public Result<UpdateTopicDTO> get(@PathVariable("id") Long id) {
        UpdateTopicDTO data = topicService.get(id);
        return new Result<UpdateTopicDTO>().ok(data);
    }

    /**
     * 保存专题页
     *
     * @param dto
     * @return
     */
    @PostMapping
    @ApiOperation("保存")
    public Result save(@RequestBody SaveTopicDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, AddGroup.class, DefaultGroup.class);

        topicService.save(dto);

        return new Result().ok(null, "保存成功");
    }

    /**
     * 修改专题页
     *
     * @param dto 修改实体
     * @return
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result update(@RequestBody UpdateTopicDTO dto) {
        //效验数据
        ValidatorUtils.validateEntity(dto, UpdateGroup.class);

        topicService.update(dto);

        return new Result().ok(null, "修改成功");
    }

    /**
     * 删除专题页
     *
     * @param id 主键id
     * @return
     */
    @PostMapping("/del")
    @ApiOperation("删除")
    public Result delete(@RequestParam("id") Long id) {
        Integer count = webFloorLinkConfigService.findFloorNumByTopicId(id);

        if (count > 0) {
            return new Result().error("当前专题页已关联楼层，请解除关联");
        }

        topicService.delete(id);

        return new Result().ok(null, "删除成功");
    }

    /**
     * 查询所有专题页
     *
     * @return
     */
    @GetMapping("all")
    @ApiOperation("查询所有专题页")
    public Result<List<TopicDTO>> getAll() {

        List<TopicDTO> list = topicService.list(new HashMap<>(1));

        return new Result<List<TopicDTO>>().ok(list);
    }


}
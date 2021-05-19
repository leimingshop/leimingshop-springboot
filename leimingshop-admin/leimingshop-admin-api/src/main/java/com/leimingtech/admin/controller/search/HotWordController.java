/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.admin.controller.search;


import com.leimingtech.commons.tools.annotation.LogOperation;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.commons.tools.validator.ValidatorUtils;
import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import com.leimingtech.dto.hotword.HotWordDTO;
import com.leimingtech.dto.hotword.HotWordSaveDTO;
import com.leimingtech.dto.hotword.HotWordUpdateDTO;
import com.leimingtech.service.search.HotWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 热词
 *
 * @author qin
 * @email 49636174@qq.com
 * @since 1.0.0 2019-12-3
 */
@RestController
@RequestMapping("hotword")
@Api(tags = "热词管理")
public class HotWordController {

    @Autowired
    private HotWordService hotWordService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 分页查询
     *
     * @param params
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页查询")
    @LogOperation("分页查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "hotWord", value = "根据热词搜索", paramType = "query", required = false, dataType = "String")
    })
    public Result<PageData<HotWordDTO>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        PageData<HotWordDTO> page = hotWordService.page(params);

        return new Result<PageData<HotWordDTO>>().ok(page);
    }

    /**
     * 新增热词
     *
     * @param hotWord
     * @return
     */
    @PostMapping
    @ApiOperation("新增热词")
    @LogOperation("新增热词")
    public Result save(@RequestBody HotWordSaveDTO hotWord) {
        //  效验数据
        ValidatorUtils.validateEntity(hotWord, AddGroup.class);
        hotWordService.saveHotWord(hotWord);

        // 缓存设置
        this.packHotWordRedisData();
        return new Result().ok(null, "保存成功");
    }


    /**
     * 删除热搜
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除热搜")
    @LogOperation("删除热搜")
    public Result delete(@RequestBody Long[] ids) {
        hotWordService.deleteById(ids);

        // 缓存设置
        this.packHotWordRedisData();

        return new Result().ok(null, "删除成功");
    }

    /**
     * 编辑回显
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("编辑回显")
    @LogOperation("编辑回显")
    public Result selectById(@PathVariable("id") Long id) {
        HotWordDTO hotWordDto = hotWordService.selectById(id);
        return new Result<HotWordDTO>().ok(hotWordDto);
    }

    /**
     * 修改热搜实体
     *
     * @param hotWord
     * @return
     */
    @PutMapping
    @ApiOperation("修改热搜实体")
    @LogOperation("修改热搜实体")
    public Result updateHotWord(@RequestBody HotWordUpdateDTO hotWord) {
        //  效验数据
        ValidatorUtils.validateEntity(hotWord, UpdateGroup.class);

        hotWordService.updateHotWord(hotWord);

        // 缓存设置
        this.packHotWordRedisData();
        return new Result().ok(null, "修改成功");
    }

    /**
     * 热词同步至Redis中
     *
     * @date 2019/12/18 9:53
     * @author lixiangx@leimingtech.com
     **/
    public void packHotWordRedisData() {
        // 清除Redis
        redisUtils.delete(RedisKeys.getFrontHotWords());
        // 查询前15条热词数据
        List<HotWordDTO> hotWordDTOList = hotWordService.findHotFixedQuantity();
        // 设置Redis缓存
        redisUtils.set(RedisKeys.getFrontHotWords(), hotWordDTOList);
    }
}

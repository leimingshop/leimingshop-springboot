/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.browse.controller;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.modules.dto.browse.FindGoodsBrowseDTO;
import com.leimingtech.modules.dto.browse.MqGoodsBrowseDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.service.browse.GoodsBrowseService;
import com.leimingtech.moudle.member.browse.code.BrowseCode;
import com.leimingtech.moudle.member.browse.vo.BrowseGoodsPcVo;
import com.leimingtech.moudle.member.browse.vo.BrowsePcVo;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.security.SecurityCurrentUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户足迹记录管理
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
@RestController
@RequestMapping("member/browse")
@Api(tags = "用户足迹记录管理")
public class MemberBrowseController {

    @Autowired
    private GoodsBrowseService goodsBrowseService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;


    /**
     * 分页查询
     *
     * @param params 查询参数
     * @return
     */
    @GetMapping("page")
    @ApiOperation("分页")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", defaultValue = "1", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", defaultValue = "10", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.ORDER_FIELD, value = "排序字段", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "createTime", value = "浏览时间", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = Constant.ORDER, value = "排序方式，可选值(asc、desc)", paramType = "query", dataType = "String")
    })
    @LogContext(code = BrowseCode.BROWSE_PAGE_CODE, note = BrowseCode.BROWSE_PAGE_DESC)
    public Result<PageData<BrowsePcVo>> page(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<PageData<BrowsePcVo>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
        params.put("memberId", memberId.toString());
        PageData<FindGoodsBrowseDTO> page = goodsBrowseService.pageList(params);
        PageData<BrowsePcVo> pcVoPageData = new PageData<>();
        if (CollectionUtils.isNotEmpty(page.getList())) {
            List<BrowsePcVo> browsePcVos = ConvertUtils.sourceToTarget(page.getList(), BrowsePcVo.class);
            pcVoPageData.setList(browsePcVos);
            pcVoPageData.setTotal(page.getTotal());
        }
        return new Result<PageData<BrowsePcVo>>().ok(pcVoPageData);
    }

    @GetMapping("list")
    @ApiOperation("个人中心足迹数据")
    @LogContext(code = BrowseCode.BROWSE_LIST_CODE, note = BrowseCode.BROWSE_LIST_DESC)
    public Result<List<BrowseGoodsPcVo>> list() {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result<List<BrowseGoodsPcVo>>().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        // 获取memberId
        Long memberId = SecurityCurrentUser.getUserDetail().getId();
//        Long memberId = 1242275400738975746L;
        Map<String, Object> params = new HashMap<>(3);
        params.put("page", 1);
        params.put("limit", 12);
        params.put("memberId", memberId.toString());
        List<MqGoodsBrowseDTO> dtoList = goodsBrowseService.pcList(params);

        List<BrowseGoodsPcVo> browseGoodsPcVos = ConvertUtils.sourceToTarget(dtoList, BrowseGoodsPcVo.class);
        return new Result<List<BrowseGoodsPcVo>>().ok(browseGoodsPcVos);
    }

    /**
     * 新增浏览记录
     *
     * @param specId 规格ID
     * @return
     */
    @PostMapping
    @ApiOperation("新增浏览记录")
    @LogContext(code = BrowseCode.BROWSE_SAVE_CODE, note = BrowseCode.BROWSE_SAVE_DESC)
    public Result save(@RequestParam Long specId, HttpServletRequest request) {

        MqGoodsBrowseDTO goodsBrowseDTO = new MqGoodsBrowseDTO();
        if (SecurityCurrentUser.userIsLogin()) {
            // 获取用户信息
            MemberDTO userDetail = SecurityCurrentUser.getUserDetail();
            goodsBrowseDTO.setMemberId(userDetail.getId());
            goodsBrowseDTO.setCreator(userDetail.getMemberName());
            goodsBrowseDTO.setUpdater(userDetail.getMemberName());
        }
        goodsBrowseDTO.setIp(IpUtils.getIpAddr(request));
        goodsBrowseDTO.setGoodsSpecId(specId);

        String message = JSONObject.toJSONString(goodsBrowseDTO);
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_BROWSE_QUEUE, message);
        return new Result().ok(null, "保存成功");
    }

    /**
     * 删除浏览记录
     *
     * @param ids 主键ID数组
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除浏览记录(ps:不传ID为清空浏览记录)")
    @LogContext(code = BrowseCode.BROWSE_DETELE_CODE, note = BrowseCode.BROWSE_DETELE_DESC)
    public Result delete(@RequestBody(required = false) Long[] ids) {
        if (!SecurityCurrentUser.userIsLogin()) {
            return new Result().error(ErrorCode.UNAUTHORIZED, "请先登录");
        }
        goodsBrowseService.delete(ids, null, SecurityCurrentUser.getUserDetail().getId());

        return new Result().ok(null, "删除成功");
    }
}

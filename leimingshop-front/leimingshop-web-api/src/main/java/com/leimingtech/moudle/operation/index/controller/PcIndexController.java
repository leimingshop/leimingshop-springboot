/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.operation.index.controller;

import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.enums.ResultCodeEnum;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.dto.hotword.HotWordDTO;
import com.leimingtech.dto.setting.SettingUpdateDTO;
import com.leimingtech.modules.dto.adv.AdvDTO;
import com.leimingtech.modules.dto.article.CmsArticleFwznFrontDTO;
import com.leimingtech.modules.dto.article.CmsArticleFwznListDTO;
import com.leimingtech.modules.dto.articleclass.CmsClassTreeListDTO;
import com.leimingtech.modules.dto.custom.GoodsClassCustomListDTO;
import com.leimingtech.modules.dto.goods.GoodsRecommendDTO;
import com.leimingtech.modules.dto.navigation.NavigationDTO;
import com.leimingtech.modules.dto.recommend.CartRecommendPageDTO;
import com.leimingtech.modules.dto.topic.TopicInfoDTO;
import com.leimingtech.modules.dto.webfloor.WebFloorAndLinkDTO;
import com.leimingtech.modules.enums.articleclass.CmsEnum;
import com.leimingtech.modules.enums.custom.ShowClassEnum;
import com.leimingtech.modules.enums.webfloor.IsShowEnum;
import com.leimingtech.modules.service.adv.AdvService;
import com.leimingtech.modules.service.article.CmsArticleService;
import com.leimingtech.modules.service.articleclass.CmsClassService;
import com.leimingtech.modules.service.cache.CacheService;
import com.leimingtech.modules.service.custom.GoodsClassCustomService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.navigation.NavigationService;
import com.leimingtech.modules.service.recommend.CartRecommendService;
import com.leimingtech.modules.service.topic.TopicService;
import com.leimingtech.moudle.operation.index.code.IndexCode;
import com.leimingtech.moudle.operation.index.vo.*;
import com.leimingtech.security.SecurityCurrentUser;
import com.leimingtech.service.search.HotWordService;
import com.leimingtech.service.setting.SettingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * pc首页接口
 *
 * @author chengqian
 * @version V1.0
 * @date 2020/5/9 10:09
 **/
@Slf4j
@RestController
@RequestMapping("operation/index")
@Api(tags = "pc首页接口")
public class PcIndexController {
    private static final Integer MAX_INDEX_SIZE = 15;

    private final String MAP_KEY_ADV_KEY = "advKey";
    private final String PARAM_PC_INDEX = "pcIndex";
    private final String PARAM_PC_STORE_INDEX = "pcStoreIndex";

    @Autowired
    private SettingService settingService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AdvService advService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private GoodsClassCustomService goodsClassCustomService;

    @Autowired
    private HotWordService hotWordService;

    @Autowired
    private CartRecommendService cartRecommendService;

    @Autowired
    private CmsClassService cmsClassService;

    @Autowired
    private CmsArticleService cmsArticleService;

    @Autowired
    private NavigationService navigationService;


    @Autowired
    private TopicService topicService;

    /**
     * 获取网站设置的系统设置的站点信息
     */
    @GetMapping("site")
    @ApiOperation("获取网站设置的系统设置的站点信息")
    @LogContext(code = IndexCode.INDEX_SITE_CODE, note = IndexCode.INDEX_SITE_GOODS_DESC)
    public Result<SiteVO> getSite() {

        SettingUpdateDTO settingUpdateDTO = settingService.getSite();

        return new Result<SiteVO>().ok(ConvertUtils.sourceToTarget(settingUpdateDTO, SiteVO.class));
    }

    /**
     * 专题页详情
     *
     * @param params 专题页id
     * @return
     */
    @GetMapping("topic/info")
    @ApiOperation("专题页详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true, dataType = "int"),
            @ApiImplicitParam(name = "id", value = "专题页id", required = true, paramType = "query", dataType = "Long")
    })
    public Result<TopicInfoDTO> topicInfo(@ApiIgnore @RequestParam Map<String, Object> params) {
        TopicInfoDTO data = topicService.topicPage(params);
        return new Result<TopicInfoDTO>().ok(data);
    }

    /**
     * 获取轮播图和导航
     *
     * @return
     */

    @GetMapping("adv")
    @ApiOperation("获取轮播图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "advKey", value = "pcIndex pc首页; pcStoreIndex 店铺首页", paramType = "query",
                    required = true, dataType = "String"),
            @ApiImplicitParam(name = "storeId", value = "店铺Id", paramType = "query", required = false, dataType =
                    "Long")
    })
    @LogContext(code = IndexCode.INDEX_ADV_SLIDESHOW_CODE, note = IndexCode.INDEX_ADV_SLIDESHOW_DESC)
    public Result<List<AdvVO>> h5Index(@ApiIgnore @RequestParam Map<String, Object> params) {
        if (null == params.get(MAP_KEY_ADV_KEY) || StringUtils.isBlank(params.get(MAP_KEY_ADV_KEY).toString())) {
            return new Result<List<AdvVO>>().error(ErrorCode.BAD_REQUEST, "查询轮播图失败");
        }
        // 首页轮播图
        List<AdvDTO> advDTOList = advService.listIngAdv(params);
        //TODO 首页导航菜单待查询
        return new Result<List<AdvVO>>().ok(ConvertUtils.sourceToTarget(advDTOList, AdvVO.class), "查询轮播图成功");
    }

    /**
     * 获取首页楼层信息
     *
     * @param storeId 店铺ID ，pc首页传1
     * @return
     */

    @GetMapping("floor")
    @ApiOperation("获取首页楼层信息")
    @ApiImplicitParam(name = "storeId", value = "店铺ID,pc首页不用传", required = false, paramType = "query", dataType =
            "long")
    @LogContext(code = IndexCode.INDEX_ADV_FLOOR_CODE, note = IndexCode.INDEX_ADV_FLOOR_DESC)
    public Result<List<WebFloorAndLinkVO>> indexFloor(@RequestParam(value = "storeId", required = false) Long storeId) {
        List<WebFloorAndLinkDTO> list = cacheService.floor(storeId, IsShowEnum.PC_FLOOR.value());
        return new Result<List<WebFloorAndLinkVO>>().ok(ConvertUtils.sourceToTarget(list, WebFloorAndLinkVO.class),
                "查询楼层成功");
    }

    @GetMapping("custom/class")
    @ApiOperation("首页商品分类")
    @LogContext(code = IndexCode.INDEX_CUSTOM_CLASS_CODE, note = IndexCode.INDEX_CUSTOM_CLASS_DESC)
    public Result<List<GoodsClassListVO>> indexGoodsClass() {
        List<GoodsClassCustomListDTO> classCustomListDTOList = goodsClassCustomService.selectAllCustomH5(ShowClassEnum.SHOW_CLASS_TYPE_PC.value());
        List<GoodsClassListVO> goodsClassListVOList = ConvertUtils.sourceToTarget(classCustomListDTOList,
                GoodsClassListVO.class);
        if (CollectionUtils.isNotEmpty(goodsClassListVOList) && goodsClassListVOList.size() > MAX_INDEX_SIZE) {
            List<GoodsClassListVO> subList = goodsClassListVOList.subList(0, 14);
            return new Result<List<GoodsClassListVO>>().ok(subList);
        }
        return new Result<List<GoodsClassListVO>>().ok(goodsClassListVOList);
    }


    @GetMapping("hot/word")
    @ApiOperation("热词列表展示")
    @LogContext(code = IndexCode.INDEX_HOT_KEY_CODE, note = IndexCode.INDEX_HOT_KEY_DESC)
    public Result<List<HotWordVO>> findHotList() {
        List<HotWordDTO> hotWordDTOList = hotWordService.findDataRedis();
        List<HotWordVO> hotWordVOList = ConvertUtils.sourceToTarget(hotWordDTOList, HotWordVO.class);
        return new Result<List<HotWordVO>>().ok(hotWordVOList);
    }

    @GetMapping("recommend/page")
    @ApiOperation("首页商品推荐列表")
    @LogContext(code = IndexCode.INDEX_RECOMMEND_GOODS_CODE, note = IndexCode.INDEX_RECOMMEND_GOODS_DESC)
    public Result<PageData<CartRecommendVO>> page(@RequestParam(defaultValue = "1", value = "page") Integer page,
                                                  @RequestParam(defaultValue = "25", value = "limit") Integer limit) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put(Constant.PAGE, page.toString());
        params.put(Constant.LIMIT, limit.toString());
        PageData<CartRecommendPageDTO> result = cartRecommendService.pageList(params);
        PageData<CartRecommendVO> pageData = new PageData<>();
        if (CollectionUtils.isNotEmpty(result.getList())) {
            List<CartRecommendVO> cartRecommendVOList = ConvertUtils.sourceToTarget(result.getList(),
                    CartRecommendVO.class);
            pageData.setList(cartRecommendVOList);
            pageData.setTotal(result.getTotal());
        }
        return new Result<PageData<CartRecommendVO>>().ok(pageData);
    }

    /**
     * 首页导航
     *
     * @param params
     * @return
     */
    @GetMapping("navigation")
    @ApiOperation("首页导航")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "storeId", value = "店铺Id pc首页不用传；店铺首页传店铺ID", required = false, paramType =
                    "query", dataType = "Long")
    })
    @LogContext(code = IndexCode.INDEX_NAVIGATION_CODE, note = IndexCode.INDEX_NAVIGATION_DESC)
    public Result<List<NavigationDTO>> navigation(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("orderBy", "asc");
        List<NavigationDTO> list = navigationService.list(params);
        return new Result<List<NavigationDTO>>().ok(list, "查询成功");
    }

    @GetMapping("classTreeList")
    @ApiOperation("帮助中心-分类树分类集合")
    @LogContext(code = IndexCode.HELPCENTER_CLASS_TREE_LIST_CODE, note = IndexCode.HELPCENTER_CLASS_TREE_LIST_DESC)
    public Result<List<CmsClassTreeListVO>> classTreeList() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("acCode", CmsEnum.FWZNCODE.value());
        List<CmsClassTreeListDTO> data = cmsClassService.getTree(params);
        if (CollectionUtils.isEmpty(data)) {
            return new Result().error(ResultCodeEnum.WARN.value(), "查询结果为空");
        }
        return new Result<List<CmsClassTreeListVO>>().ok(ConvertUtils.sourceToTarget(data, CmsClassTreeListVO.class));
    }

    @GetMapping("firstClassList")
    @ApiOperation("帮助中心-一级分类集合")
    @LogContext(code = IndexCode.HELPCENTER_FIRST_CLASS_CODE, note = IndexCode.HELPCENTER_FIRST_CLASS_DESC)
    public Result<List<CmsClassTreeListVO>> firstClassList() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("acCode", CmsEnum.FWZNCODE.value());
        List<CmsClassTreeListDTO> data = cmsClassService.firstClassList(params);
        if (CollectionUtils.isEmpty(data)) {
            return new Result().error(ResultCodeEnum.WARN.value(), "查询结果为空");
        }
        return new Result<List<CmsClassTreeListVO>>().ok(ConvertUtils.sourceToTarget(data, CmsClassTreeListVO.class));
    }

    @GetMapping("childClassList")
    @ApiOperation("帮助中心-子分类集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父ID", paramType = "query", dataType = "Long")
    })
    @LogContext(code = IndexCode.HELPCENTER_CHILDREN_LIST_CODE, note = IndexCode.HELPCENTER_CHILDREN_LIST_DESC)
    public Result<List<CmsClassTreeListDTO>> childClassList(@ApiIgnore @RequestParam Map<String, Object> params) {
        params.put("acCode", CmsEnum.FWZNCODE.value());
        List<CmsClassTreeListDTO> listDtos = cmsClassService.childClassList(params);
        return new Result<List<CmsClassTreeListDTO>>().ok(listDtos, "查询成功");
    }


    @GetMapping("help/page")
    @ApiOperation("帮助中心-分类下的问题列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = Constant.PAGE, value = "当前页码，从1开始", paramType = "query", required = true,
                    dataType = "int"),
            @ApiImplicitParam(name = Constant.LIMIT, value = "每页显示记录数", paramType = "query", required = true,
                    dataType = "int"),
            @ApiImplicitParam(name = "articleTitle", value = "分类名称", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "acId", value = "分类ID", paramType = "query", dataType = "String")
    })
    @LogContext(code = IndexCode.HELPCENTER_PAGE_LIST_CODE, note = IndexCode.HELPCENTER_PAGE_LIST_DESC)
    public Result<PageData<CmsArticleFwznListVO>> artPage(@ApiIgnore @RequestParam Map<String, Object> params) {
        //启用状态，前台显示
        params.put("status", 1);
        params.put("front", 1);
        PageData<CmsArticleFwznListDTO> page = cmsArticleService.pageFwzn(params);
        PageData<CmsArticleFwznListVO> pageData = new PageData<>();
        BeanCopier.create(PageData.class, PageData.class, false)
                .copy(page, pageData, null);
        return new Result<PageData<CmsArticleFwznListVO>>().ok(pageData, "查询服务指南成功");
    }

    @GetMapping("help/{id}")
    @ApiOperation("帮助中心-问题详情")
    @LogContext(code = IndexCode.HELPCENTER_QUESTION_DETAIL_CODE, note = IndexCode.HELPCENTER_QUESTION_DETAIL_DESC)
    public Result<CmsArticleFwznFrontVO> get(@PathVariable("id") Long id) {
        CmsArticleFwznFrontDTO data = cmsArticleService.getFwznArticleFront(id);
        return new Result<CmsArticleFwznFrontVO>().ok(ConvertUtils.sourceToTarget(data, CmsArticleFwznFrontVO.class));
    }

    @GetMapping("recommend")
    @ApiOperation("推荐接口")
    public Result<List<GoodsRecommendDTO>> recommend(@RequestParam(value = "goodsId", required = false) Long goodsId,
                                                     @RequestParam(value = "addFlag") Integer addFlag) {
        Long userId = null;
        if (SecurityCurrentUser.userIsLogin()) {
            userId = SecurityCurrentUser.getUserDetail().getId();
        }

//        List<GoodsRecommendDTO> goodsRecommendDTOList = goodsService.hotRecommend(goodsId, userId, addFlag);
        List<GoodsRecommendDTO> goodsRecommendDTOList = null;

        return new Result<List<GoodsRecommendDTO>>().ok(goodsRecommendDTOList);
    }


}

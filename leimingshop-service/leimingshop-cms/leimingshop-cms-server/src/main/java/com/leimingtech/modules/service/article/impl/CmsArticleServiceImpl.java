/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.article.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.article.CmsArticleDao;
import com.leimingtech.modules.dao.articleclass.CmsClassDao;
import com.leimingtech.modules.dao.attention.CmsAttentionDao;
import com.leimingtech.modules.dao.circle.CmsCircleDao;
import com.leimingtech.modules.dto.article.*;
import com.leimingtech.modules.dto.browse.BrowseCmsDTO;
import com.leimingtech.modules.dto.cart.CartCmsDTO;
import com.leimingtech.modules.dto.goods.cms.CircleGoodsSearchDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.remind.CmsRemindMqDTO;
import com.leimingtech.modules.entity.article.CmsArticleEntity;
import com.leimingtech.modules.entity.articleclass.CmsClassEntity;
import com.leimingtech.modules.entity.circle.CmsCircleEntity;
import com.leimingtech.modules.enums.articleclass.CmsEnum;
import com.leimingtech.modules.enums.remind.RemindTypeEnum;
import com.leimingtech.modules.service.article.CmsArticleService;
import com.leimingtech.modules.service.browse.GoodsBrowseService;
import com.leimingtech.modules.service.cart.CartService;
import com.leimingtech.modules.service.favorites.FavoritesService;
import com.leimingtech.modules.service.goods.GoodsService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.remind.CmsRemindService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.leimingtech.service.stopword.StopWordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 文章管理 CmsArticleServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsArticleServiceImpl extends BaseServiceImpl<CmsArticleDao, CmsArticleEntity> implements CmsArticleService {

    private static final String FLAG = "flag";

    private static final String MEMBER_ID = "memberId";

    private static final String CART_GOOD_FLAG = "cartGoodFlag";

    private static final String ORDER_GOOD_FLAG = "orderGoodFlag";

    private static final String FAVORITE_GOOD_FLAG = "favoriteGoodFlag";

    private static final String BROWSE_GOOD_FLAG = "browseGoodFlag";
    @Resource
    private CmsClassDao cmsClassDao;

    @Resource
    private CmsAttentionDao attentionDao;

    @Resource
    private CmsCircleDao cmsCircleDao;

    @Resource
    private MemberService memberService;

    @Resource
    private CartService cartService;

    @Resource
    private OrderService orderService;

    @Resource
    private FavoritesService favoritesService;

    @Resource
    private GoodsBrowseService goodsBrowseService;

    @Resource
    private GoodsService goodsService;

    @Autowired
    private CmsRemindService cmsRemindService;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private StopWordService stopWordService;

    /**
     * 圈子文章分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsArticleQzListDTO> pageQz(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsArticleQzListDTO> page = new Page<>(pageNum, limit);
        List<CmsArticleQzListDTO> cmsArticleQzList = baseDao.selectCmsArticleQzList(page, params);
        return new PageData(cmsArticleQzList, page.getTotal());
    }

    /**
     * 资讯文章分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsArticleZxListDTO> pageZx(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsArticleZxListDTO> page = new Page<>(pageNum, limit);
        List<CmsArticleZxListDTO> cmsArticleZxList = baseDao.selectCmsArticleZxList(page, params);
        return new PageData<>(cmsArticleZxList, page.getTotal());
    }

    /**
     * 服务指南文章分页查询
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsArticleFwznListDTO> pageFwzn(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsArticleFwznListDTO> page = new Page<>(pageNum, limit);
        List<CmsArticleFwznListDTO> cmsArticleFwznList = baseDao.selectCmsArticleFwznList(page, params);
        return new PageData(cmsArticleFwznList, page.getTotal());
    }

    /**
     * 圈子文章新增
     *
     * @param dto
     */
    @Override

    public void saveQz(@RequestBody CmsArticleQzSaveDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setCreator(userName);
        entity.setUpdater(userName);
        insert(entity);
    }

    /**
     * @author zhangyuhao
     * 资讯文章新增
     */
    @Override

    public void saveZx(@RequestBody CmsArticleZxSaveDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setCreator(userName);
        entity.setUpdater(userName);
        insert(entity);
    }

    /**
     * 服务指南文章新增
     *
     * @param dto
     */
    @Override

    public void saveFwzn(@RequestBody CmsArticleFwznSaveDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setCreator(userName);
        entity.setUpdater(userName);
        insert(entity);
    }

    /**
     * 圈子文章修改
     *
     * @param dto
     */
    @Override

    public void updateQz(@RequestBody CmsArticleQzUpdateDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setUpdateDate(date);
        entity.setUpdater(userName);
        updateById(entity);
    }

    /**
     * @Author zhangyuhao
     * 资讯文章修改
     */
    @Override

    public void updateZx(@RequestBody CmsArticleZxUpdateDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setUpdateDate(date);
        entity.setUpdater(userName);
        updateById(entity);
    }

    /**
     * 服务指南文章修改
     *
     * @param dto
     */
    @Override

    public void updateFwzn(@RequestBody CmsArticleFwznUpdateDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity entity = ConvertUtils.sourceToTarget(dto, CmsArticleEntity.class);
        entity.setUpdateDate(date);
        entity.setUpdater(userName);
        updateById(entity);
    }

    /**
     * 文章逻辑删除
     *
     * @param ids
     */
    @Override
    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        logicDelete(ids);
        //物理删除
//        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 圈子后台文章详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsArticleQzAdminInfoDTO getQzArticleAdmin(Long id) {
        CmsArticleQzAdminInfoDTO cmsArticleQzSaveDTO = baseDao.selectCmsArticleQzInfo(id);
        return cmsArticleQzSaveDTO;
    }

    /**
     * 资讯后台文章详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsArticleZxSaveDTO getZxArticleAdmin(Long id) {
        CmsArticleEntity entity = baseDao.selectById(id);
        CmsClassEntity classEntity = cmsClassDao.selectById(entity.getAcId());
        //分类acIdpaths
        String[] acIdpaths = classEntity.getAcIdpaths().split(",");
        CmsArticleZxSaveDTO articleZx = ConvertUtils.sourceToTarget(entity, CmsArticleZxSaveDTO.class);
        articleZx.setAcIdpaths(acIdpaths);
        return articleZx;
    }

    /**
     * 服务指南后台文章详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsArticleFwznSaveDTO getFwznArticleAdmin(Long id) {
        CmsArticleEntity entity = baseDao.selectById(id);
        CmsClassEntity classEntity = cmsClassDao.selectById(entity.getAcId());
        //分类acIdpaths
        String[] acIdpaths = classEntity.getAcIdpaths().split(",");
        CmsArticleFwznSaveDTO articleFwzn = ConvertUtils.sourceToTarget(entity, CmsArticleFwznSaveDTO.class);
        articleFwzn.setAcIdpaths(acIdpaths);
        return articleFwzn;
    }

    /**
     * 前台圈子文章详情
     *
     * @param params
     * @return
     */
    @Override

    public CmsFrontArticleDetailDTO getFrontArticleDetail(@RequestParam Map<String, Object> params) {
        CmsFrontArticleDetailDTO detailDTO = baseDao.selectArticleById(params);
        //获取用户信息
        MemberDTO member = memberService.getById(detailDTO.getArticleCreaterId());
        Optional.ofNullable(member).ifPresent(m -> {
            detailDTO.setMemberAvatar(m.getMemberAvatar());
            detailDTO.setNickName(m.getNickName());
        });
        Long memberId = Long.parseLong(params.get(MEMBER_ID).toString());
        detailDTO.setLoginFlag(memberId.equals(detailDTO.getArticleCreaterId()) ? 1 : 0);
        return detailDTO;
    }

    /**
     * 前台资讯文章详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsArticleZxFrontDTO getZxArticleFront(Long id) {
        CmsArticleEntity entity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, CmsArticleZxFrontDTO.class);
    }

    /**
     * 前台服务指南文章详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsArticleFwznFrontDTO getFwznArticleFront(Long id) {
        CmsArticleEntity entity = baseDao.selectById(id);
        return ConvertUtils.sourceToTarget(entity, CmsArticleFwznFrontDTO.class);
    }

    /**
     * H5文章列表
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsFrontCircleArticleListDTO> frontArticleList(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsFrontCircleArticleListDTO> page = new Page<>(pageNum, limit);
        List<CmsFrontCircleArticleListDTO> articleList;
        if (String.valueOf(1).equals(params.get(FLAG).toString())) {
            articleList = baseDao.selectFrontConcernArticleList(page, params);
        } else {
            //查询热门帖子列表
            articleList = baseDao.selectFrontHotArticleList(page, params);
        }
        memberInfo(articleList, params);

        return new PageData<>(articleList, page.getTotal());
    }

    /**
     * 设置用户昵称和头像
     */
    public void memberInfo(List<CmsFrontCircleArticleListDTO> articleList, Map<String, Object> params) {
        Long memberId = Long.parseLong(params.get(MEMBER_ID).toString());
        articleList.forEach(x -> {
            MemberDTO memberDTO = memberService.getById(x.getArticleCreaterId());
            if (memberDTO != null) {
                x.setMemberAvatar(memberDTO.getMemberAvatar());
                x.setNickName(memberDTO.getNickName());
            }
            x.setLoginFlag(memberId.equals(x.getArticleCreaterId()) ? 1 : 0);
        });
    }

    /**
     * 文章状态修改
     *
     * @param dto
     */
    @Override

    public void statusUpdate(@RequestBody CmsArticleStatusUpdateDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        CmsArticleEntity cmsArticleEntity = new CmsArticleEntity();
        cmsArticleEntity.setId(dto.getId());
        switch (dto.getType()) {
            case 1:
                cmsArticleEntity.setTopFlag(dto.getCode());
                break;
            case 2:
                cmsArticleEntity.setHeadFlag(dto.getCode());
                break;
            case 3:
                cmsArticleEntity.setStatus(dto.getCode());
                break;
            case 4:
                cmsArticleEntity.setCommentFlag(dto.getCode());
                break;
            case 5:
                cmsArticleEntity.setEssenceFlag(dto.getCode());
                break;
            default:
                return;
        }
        updateById(cmsArticleEntity);
    }


    /**
     * H5帖子发布
     *
     * @param article
     */
    @Override

    public void frontArticlePublish(@RequestBody CmsFrontArticlePublishDTO article) {
        Date date = new Date();
        CmsArticleEntity articleEntity = new CmsArticleEntity();
        BeanUtils.copyProperties(article, articleEntity);
        articleEntity.setTopicName(StringUtils.isNotEmpty(article.getTopicName()) ? article.getTopicName() : null);
        articleEntity.setCreateDate(new Date());
        articleEntity.setCreator(article.getCreator());
        articleEntity.setUpdateDate(date);
        articleEntity.setUpdater(article.getCreator());
        articleEntity.setAcCode(1);
        articleEntity.setAudit(1);

        Optional.ofNullable(article.getArticleFlag()).ifPresent(x -> {
            if (CmsEnum.wzCOODE.value().equals(x)) {
                articleEntity.setAudit(0);
            }
        });
        //处理内容,标题
        articleEntity.setArticleContent(stopWordService.replaceStopWord(articleEntity.getArticleContent()));

        if (StringUtils.isNotBlank(articleEntity.getArticleTitle())) {
            articleEntity.setArticleTitle(stopWordService.replaceStopWord(articleEntity.getArticleTitle()));
        }

        if (StringUtils.isNotBlank(articleEntity.getArticleShortTitle())) {
            articleEntity.setArticleShortTitle(stopWordService.replaceStopWord(articleEntity.getArticleShortTitle()));
        }
        baseDao.insert(articleEntity);
        if (StringUtils.isNotEmpty(article.getTopicName())) {
            dealTopic(article, date);
        }
        //非文章类型帖子直接发送艾特信息
        if (!CmsEnum.wzCOODE.value().equals(articleEntity.getArticleFlag())) {
            if (StringUtils.isNotBlank(articleEntity.getRemind())) {
                CmsRemindMqDTO cmsRemindMqDTO = new CmsRemindMqDTO();
                cmsRemindMqDTO.setRemindType(RemindTypeEnum.ARTICLE_CODE.value());
                cmsRemindMqDTO.setRemindTypeId(articleEntity.getId());
                cmsRemindMqDTO.setMemberIdList(articleEntity.getRemind());
                cmsRemindMqDTO.setCreatorId(articleEntity.getArticleCreaterId());
                String message = JSONObject.toJSONString(cmsRemindMqDTO);
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE, message);
            }
        }
    }

    /**
     * 发布文章时处理话题
     *
     * @param article
     */
    private void dealTopic(CmsFrontArticlePublishDTO article, Date date) {
        //处理话题
        List<String> topicName = Arrays.asList(article.getTopicName().split(","));
        topicName.forEach(topic -> {
            List<CmsCircleEntity> entity = cmsCircleDao.selectList(new QueryWrapper<CmsCircleEntity>()
                    .eq("topic_name", topic)
                    .eq("del_flag", 0));
            if (entity.size() == 0 && StringUtils.isNotEmpty(article.getTopicName())) {
                //新增话题
                CmsCircleEntity circleEntity = new CmsCircleEntity();
                circleEntity.setTopicName(topic);
                if (article.getAcId() != null) {
                    circleEntity.setAcId(article.getAcId());
                }
                circleEntity.setCreaterId(article.getArticleCreaterId());
                circleEntity.setCreateDate(date);
                circleEntity.setUpdateId(article.getArticleCreaterId());
                circleEntity.setUpdateDate(date);
                cmsCircleDao.insert(circleEntity);
            }
        });
    }

    /**
     * H5帖子修改
     *
     * @param article
     */
    @Override

    public void frontArticleUpdate(@RequestBody CmsFrontArticleUpdateDTO article) {
        Date date = new Date();
        CmsArticleEntity articleEntity = new CmsArticleEntity();
        BeanUtils.copyProperties(article, articleEntity);
        articleEntity.setTopicName(StringUtils.isNotEmpty(article.getTopicName()) ? article.getTopicName() : null);
        articleEntity.setAudit(0);
        articleEntity.setUpdateDate(date);
        baseDao.updateById(articleEntity);
        if (StringUtils.isNotEmpty(article.getTopicName())) {
            dealTopic(article, date);
        }
    }

    /**
     * H5帖子修改回显详情
     *
     * @param id
     * @return
     */
    @Override

    public CmsFrontArticleUpdateDetailDTO getQzArticleFrontInfo(Long id) {
        return baseDao.selectQzInfoById(id);
    }

    /**
     * 话题下的帖子分页列表（前台）
     *
     * @param params
     * @return
     */
    @Override
    public PageData<CmsFrontCircleArticleListDTO> selectArticleListByTopicId(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsFrontCircleArticleListDTO> page = new Page<>(pageNum, limit);
        List<CmsFrontCircleArticleListDTO> cmsFrontCircleArticleListDTOList = baseDao.selectArticleListByTopicId(page, params);
        memberInfo(cmsFrontCircleArticleListDTOList, params);
        return new PageData(cmsFrontCircleArticleListDTOList, page.getTotal());
    }

    /**
     * 用户主页帖子列表
     */
    @Override

    public PageData<CmsFrontCircleArticleListDTO> userIndexArticleList(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsFrontCircleArticleListDTO> page = new Page<>(pageNum, limit);

        List<CmsFrontCircleArticleListDTO> list = baseDao.selectArticleListByMemberId(page, params);

        Long memberId = Long.parseLong(params.get("loginMemberId").toString());
        list.forEach(x -> {
            MemberDTO memberDTO = memberService.getById(x.getArticleCreaterId());
            if (memberDTO != null) {
                x.setMemberAvatar(memberDTO.getMemberAvatar());
                x.setNickName(memberDTO.getNickName());
            }
            x.setLoginFlag(memberId.equals(x.getArticleCreaterId()) ? 1 : 0);
        });

        return new PageData<>(list, page.getTotal());
    }

    /**
     * 用户主页头部
     */
    @Override

    public MemberIndexDTO userIndexHead(@RequestParam Map<String, Object> params) {

        MemberIndexDTO memberIndexDTO = new MemberIndexDTO();
        Long memberId = Long.parseLong(params.get(MEMBER_ID).toString());
        Long loginMemberId = Long.parseLong(params.get("loginMemberId").toString());
        Integer articleNum = baseDao.selectCountByMemberId(memberId);
        Integer fansNum = attentionDao.selectFansNum(memberId);
        Integer attentionNum = attentionDao.selectAttentionNum(memberId);
        Integer concernedFlag = attentionDao.concernedFlag(memberId, loginMemberId);
        MemberDTO memberDTO = memberService.getById(memberId);
        Integer remindNum = cmsRemindService.readAndMemberCount(memberId);

        memberIndexDTO.setLoginFlag(loginMemberId.equals(memberId) ? 1 : 0);
        memberIndexDTO.setArticleCreaterId(memberDTO.getId());
        memberIndexDTO.setAttentionNum(attentionNum);
        memberIndexDTO.setArticleNum(articleNum);
        memberIndexDTO.setFansNum(fansNum);
        memberIndexDTO.setNickName(memberDTO.getNickName());
        memberIndexDTO.setMemberAvatar(memberDTO.getMemberAvatar());
        memberIndexDTO.setConcernedFlag(concernedFlag);
        memberIndexDTO.setRemindNum(remindNum);
        return memberIndexDTO;
    }

    /**
     * 用户搜索
     */
    @Override

    public PageData<MemberIndexDTO> userSearch(@RequestParam Map<String, Object> params) {
        List<MemberIndexDTO> result = new ArrayList<>();

        PageData<MemberDTO> memberPageDataDTO = memberService.selectByNickName(params);
        List<MemberDTO> list = memberPageDataDTO.getList();
        ConvertUtils.sourceToTarget(memberPageDataDTO, MemberIndexDTO.class);
        Long memberId = null;
        if (params.get(MEMBER_ID) != null) {
            memberId = Long.parseLong(params.get(MEMBER_ID).toString());
        }
        if (list != null) {
            Long finalMemberId = memberId;
            list.forEach(memberDTO -> {
                Long id = memberDTO.getId();
                params.put("id", id);
                MemberIndexDTO memberIndexDTO = baseDao.selectNumByMemberId(params);

                MemberIndexDTO memberIndex = baseDao.selectFirstArticle(params);
                if (memberIndex != null) {
                    memberIndexDTO.setArticleContent(memberIndex.getArticleContent());
                    memberIndexDTO.setArticleFlag(memberIndex.getArticleFlag());
                }
                memberIndexDTO.setArticleCreaterId(id);
                memberIndexDTO.setNickName(memberDTO.getNickName());
                memberIndexDTO.setMemberAvatar(memberDTO.getMemberAvatar());
                memberIndexDTO.setLoginFlag(id.equals(finalMemberId) ? 1 : 0);
                if (!id.equals(finalMemberId)) {
                    result.add(memberIndexDTO);
                }
            });

        }
        return new PageData<>(result, memberPageDataDTO.getTotal());
    }

    /**
     * 文章点赞
     *
     * @param id
     */
    @Override

    public void frontArticlePraise(@RequestParam Long id) {
        baseDao.updateArticlePraise(id);
    }


    /**
     * 文章浏览量更新
     *
     * @param params
     */
    @Override

    public void updateArticlePvNum(@RequestParam Map<String, Object> params) {
        //文章id
        Long articleId = Long.valueOf(String.valueOf(params.get("articleId")));
        //更新浏览量
        baseDao.updateArticlePvNum(articleId);

        //用户id
        Long memberId = Long.valueOf(String.valueOf(params.get(MEMBER_ID)));

        params.put("readFlag", 0);
        //查询文章中有没有艾特我
        List<Map<String, Object>> listMap1 = cmsRemindService.articleGetIdList(params);
        if (null != listMap1 && !listMap1.isEmpty()) {
            for (Map<String, Object> map1 : listMap1) {
                Long id = Long.valueOf(String.valueOf(map1.get("remindId")));
                cmsRemindService.read(id);
            }
        }

        //查询评论中有没有艾特我
        List<Map<String, Object>> listMap2 = cmsRemindService.commentGetIdList(params);
        if (null != listMap2 && !listMap2.isEmpty()) {
            for (Map<String, Object> map2 : listMap2) {
                Long id = Long.valueOf(String.valueOf(map2.get("remindId")));
                cmsRemindService.read(id);
            }
        }
    }

    /**
     * 文章分享数更新
     */
    @Override

    public void frontArticleShare(@RequestParam Long id) {
        baseDao.updateArticleShareNum(id);
    }


    /**
     * 指定商品分页搜索
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsFrontGoodsSearchDTO> circleGoodsSearch(@RequestParam Map<String, Object> params) {
        Long memberId = Long.parseLong(params.get(MEMBER_ID).toString());
        //商品id查询去重
        Set<Long> goodIds = new HashSet<>();
        //购物车
        if (String.valueOf(1).equals(params.get(CART_GOOD_FLAG).toString())) {
            List<CartCmsDTO> cartGoodIds = cartService.getCartGoodIds(memberId);
            cartGoodIds.forEach(id -> {
                goodIds.add(id.getGoodsId());
            });
        }
        //订单
        if (String.valueOf(1).equals(params.get(ORDER_GOOD_FLAG).toString())) {
            List<Long> orderGoodIds = orderService.getOrderGoodIds(memberId);
            orderGoodIds.forEach(id -> {
                goodIds.add(id);
            });
        }
        //收藏
        if (String.valueOf(1).equals(params.get(FAVORITE_GOOD_FLAG).toString())) {
            List<Long> favoriteGoodIds = favoritesService.getFavoriteGoodIds(memberId);
            favoriteGoodIds.forEach(id -> {
                goodIds.add(id);
            });
        }
        //浏览
        if (String.valueOf(1).equals(params.get(BROWSE_GOOD_FLAG).toString())) {
            List<BrowseCmsDTO> browseGoodIds = goodsBrowseService.getBrowseGoodIds(memberId);
            browseGoodIds.forEach(id -> {
                goodIds.add(id.getGoodsId());
            });
        }
        //查询商品信息
        PageData<CircleGoodsSearchDTO> goodsListDTO = goodsService.circleGoodsSearch(params, goodIds);
        List<CircleGoodsSearchDTO> list = goodsListDTO.getList();
        List<CmsFrontGoodsSearchDTO> goodsSearchDTO = ConvertUtils.sourceToTarget(list, CmsFrontGoodsSearchDTO.class);
        return new PageData<>(goodsSearchDTO, goodsListDTO.getTotal());
    }

}

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.attention.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.article.CmsArticleDao;
import com.leimingtech.modules.dao.attention.CmsAttentionDao;
import com.leimingtech.modules.dto.attention.CmsAttentionClassListDTO;
import com.leimingtech.modules.dto.attention.CmsAttentionDTO;
import com.leimingtech.modules.dto.attention.CmsAttentionListDTO;
import com.leimingtech.modules.dto.attention.CmsRecommendAttentionDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.entity.attention.CmsAttentionEntity;
import com.leimingtech.modules.enums.remind.RemindTypeEnum;
import com.leimingtech.modules.service.attention.CmsAttentionService;
import com.leimingtech.modules.service.member.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 圈子关注管理 CmsAttentionServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsAttentionServiceImpl extends BaseServiceImpl<CmsAttentionDao, CmsAttentionEntity> implements CmsAttentionService {

    @Resource
    private MemberService memberService;

    @Resource
    private CmsAttentionDao attentionDao;

    @Resource
    private CmsArticleDao cmsArticleDao;

    @Override

    public PageData<CmsAttentionDTO> page(@RequestParam Map<String, Object> params) {
        IPage<CmsAttentionEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, CmsAttentionDTO.class);
    }

    @Override

    public List<CmsAttentionDTO> list(Map<String, Object> params) {
        List<CmsAttentionEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, CmsAttentionDTO.class);
    }

    private QueryWrapper<CmsAttentionEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CmsAttentionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    @Override

    public CmsAttentionDTO get(Long id) {
        CmsAttentionEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CmsAttentionDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsAttentionDTO dto) {

        //更新点赞数
        if (dto.getAttentionFlag() == RemindTypeEnum.ARTICLE_REMIND.value()) {
            cmsArticleDao.updateArticlePraise(dto.getFocusedId());
        }
        CmsAttentionEntity entity = ConvertUtils.sourceToTarget(dto, CmsAttentionEntity.class);
        Date date = new Date();
        entity.setCreateDate(date);
        entity.setUpdateDate(date);
        entity.setUpdater(dto.getCreator());
        insert(entity);
    }

    /**
     * 我关注的用户列表
     *
     * @param userId
     * @return
     */
    @Override

    public List<CmsAttentionListDTO> selectAttentionUserList(@RequestParam Long userId) {

        List<CmsAttentionListDTO> list = baseDao.selectAttentionUserList(userId);
        list.forEach(attentionListDTO -> {
            MemberDTO memberDTO = memberService.getById(attentionListDTO.getFocusedId());
            Optional.ofNullable(memberDTO).ifPresent(member -> {
                attentionListDTO.setMemberAvatar(memberDTO.getMemberAvatar());
                attentionListDTO.setNickName(memberDTO.getNickName());
            });
        });
        return list;
    }

    /**
     * 我关注的分类列表
     *
     * @param userId
     * @return
     */
    @Override

    public List<CmsAttentionClassListDTO> selectAttentionClassList(@RequestParam Long userId) {
        List<CmsAttentionClassListDTO> list = baseDao.selectAttentionClassList(userId);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CmsAttentionDTO dto) {
        CmsAttentionEntity entity = ConvertUtils.sourceToTarget(dto, CmsAttentionEntity.class);

        updateById(entity);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, CmsAttentionEntity.articleclass);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    /**
     * 取消关注，取消点赞
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void cancelAttention(@RequestParam Map<String, Object> params) {
        QueryWrapper<CmsAttentionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attention_creater_id", params.get("memberId"))
                .eq("attention_flag", params.get("attentionFlag"));
        List<CmsAttentionEntity> list = baseDao.selectList(wrapper);
        Optional.ofNullable(list).ifPresent(lists -> {
            lists.forEach(cmsAttentionEntity -> {
                Object ids = params.get("ids");
                if (cmsAttentionEntity.getFocusedId().equals(Long.parseLong(ids.toString()))) {
                    cmsArticleDao.cancelArticlePraise(cmsAttentionEntity.getFocusedId());
                    baseDao.deleteAttention(cmsAttentionEntity.getId());
                }
            });
        });

    }


    /**
     * 推荐关注的用户列表
     *
     * @return
     */
    @Override

    public List<CmsRecommendAttentionDTO> selectRecommendUserList(@RequestParam Long memberId) {
        List<CmsRecommendAttentionDTO> attentionListDTOList = baseDao.getRecommendUser(memberId);
        attentionListDTOList.forEach(attentionListDTO -> {
            MemberDTO memberDTO = memberService.getById(attentionListDTO.getFocusedId());
            Optional.ofNullable(memberDTO).ifPresent(member -> {
                attentionListDTO.setMemberAvatar(memberDTO.getMemberAvatar());
                attentionListDTO.setNickName(memberDTO.getNickName());
            });
            attentionListDTO.setFansNum(baseDao.selectFansNum(attentionListDTO.getFocusedId()));
            attentionListDTO.setArticleNum(cmsArticleDao.selectArticleCount(attentionListDTO.getFocusedId()));
        });
        return attentionListDTOList;
    }


    /**
     * 我的粉丝列表
     */
    @Override

    public List<CmsRecommendAttentionDTO> selectFansList(@RequestParam Long memberId) {
        List<CmsRecommendAttentionDTO> list = baseDao.selectFansList(memberId);
        list.forEach(attentionListDTO -> {
            MemberDTO memberDTO = memberService.getById(attentionListDTO.getAttentionCreaterId());
            Optional.ofNullable(memberDTO).ifPresent(member -> {
                attentionListDTO.setMemberAvatar(member.getMemberAvatar());
                attentionListDTO.setNickName(member.getNickName());
            });

            Integer articleNum = cmsArticleDao.selectArticleCount(attentionListDTO.getAttentionCreaterId());
            Integer fansNum = attentionDao.selectFansNum(attentionListDTO.getAttentionCreaterId());
            attentionListDTO.setArticleNum(articleNum);
            attentionListDTO.setFansNum(fansNum);
        });
        return list;
    }


}

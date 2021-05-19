/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.remind.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.remind.CmsRemindDao;
import com.leimingtech.modules.dto.article.CmsFrontCircleArticleListDTO;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.remind.CmsRemindDTO;
import com.leimingtech.modules.entity.remind.CmsRemindEntity;
import com.leimingtech.modules.enums.CmsEnums;
import com.leimingtech.modules.enums.remind.RemindTypeEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.remind.CmsRemindService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-08
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsRemindServiceImpl extends BaseServiceImpl<CmsRemindDao, CmsRemindEntity> implements CmsRemindService {

    @Autowired
    private MemberService memberService;

    @Override

    public PageData<CmsRemindDTO> page(@RequestParam Map<String, Object> params) {
        IPage<CmsRemindEntity> page = baseDao.selectPage(
                getPage(params, Constant.UPDATE_DATE, false),
                getWrapper(params)
        );

        return getPageData(page, CmsRemindDTO.class);
    }

    @Override

    public List<CmsRemindDTO> list(Map<String, Object> params) {
        List<CmsRemindEntity> entityList = baseDao.selectList(getWrapper(params));

        return ConvertUtils.sourceToTarget(entityList, CmsRemindDTO.class);
    }

    private QueryWrapper<CmsRemindEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<CmsRemindEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);
        return wrapper;
    }

    @Override

    public CmsRemindDTO get(Long id) {
        CmsRemindEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CmsRemindDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsRemindDTO dto) {
        CmsRemindEntity entity = ConvertUtils.sourceToTarget(dto, CmsRemindEntity.class);
        entity.setCreateDate(new Date());
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CmsRemindDTO dto) {
        CmsRemindEntity entity = ConvertUtils.sourceToTarget(dto, CmsRemindEntity.class);

        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        //logicDelete(ids, CmsRemindEntity.class);

        //物理删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override

    public Integer readAndMemberCount(Long creatorId) {
        QueryWrapper<CmsRemindEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("member_id", creatorId).eq("read_flag", 0).eq("del_flag", 0);
        return baseDao.selectCount(queryWrapper);
    }

    @Override

    public PageData<CmsFrontCircleArticleListDTO> pageList(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsFrontCircleArticleListDTO> page = new Page<>(pageNum, limit);
        List<CmsFrontCircleArticleListDTO> list = new ArrayList<>();
        String typeFlag = String.valueOf(params.get("typeFlag"));
        if (CmsEnums.ALL_ARTICLE.value().equals(typeFlag)) {
            //艾特我的全部文章信息
            list = baseDao.selectArticleList(page, params);
        } else if (CmsEnums.ALL_NWE_ARTICLE.value().equals(typeFlag)) {
            //艾特我的，我所关注的用户发布的文章
            params.put("creatorId", params.get("memberId"));
            list = baseDao.selectArticleList(page, params);
        } else if (CmsEnums.ALL_EVALUATE.value().equals(typeFlag)) {
            //艾特我的所有评论
            list = baseDao.selectCommentList(page, params);
        } else if (CmsEnums.ALL_NEW_EVALUATE.value().equals(typeFlag)) {
            //艾特我的，我所关注用户发布的评论
            params.put("creatorId", params.get("memberId"));
            list = baseDao.selectCommentList(page, params);
        } else {
            //全部
            list = baseDao.selectAllList(page, params);
        }
        //获取用户头像,姓名等信息
        memberInfo(list, params);
        return new PageData<>(list, page.getTotal());
    }

    @Override

    public Integer read(Long id) {
        CmsRemindEntity cmsRemindEntity = new CmsRemindEntity();
        cmsRemindEntity.setId(id);
        cmsRemindEntity.setUpdateDate(new Date());
        cmsRemindEntity.setReadFlag(1);
        return baseDao.updateById(cmsRemindEntity);
    }

    @Override
    public List<Map<String, Object>> commentGetIdList(@RequestParam Map<String, Object> params) {
        return baseDao.commentGetIdList(params);
    }

    @Override
    public List<Map<String, Object>> articleGetIdList(@RequestParam Map<String, Object> params) {
        return baseDao.articleGetIdList(params);
    }

    @Override
    public void deleteRemind(@RequestParam Map<String, Object> params) {
        Integer remindType = Integer.valueOf(String.valueOf(params.get("remindType")));
        //图文
        if (RemindTypeEnum.ARTICLE_CODE.value() == remindType) {
            List<Map<String, Object>> listMap1 = baseDao.articleGetIdList(params);
            if (null != listMap1 && !listMap1.isEmpty()) {
                for (Map<String, Object> map1 : listMap1) {
                    Long id = Long.valueOf(String.valueOf(map1.get("remindId")));
                    baseDao.updateDelById(id);
                }
            }
        }
        if (RemindTypeEnum.COMMENT_CODE.value() == remindType) {
            List<Map<String, Object>> listMap2 = baseDao.commentGetIdList(params);
            if (null != listMap2 && !listMap2.isEmpty()) {
                for (Map<String, Object> map2 : listMap2) {
                    Long id = Long.valueOf(String.valueOf(map2.get("remindId")));
                    baseDao.updateDelById(id);
                }
            }
        }
        if (RemindTypeEnum.ARTICLE_CODE.value() != remindType || RemindTypeEnum.COMMENT_CODE.value() == remindType) {
            List<Map<String, Object>> listMap1 = baseDao.articleGetIdList(params);
            if (null != listMap1 && !listMap1.isEmpty()) {
                for (Map<String, Object> map1 : listMap1) {
                    Long id = Long.valueOf(String.valueOf(map1.get("remindId")));
                    baseDao.updateDelById(id);
                }
            }
            List<Map<String, Object>> listMap2 = baseDao.commentGetIdList(params);
            if (null != listMap2 && !listMap2.isEmpty()) {
                for (Map<String, Object> map2 : listMap2) {
                    Long id = Long.valueOf(String.valueOf(map2.get("remindId")));
                    baseDao.updateDelById(id);
                }
            }
        }
    }

    /**
     * 设置用户昵称和头像
     */
    public void memberInfo(List<CmsFrontCircleArticleListDTO> list, Map<String, Object> params) {
        Long memberId = Long.parseLong(params.get("memberId").toString());
        list.forEach(x -> {
            MemberDTO memberDTO = memberService.getById(x.getArticleCreaterId());
            if (memberDTO != null) {
                x.setMemberAvatar(memberDTO.getMemberAvatar());
                x.setNickName(memberDTO.getNickName());
            }
            x.setLoginFlag(memberId.equals(x.getArticleCreaterId()) ? 1 : 0);
        });
    }
}
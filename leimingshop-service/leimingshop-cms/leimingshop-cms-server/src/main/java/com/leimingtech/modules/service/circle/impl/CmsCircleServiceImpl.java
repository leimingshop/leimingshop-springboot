/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.circle.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.attention.CmsAttentionDao;
import com.leimingtech.modules.dao.circle.CmsCircleDao;
import com.leimingtech.modules.dto.attention.CmsFocusMemberDTO;
import com.leimingtech.modules.dto.attention.CmsMemberFocusDTO;
import com.leimingtech.modules.dto.circle.*;
import com.leimingtech.modules.entity.circle.CmsCircleEntity;
import com.leimingtech.modules.service.article.CmsArticleService;
import com.leimingtech.modules.service.circle.CmsCircleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 话题管理 CmsCircleServiceImpl
 *
 * @author pixiaoyong@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsCircleServiceImpl extends BaseServiceImpl<CmsCircleDao, CmsCircleEntity> implements CmsCircleService {

    @Resource
    private CmsAttentionDao cmsAttentionDao;

    @Resource
    private CmsArticleService cmsArticleService;


    /**
     * 圈子话题列表
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsCircleTopicPageDTO> page(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsCircleTopicPageDTO> page = new Page<>(pageNum, limit);
        List<CmsCircleTopicPageDTO> list = baseDao.selectCmsCircleTopicList(page, params);
        return new PageData(list, page.getTotal());
    }

    @Override

    public CmsCircleTopicDTO get(Long id) {
        CmsCircleEntity entity = baseDao.selectById(id);

        return ConvertUtils.sourceToTarget(entity, CmsCircleTopicDTO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsCircleTopicSaveDTO dto) {
        Date date = new Date();
        Long userId = SecurityUser.getUserId();
        String userName = SecurityUser.getUserName();
        CmsCircleEntity entity = ConvertUtils.sourceToTarget(dto, CmsCircleEntity.class);
        entity.setCreaterId(userId);
        entity.setCreator(userName);
        entity.setCreateDate(date);
        entity.setUpdateId(userId);
        entity.setUpdater(userName);
        entity.setUpdateDate(date);
        insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void update(@RequestBody CmsCircleTopicDTO dto) {
        Date date = new Date();
        Long userId = SecurityUser.getUserId();
        String userName = SecurityUser.getUserName();
        CmsCircleEntity entity = ConvertUtils.sourceToTarget(dto, CmsCircleEntity.class);
        entity.setUpdateId(userId);
        entity.setUpdater(userName);
        entity.setUpdateDate(date);
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //逻辑删除
        baseDao.deleteBatchIds(Arrays.asList(ids));
    }

    @Override

    public List<CmsMemberFocusDTO> memberFocus(Long id) {
        List<CmsMemberFocusDTO> list = cmsAttentionDao.getFocusList(id);
        return list;
    }

    @Override

    public List<CmsFocusMemberDTO> focusMember(Long id) {
        List<CmsFocusMemberDTO> list = cmsAttentionDao.getFocusMemberList(id);
        return list;
    }


    /**
     * 发布文章时创建话题
     */
    @Override

    public void createTopic(CmsCircleTopicSaveDTO cmsCircleTopicSaveDTO) {
        Date date = new Date();
        CmsCircleEntity cmsCircleEntity = new CmsCircleEntity();
        ConvertUtils.sourceToTarget(cmsCircleTopicSaveDTO, CmsCircleEntity.class);
        cmsCircleEntity.setCreateDate(date);
        cmsCircleEntity.setUpdateDate(date);
        baseDao.insert(cmsCircleEntity);
    }

    /**
     * 发布话题搜索页（前台）
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsTopicSelectFrontDTO> topicSelectFront(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsTopicSelectFrontDTO> page = new Page<>(pageNum, limit);
        List<CmsTopicSelectFrontDTO> list = baseDao.topicSelectFront(page, params);
        return new PageData(list, page.getTotal());
    }

    /**
     * H5话题主页
     *
     * @param params
     * @return
     */
    @Override

    public CmsTopicIndexFrontDTO topicIndex(@RequestParam Map<String, Object> params) {
        //话题详细信息
        CmsTopicIndexFrontDTO topic = baseDao.selectTopicIndex(params);
        return topic;
    }


}

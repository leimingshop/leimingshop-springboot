/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.articlerecommend.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.service.impl.BaseServiceImpl;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.modules.dao.articlerecommend.CmsArticleRecommendDao;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleAddRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendSaveDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsFrontArticleRecommendDTO;
import com.leimingtech.modules.entity.articlerecommend.CmsArticleRecommendEntity;
import com.leimingtech.modules.service.articlerecommend.CmsArticleRecommendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.*;

/**
 * 文章推荐管理 CmsArticleRecommendServiceImpl
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */
@Service
@Transactional(rollbackFor = Exception.class)

public class CmsArticleRecommendServiceImpl extends BaseServiceImpl<CmsArticleRecommendDao, CmsArticleRecommendEntity> implements CmsArticleRecommendService {

    @Resource
    private CmsArticleRecommendDao cmsArticleRecommendDao;

    /**
     * 资讯相关推荐
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsArticleRecommendDTO> page(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsArticleRecommendDTO> cmsArticleRecommendPageDTO = new Page<>(pageNum, limit);
        List<CmsArticleRecommendDTO> page = cmsArticleRecommendDao.selectCmsRecommendList(cmsArticleRecommendPageDTO, params);
        return new PageData(page, cmsArticleRecommendPageDTO.getTotal());
    }

    /**
     * 资讯添加推荐列表
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsArticleAddRecommendDTO> addRecommendList(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsArticleAddRecommendDTO> page = new Page<>(pageNum, limit);
        List<CmsArticleAddRecommendDTO> cmsArticleList = cmsArticleRecommendDao.selectAddRecommendList(page, params);
        return new PageData<>(cmsArticleList, page.getTotal());
    }

    /**
     * 新增相关推荐
     *
     * @param dto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void save(@RequestBody CmsArticleRecommendSaveDTO dto) {
        Date date = new Date();
        String userName = SecurityUser.getUserName();
        List<CmsArticleRecommendEntity> recommendEntityList = new ArrayList<>();
        List<Long> ids = Arrays.asList(dto.getRecommendArticleIds());
        ids.forEach(id -> {
                    CmsArticleRecommendEntity recommendEntity = new CmsArticleRecommendEntity();
                    recommendEntity.setMainArticleId(dto.getMainArticleId());
                    recommendEntity.setRecommendArticleId(id);
                    recommendEntity.setCreateDate(date);
                    recommendEntity.setUpdateDate(date);
                    recommendEntity.setCreator(userName);
                    recommendEntity.setUpdater(userName);
                    recommendEntityList.add(recommendEntity);
                }
        );
        insertBatch(recommendEntityList);
    }

    /**
     * 移除推荐
     *
     * @param ids
     */
    @Override
    @Transactional(rollbackFor = Exception.class)

    public void delete(@RequestBody Long[] ids) {
        //物理删除
        cmsArticleRecommendDao.deleteRecommends(ids);
    }

    /**
     * 前台资讯相关推荐
     *
     * @param params
     * @return
     */
    @Override

    public PageData<CmsFrontArticleRecommendDTO> pageFront(@RequestParam Map<String, Object> params) {
        int pageNum = Integer.valueOf(params.get(Constant.PAGE).toString());
        int limit = Integer.valueOf(params.get(Constant.LIMIT).toString());
        Page<CmsFrontArticleRecommendDTO> page = new Page<>(pageNum, limit);
        List<CmsFrontArticleRecommendDTO> list = cmsArticleRecommendDao.pageFront(page, params);
        return new PageData(list, page.getTotal());
    }

}

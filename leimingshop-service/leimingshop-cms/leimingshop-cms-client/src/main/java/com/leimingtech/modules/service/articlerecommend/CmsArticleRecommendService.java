/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.articlerecommend;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleAddRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsArticleRecommendSaveDTO;
import com.leimingtech.modules.dto.articlerecommend.CmsFrontArticleRecommendDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 文章推荐管理 CmsArticleRecommendService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsArticleRecommendService {

    /**
     * 资讯相关推荐
     *
     * @param params
     * @return
     */

    PageData<CmsArticleRecommendDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 资讯添加推荐列表
     *
     * @param params
     * @return
     */

    PageData<CmsArticleAddRecommendDTO> addRecommendList(@RequestParam Map<String, Object> params);

    /**
     * 新增相关推荐
     *
     * @param dto
     */

    void save(@RequestBody CmsArticleRecommendSaveDTO dto);

    /**
     * 移除推荐
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 前台资讯相关推荐
     *
     * @param params
     * @return
     */

    PageData<CmsFrontArticleRecommendDTO> pageFront(@RequestParam Map<String, Object> params);

}
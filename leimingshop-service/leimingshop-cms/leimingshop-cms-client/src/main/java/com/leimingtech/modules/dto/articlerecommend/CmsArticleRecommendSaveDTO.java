/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.articlerecommend;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author zhangyuhao
 * @email 2625024471@qq.com
 * @date 2020/1/13 15:27
 */
@Data
@ApiModel(value = "新增资讯关联文章展示类")
public class CmsArticleRecommendSaveDTO {
    /**
     * 主文章ID
     */
    private Long mainArticleId;

    /**
     * 关联文章ID
     */
    private Long[] recommendArticleIds;

}

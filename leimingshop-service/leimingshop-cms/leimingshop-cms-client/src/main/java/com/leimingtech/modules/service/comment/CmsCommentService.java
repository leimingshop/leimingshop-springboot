/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.comment;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.comment.CmsCommentDTO;
import com.leimingtech.modules.dto.comment.CmsCommentFrontPageDTO;
import com.leimingtech.modules.dto.comment.CmsCommentSaveDTO;
import com.leimingtech.modules.dto.comment.CmsCommentStatusUpdateDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 评论管理 CmsCommentService
 *
 * @author yuhaifeng yuhaifeng@leimingtech.com
 * @since v1.0.0 2019-12-31
 */

public interface CmsCommentService {

    /**
     * 资讯/圈子评论分页查询
     *
     * @param params
     * @return
     */

    PageData<CmsCommentDTO> commentList(@RequestParam Map<String, Object> params);

    /**
     * 删除评论
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 评论状态修改
     *
     * @param dto
     */

    void statusUpdate(@RequestBody CmsCommentStatusUpdateDTO dto);

    /**
     * 资讯/圈子前台评论分页查询(文章详情页评论)
     *
     * @param params
     * @return
     */

    PageData<CmsCommentFrontPageDTO> commentFrontFirstPage(@RequestParam Map<String, Object> params);

    /**
     * 资讯/圈子前台评论分页查询(二级评论页)
     *
     * @param params
     * @return
     */

    PageData<CmsCommentFrontPageDTO> commentFrontSecondPage(@RequestParam Map<String, Object> params);


    /**
     * 新增评论
     *
     * @param form
     */

    void save(@RequestBody CmsCommentSaveDTO form);

    /**
     * 删除评论(前台)
     *
     * @param ids
     * @param memberId
     * @return
     */

    Map<String, Object> deleteFront(@RequestParam(value = "ids") Long[] ids, @RequestParam(value = "memberId") Long memberId);

}

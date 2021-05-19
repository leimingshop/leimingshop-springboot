/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.feedback;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.feedback.FeedbackDTO;
import com.leimingtech.dto.feedback.SaveFeedbackDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 意见反馈信息表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2020-04-13
 */

public interface FeedbackService {
    /**
     * 反馈分页
     *
     * @param params
     * @return
     */

    PageData<FeedbackDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 查询所有
     *
     * @param params
     * @return
     */

    List<FeedbackDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 反馈详情
     *
     * @param id
     * @return
     */

    FeedbackDTO get(Long id);

    /**
     * 保存反馈信息
     *
     * @param dto
     */

    Long save(@RequestBody SaveFeedbackDTO dto);

    /**
     * 更新
     *
     * @param dto
     */

    void update(@RequestBody FeedbackDTO dto);

    /**
     * 用户删除反馈信息
     *
     * @param ids
     */

    void delete(@RequestBody Long[] ids);
}
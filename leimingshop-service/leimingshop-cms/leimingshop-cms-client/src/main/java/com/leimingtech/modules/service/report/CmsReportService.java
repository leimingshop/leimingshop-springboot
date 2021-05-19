/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.report;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.modules.dto.report.CmsArticleReportAdminInfoDTO;
import com.leimingtech.modules.dto.report.CmsReportDTO;
import com.leimingtech.modules.dto.report.CmsReportPageDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 投诉管理
 *
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-02
 */

public interface CmsReportService {
    /**
     * 举报信息分页查询
     *
     * @param params 查询参数
     * @return 返回分页信息
     */

    PageData<CmsReportPageDto> page(@RequestParam Map<String, Object> params);

    /**
     * 查询举报信息
     *
     * @param params 查询条件
     * @return 返回举报信息
     */

    List<CmsReportDTO> list(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询举报详情
     *
     * @param id 主键id
     * @return 返回详情
     */

    CmsReportDTO get(Long id);

    /**
     * 保存举报信息
     *
     * @param dto 保存参数有
     */

    void save(@RequestBody CmsReportDTO dto);

    /**
     * 更新举报信息
     *
     * @param dto 更新参数
     */

    void update(@RequestBody CmsReportDTO dto);

    /**
     * 删除信息
     *
     * @param ids 主键id
     */

    void deleteList(@RequestBody Long[] ids);

    /**
     * 根据状态更新信息
     *
     * @param params 更新条件
     */

    void statusUpdate(@RequestParam Map<String, Object> params);

    /**
     * 根据id查询举报细腻
     *
     * @param id 主键id
     * @return 返回举报信息
     */

    CmsArticleReportAdminInfoDTO getArticleReportAdmin(Long id);

    /**
     * 查询用户是否举报
     *
     * @param params 查询条件
     * @return 返回举报循序
     */

    List<CmsReportDTO> selectMemberAndFlag(@RequestParam Map<String, Object> params);
}
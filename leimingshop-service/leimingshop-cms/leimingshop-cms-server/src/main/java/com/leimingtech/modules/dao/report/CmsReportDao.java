/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao.report;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leimingtech.commons.mybatis.dao.BaseDao;
import com.leimingtech.modules.dto.report.CmsArticleReportAdminInfoDTO;
import com.leimingtech.modules.dto.report.CmsReportDTO;
import com.leimingtech.modules.dto.report.CmsReportPageDto;
import com.leimingtech.modules.entity.report.CmsReportEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 投诉管理
 *
 * @author hecheng 347861695@qq.com
 * @since v1.0.0 2020-04-02
 */
@Mapper
public interface CmsReportDao extends BaseDao<CmsReportEntity> {

    /**
     * 分页查询投诉的信息
     *
     * @param page
     * @param params
     * @return
     */
    List<CmsReportPageDto> selectReportPage(Page page, @Param("params") Map<String, Object> params);

    /**
     * 查询举报的文章相关详细信息
     *
     * @param id
     * @return
     */
    CmsArticleReportAdminInfoDTO selectArticleReportInfo(@Param("id") Long id);

    /**
     * 根据id逻辑删除
     *
     * @param id
     * @return
     */
    int updateDelById(@Param("id") Long id);

    /**
     * 查询举报信息
     *
     * @param params 查询条件
     * @return 返回举报信息
     */
    List<CmsReportDTO> selectMemberAndFlag(@Param("params") Map<String, Object> params);
}
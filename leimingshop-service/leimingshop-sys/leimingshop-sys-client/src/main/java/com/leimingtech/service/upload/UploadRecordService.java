/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.upload;


import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dto.upload.UploadRecordDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * 文件上传记录表
 *
 * @author chengqian
 * @email 543826372@gmail.com
 * @since 7.0 2019-05-24
 */

public interface UploadRecordService {

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */

    PageData<UploadRecordDTO> page(@RequestParam Map<String, Object> params);

    /**
     * 导出记录
     *
     * @param params 导出参数
     * @return
     */

    List<UploadRecordDTO> list(@RequestParam Map<String, Object> params);

    /***
     * 保存上传记录
     * @param dto 参数实体
     */

    void save(@RequestBody UploadRecordDTO dto);

    /**
     * 更新上传记录
     *
     * @param dto 参数实体
     */

    void update(@RequestBody UploadRecordDTO dto);

    /**
     * 删除上传记录
     *
     * @param ids 主键ID
     */

    void delete(@RequestBody Long[] ids);

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */

    UploadRecordDTO get(@RequestParam("id") Long id);

}
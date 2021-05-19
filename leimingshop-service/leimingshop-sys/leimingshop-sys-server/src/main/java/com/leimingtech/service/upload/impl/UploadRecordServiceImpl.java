/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.service.upload.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.leimingtech.commons.mybatis.service.impl.CrudServiceImpl;
import com.leimingtech.commons.tools.page.PageData;
import com.leimingtech.dao.uploadrecord.UploadRecordDao;
import com.leimingtech.dto.upload.UploadRecordDTO;
import com.leimingtech.entity.upload.UploadRecordEntity;
import com.leimingtech.service.upload.UploadRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
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
@Service
public class UploadRecordServiceImpl extends CrudServiceImpl<UploadRecordDao, UploadRecordEntity, UploadRecordDTO> implements UploadRecordService {
    /**
     * 条件构造器
     *
     * @param params 分页参数
     * @return
     */
    @Override
    public QueryWrapper<UploadRecordEntity> getWrapper(Map<String, Object> params) {
        String id = (String) params.get("id");

        QueryWrapper<UploadRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq(StringUtils.isNotBlank(id), "id", id);

        return wrapper;
    }

    /**
     * 分页查询信息
     *
     * @param params 分页参数
     * @return
     */
    @Override

    public PageData<UploadRecordDTO> page(@RequestParam Map<String, Object> params) {
        return super.page(params);
    }

    /**
     * 导出信息
     *
     * @param params 导出参数
     * @return
     */
    @Override

    public List<UploadRecordDTO> list(@RequestParam Map<String, Object> params) {
        return super.list(params);
    }

    /***
     * 保存上传记录
     * @param dto 参数实体
     */
    @Override

    public void save(@RequestBody UploadRecordDTO dto) {
        super.save(dto);
    }

    /**
     * 更新上传记录
     *
     * @param dto 参数实体
     */
    @Override

    public void update(@RequestBody UploadRecordDTO dto) {
        super.update(dto);
    }

    /**
     * 删除上传记录
     *
     * @param ids 主键ID
     */
    @Override

    public void delete(@RequestBody Long[] ids) {
        super.delete(ids);
    }

    /**
     * 根据ID获取信息
     *
     * @param id 主键ID
     * @return
     */
    @Override

    public UploadRecordDTO get(@RequestParam("id") Long id) {
        return super.get(id);
    }

}
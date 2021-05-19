/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.mybatis.service;

import com.leimingtech.commons.tools.page.PageData;

import java.util.List;
import java.util.Map;

/**
 * CRUD基础服务接口
 */
public interface CrudService<T, D> extends BaseService<T> {

    PageData<D> page(Map<String, Object> params);

    List<D> list(Map<String, Object> params);

    D get(Long id);

    void save(D dto);

    void update(D dto);

    void delete(Long[] ids);

}
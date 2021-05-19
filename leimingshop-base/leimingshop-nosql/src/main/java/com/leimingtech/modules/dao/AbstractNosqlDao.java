/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dao;


import com.leimingtech.modules.dto.page.PageModelDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 非关系型数据库存储抽象方法
 * @Date: 2019/8/1 15:06
 * @Version: V1.0
 */
@Component
public abstract class AbstractNosqlDao {

    /**
     * @param collectionName: 集合名称
     * @param params:         参数集合
     * @Author: SWH ab4856812@163.com
     * @Description: 通用查询抽象方法
     * @Date: 2019/8/1 15:14
     * @Version: V1.0
     */
    public abstract List<String> queryData(String collectionName, Map<String, Object> params);

    /**
     * @param collectionName:集合名称
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 增加数据（单个保存）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public abstract boolean saveData(String collectionName, Map<String, Object> params);

    /**
     * @param collectionName:集合名称
     * @param primaryKey:根据某字段查询
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 增加数据批量（增加了判断是否重复）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public abstract boolean saveDataBatch(String collectionName, String primaryKey, Map<String, Object> params);

    /**
     * @param collectionName:集合名称
     * @param primaryKey:根据某字段查询
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 修改数据（单个和批量均支持）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public abstract boolean updateData(String collectionName, String primaryKey, Map<String, Object> params);

    /**
     * @param collectionName:集合名称
     * @param params:传入参数
     * @Author: SWH ab4856812@163.com
     * @Description: 删除数据（单个删除）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public abstract boolean deleteData(String collectionName, Map<String, Object> params);

    /**
     * @param collectionName:集合名称
     * @param pageModelDTO:       封装数据
     * @Author: SWH ab4856812@163.com
     * @Description: 组合查询（查询分页数据，增加排序等）
     * @Date: 2019/8/2 11:25
     * @Version: V1.0
     */
    public abstract PageModelDTO composeQueryData(String collectionName, PageModelDTO pageModelDTO);

    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param dataList       : 数据集合(为了解耦，使用Object类型接收参数)
     * @date 2019/12/9 20:27
     * @author lixiangx@leimingtech.com
     **/
    public abstract void saveBatch(List<? extends Object> dataList, String collectionName);

    /**
     * 批量保存数据（仅保存）
     *
     * @param collectionName : 集合名称
     * @param object         : 要保存的对象
     * @date 2019年12月11日18:06:30
     * @author xuzhch
     **/
    public abstract void saveObj(Object object, String collectionName);
}

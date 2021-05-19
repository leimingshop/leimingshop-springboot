/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.store;

import com.leimingtech.modules.dao.store.StoreStatisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 店铺信息查询实现类
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Service
public class StoreStatisServiceImpl implements StoreStatisService {
    @Autowired
    private StoreStatisDao storeStatisDao;

    /**
     * 查询所有店铺ID
     *
     * @return 所有点ID集合
     * @date 2019/12/30/030 16:04
     * @author xuzhch
     **/
    //@DataSource("store")
    @Override
    public List<Long> selectList() {
        return storeStatisDao.selectList();
    }
}

package com.demo.common.service.impl

import com.demo.base.entity.BaseVO
import com.demo.base.entity.PageEntity
import com.demo.common.dao.CommonDBDao
import com.demo.common.service.CommonDBService
import org.springframework.stereotype.Service

/**
 * @Description: 公共service层实现类
 * @author demo
 * @date 2017-06-16
 * @version V1.0
 */
@Service
class CommonDBServiceImpl(val commonDBDao: CommonDBDao) : CommonDBService {

    /**
     * 插入一个对象
     */
    override fun insertEntity(baseVO: BaseVO): Boolean = commonDBDao.insertEntity(baseVO)

    /**
     * 批量插入实体
     */
    override fun insertEntities(baseVOList: List<BaseVO>): Boolean = commonDBDao.insertEntities(baseVOList)

    /**
     * 根据对象ID删除对象信息
     */
    override fun deleteEntityById(baseVO: BaseVO): Boolean = commonDBDao.deleteEntityById(baseVO)

    /**
     * 批量删除
     */
    override fun deleteEntitiesById(baseVOList: List<BaseVO>): Boolean = commonDBDao.deleteEntitiesById(baseVOList)

    /**
     * 更新一个对象信息
     */
    override fun updateEntity(baseVO: BaseVO): Boolean = commonDBDao.updateEntity(baseVO)

    /**
     * 批量更新
     */
    override fun updateEntities(baseVOList: List<BaseVO>): Boolean = commonDBDao.updateEntities(baseVOList)

    /**
     * 根据对象ID查询该对象信息
     * @return 返回查询出来的对象信息
     */
    override fun selectEntityById(baseVO: BaseVO): BaseVO? = commonDBDao.selectEntityById(baseVO)

    /**
     * 根据查询条件查询对象信息
     */
    override fun selectEntities(baseVO: BaseVO): List<BaseVO> = commonDBDao.selectEntities(baseVO)

    /**
     * 根据对象信息以及methodName查询对象信息
     */
    override fun selectEntities(baseVO: BaseVO, methodName:String): List<BaseVO> = commonDBDao.selectEntities(baseVO,methodName)

    /**
     * 根据查询条件分页查询对象信息
     */
    override fun selectEntitiesByPage(baseVO: BaseVO): PageEntity = commonDBDao.selectEntitiesByPage(baseVO)

    /**
     * 查询总条数
     */
    override fun selectTotalCount(baseVO: BaseVO): Long = commonDBDao.selectTotalCount(baseVO)

    /**
     * 根据查询条件分页查询对象信息 带合计footer
     * @Description 根据查询条件分页查询对象信息
     * @param baseVO 查询条件信息
     * @return 分页查询结果信息
     */
    override fun selectEntitiesByPageWithFooter(baseVO: BaseVO): PageEntity {
        var pageEntity = PageEntity()
        try {
            pageEntity = commonDBDao.selectEntitiesByPage(baseVO)
            pageEntity.footer = commonDBDao.selectEntities(baseVO, "selectTotalObject")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pageEntity
    }

}
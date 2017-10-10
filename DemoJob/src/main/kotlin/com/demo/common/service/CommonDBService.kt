package com.demo.common.service

import com.demo.base.entity.BaseVO
import com.demo.base.entity.PageEntity

interface CommonDBService{
    /**
     * 插入一个实体对象
     * @param baseVO 插入的对象信息
     * @return 成功返回true，失败返回false
     */
    fun insertEntity(baseVO: BaseVO): Boolean

    /**
     * 批量插入实体
     * @param baseVOList 要插入的实体集合
     * @return 成功返回true，失败返回false
     */
    fun insertEntities(baseVOList: List<BaseVO>): Boolean

    /**
     * 根据对象ID删除实体对象信息
     * @return 成功返回true，失败返回false
     */
    fun deleteEntityById(baseVO: BaseVO): Boolean

    /**
     * 批量删除
     * @param baseVOList 要删除的实体集合
     * @return 成功返回true，失败返回false
     */
    fun deleteEntitiesById(baseVOList: List<BaseVO>): Boolean

    /**
     * 更新一个实体对象信息
     * @param baseVO 需要更新的对象信息
     * @return 成功返回true，失败返回false
     */
    fun updateEntity(baseVO: BaseVO): Boolean

    /**
     * 批量更新
     * @param baseVOList 要更新的对象集合
     * @return 成功返回true，失败返回false
     */
    fun updateEntities(baseVOList: List<BaseVO>): Boolean

    /**
     * 根据对象ID查询该对象信息
     * @return 返回查询出来的对象信息
     */
    fun selectEntityById(baseVO: BaseVO): BaseVO?

    /**
     * 根据查询条件查询对象信息
     * @param baseVO 查询条件信息
     * @return 查询结果信息
     */
    fun selectEntities(baseVO: BaseVO): List<BaseVO>

    /**
     * 根据查询条件分页查询对象信息
     * @param baseVO 查询条件信息
     * @return 分页查询结果信息
     */
    fun selectEntitiesByPage(baseVO: BaseVO): PageEntity

    /**
     * 根据查询条件查询对象信息
     * @param baseVO 查询条件信息
     * @param methodName mapper中查询的方法名称
     * @return 查询结果信息
     */
    fun selectEntities(baseVO: BaseVO, methodName:String): List<BaseVO>

    /**
     * 查询总条数
     * @param baseVO 查询的对象信息
     * @return 返回总条数
     */
    fun selectTotalCount(baseVO: BaseVO): Long

    /**
     * 根据查询条件分页查询对象信息 带合计footer
     * @Description 根据查询条件分页查询对象信息
     * @param baseVO 查询条件信息
     * @return 分页查询结果信息
     */
    fun selectEntitiesByPageWithFooter(baseVO: BaseVO): PageEntity
}
package com.demo.common.dao.impl

import com.demo.base.dao.BaseDao
import com.demo.base.entity.BaseVO
import com.demo.base.entity.PageEntity
import com.demo.common.consts.CommonConsts
import com.demo.common.dao.CommonDBDao
import org.springframework.stereotype.Repository

@Repository
class CommonDBDaoImpl : BaseDao(), CommonDBDao {
    private val mapperPreName: String = "MybatisMapper"
    /**
     * 插入一个实体对象
     */
    override fun insertEntity(baseVO: BaseVO): Boolean {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.insertObject("$mapperPreName.$mapperName.insertEntity", baseVO) > 0
    }

    /**
     * 批量插入实体
     */
    override fun insertEntities(baseVOList: List<BaseVO>): Boolean {
        if (baseVOList.isNotEmpty()) {
            val mapperName = baseVOList[0].javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
            return this.batchInsertObject("$mapperPreName.$mapperName.insertEntity", baseVOList)
        }
        return false
    }

    /**
     * 根据对象ID删除实体对象信息
     */
    override fun deleteEntityById(baseVO: BaseVO): Boolean {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.deleteObject("$mapperPreName.$mapperName.deleteEntityById", baseVO) > 0
    }

    /**
     * 批量删除
     */
    override fun deleteEntitiesById(baseVOList: List<BaseVO>): Boolean {
        if (baseVOList.isNotEmpty()) {
            val mapperName = baseVOList[0].javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
            return this.batchDeleteObject("$mapperPreName.$mapperName.deleteEntityById", baseVOList)
        }
        return false
    }

    /**
     * 更新一个实体对象信息
     */
    override fun updateEntity(baseVO: BaseVO): Boolean {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.updateObject("SystemManage.$mapperName.updateEntity", baseVO) > 0
    }

    /**
     * 批量更新
     */
    override fun updateEntities(baseVOList: List<BaseVO>): Boolean {
        if (baseVOList.isNotEmpty()) {
            val mapperName = baseVOList[0].javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
            return this.batchUpdateObject("$mapperPreName.$mapperName.updateEntity", baseVOList)
        }
        return false
    }

    /**
     * 根据对象ID查询该对象信息
     */
    override fun selectEntityById(baseVO: BaseVO): BaseVO? {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.selectObject("$mapperPreName.$mapperName.selectEntityById", baseVO)
    }

    /**
     * 根据查询条件查询对象信息
     */
    override fun selectEntities(baseVO: BaseVO): List<BaseVO> {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.selectList("$mapperPreName.$mapperName.selectEntities", baseVO)
    }

    /**
     * 根据查询条件分页查询对象信息
     */
    override fun selectEntitiesByPage(baseVO: BaseVO): PageEntity {
        val pageEntity = PageEntity()
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        pageEntity.total = this.selectTotal("$mapperPreName.$mapperName.selectEntitiesTotal", baseVO)
        pageEntity.rows = this.selectList("$mapperPreName.$mapperName.selectEntities", baseVO, baseVO.pageNumber,
                baseVO.pageSize)
        return pageEntity
    }

    /**
     * 根据查询条件查询对象信息
     * @param baseVO 查询条件信息
     * @param methodName mapper中查询的方法名称
     * @return 查询结果信息
     */
    override fun selectEntities(baseVO: BaseVO, methodName: String): List<BaseVO> {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.selectList("$mapperPreName.$mapperName.$methodName", baseVO)
    }

    /**
     * 查询总条数
     */
    override fun selectTotalCount(baseVO: BaseVO): Long {
        val mapperName = baseVO.javaClass.simpleName.replace(CommonConsts.REPLACE_STR_VO, CommonConsts.REPLACE_STR_MAPPER)
        return this.selectTotal("$mapperPreName.$mapperName.selectEntitiesTotal", baseVO)
    }

}
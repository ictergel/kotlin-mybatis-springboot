package com.demo.base.dao

import org.apache.ibatis.session.ExecutorType
import org.apache.ibatis.session.RowBounds
import org.apache.ibatis.session.SqlSession
import org.mybatis.spring.support.SqlSessionDaoSupport
import org.apache.ibatis.session.SqlSessionFactory
import org.mybatis.spring.SqlSessionTemplate
import java.lang.Exception
import javax.annotation.Resource

open class BaseDao : SqlSessionDaoSupport() {

    @Resource
    override fun setSqlSessionFactory(sqlSessionFactory: SqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory)
    }

    /**
     * 插入数据
     * @param method 要执行的mapper中的id
     * @param entity 插入对象，当传入的entity为List时，批量插入
     * @return 执行影响行数 ，为0表示未执行成功 大于0表示成功
     */
    protected fun insertObject(method: String, entity: Any): Int {
        return this.sqlSession.insert(method, entity)
    }

    /**
     * 批量插入
     * @param method  要执行的mapper中的id
     * @param objList 对象列表
     * @return list集合
     */
    fun <T> batchInsertObject(method: String, objList: List<T>): Boolean {
        return this.batchObject(method, objList, "insert")
    }

    /**
     * 删除数据
     * @param method 要执行的mapper中的id
     * @param entity 删除对象条件
     * @return 执行影响行数 ，为0表示未执行成功 大于0表示成功
     */
    fun deleteObject(method: String, entity: Any): Int {
        return this.sqlSession.delete(method, entity)
    }

    /**
     * 批量删除
     * @param method  要执行的mapper中的id
     * @param objList 对象列表
     * @return 修改成功返回true，失败返回false
     */
    fun <T> batchDeleteObject(method: String, objList: List<T>): Boolean {
        return this.batchObject(method, objList, "delete")
    }

    /**
     * 修改数据
     * @param method 要执行的mapper中的id
     * @param entity 修改对象
     * @return 执行影响行数 ，为0表示未执行成功 大于0表示成功
     */
    fun updateObject(method: String, entity: Any): Int {
        return this.sqlSession.update(method, entity)
    }

    /**
     * 批量修改
     * @param method  要执行的mapper中的id
     * @param objList 对象列表
     * @return 修改成功返回true，失败返回false
     */
    fun <T> batchUpdateObject(method: String, objList: List<T>): Boolean {
        return this.batchObject(method, objList, "update")
    }

    /**
     * 批量处理数据，增加，删除，修改
     * @param method  要执行的mapper中的id
     * @param objList 对象列表
     * @param opType  操作类型，insert delete  update
     * @return 修改成功返回true，失败返回false
     */
    private fun <T> batchObject(method: String, objList: List<T>, opType: String): Boolean {
        val st = sqlSession as SqlSessionTemplate
        var sqlSession: SqlSession? = null
        var flag: Boolean
        try {
            sqlSession = st.sqlSessionFactory.openSession(ExecutorType.BATCH, false)
            for (t in objList) {
                when (opType) {
                    "insert" -> sqlSession!!.insert(method, t)
                    "update" -> sqlSession!!.update(method, t)
                    "delete" -> sqlSession!!.delete(method, t)
                    else -> {
                    }
                }
            }
            sqlSession.commit()
            sqlSession.clearCache()
            flag = true
        } catch (e: Exception) {
            flag = false
            e.printStackTrace()
        } finally {
            if (sqlSession != null)
                sqlSession.close()
        }
        return flag
    }

    /**
     * 返回指定类型的List集合
     * @param method 要执行的mapper中的id
     * @return list集合
     */
    fun <T> selectList(method: String): List<T> {
        return this.sqlSession.selectList(method)
    }

    /**
     * 返回指定类型的List集合
     * @param method 要执行的mapper中的id
     * @param entity 指定类型的数据，主要用作参数
     * @return list集合
     */
    fun <T> selectList(method: String, entity: Any): List<T> {
        return this.sqlSession.selectList(method, entity)
    }

    /**
     * 分页查询列表
     * @param method   要执行的mapper中的id
     * @param entity   指定类型的数据
     * @param pageNo   页数
     * @param pageSize 每页显示数量
     * @return list集合
     */
    fun <T> selectList(method: String, entity: T, pageNo: Int, pageSize: Int): List<T> {
        return this.sqlSession.selectList(method, entity, RowBounds(pageSize * (pageNo - 1), pageSize))
    }

    /**
     * 返回指定类型数据，返回一个对象
     * @param method 要执行的mapper中的id
     * @return 指定类型实体
     */
    fun <T> selectObject(method: String): T {
        return this.sqlSession.selectOne(method)
    }

    /**
     * 返回指定类型数据，返回一个对象
     * @param method 要执行的mapper中的id
     * @param entity 指定类型的数据
     * @return 指定类型实体
     */
    fun <T> selectObject(method: String, entity: Any): T {
        return this.sqlSession.selectOne(method, entity)
    }

    /**
     * 返回总数
     * @param method 要执行的mapper中的id
     * @param entity 指定类型的数据
     * @return 查询指定类型的数据记录总数
     */
    fun selectTotal(method: String, entity: Any): Long {
        return this.sqlSession.selectOne(method, entity)
    }
}
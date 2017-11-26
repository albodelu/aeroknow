package biz.eventually.atpl.data.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Transaction
import biz.eventually.atpl.data.db.Question
import biz.eventually.atpl.data.db.Subject
import biz.eventually.atpl.data.dto.QuestionView
import biz.eventually.atpl.data.dto.SubjectView

/**
 * Created by Thibault de Lambilly on 17/10/17.
 */
@Dao
abstract class QuestionDao : BaseDao<Question> {

    @Query("SELECT * FROM question")
    abstract fun getAll(): LiveData<List<Question>>

    @Transaction // good practice with POJO w/ @Relation Object to ensure consistency
    @Query("SELECT * FROM question WHERE topic_id = :topicId")
    abstract fun findByTopicId(topicId: Long): LiveData<List<QuestionView>>


    @Query("SELECT idWeb FROM question")
    abstract fun getIds(): List<Long>

    @Query("SELECT * FROM question WHERE idWeb = :idWeb")
    abstract fun findById(idWeb: Long): Question?
}
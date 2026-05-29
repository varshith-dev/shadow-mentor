package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatMessageDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage)

    @Query("DELETE FROM chat_messages")
    suspend fun deleteAllMessages()
}

@Dao
interface GeneratedProjectDao {
    @Query("SELECT * FROM generated_projects ORDER BY timestamp DESC")
    fun getAllProjects(): Flow<List<GeneratedProject>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: GeneratedProject)

    @Update
    suspend fun updateProject(project: GeneratedProject)

    @Query("DELETE FROM generated_projects WHERE id = :id")
    suspend fun deleteProjectById(id: Int)

    @Query("DELETE FROM generated_projects")
    suspend fun deleteAllProjects()
}

@Dao
interface LearningNodeDao {
    @Query("SELECT * FROM learning_nodes ORDER BY `order` ASC")
    fun getAllNodes(): Flow<List<LearningNode>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNodes(nodes: List<LearningNode>)

    @Update
    suspend fun updateNode(node: LearningNode)
}

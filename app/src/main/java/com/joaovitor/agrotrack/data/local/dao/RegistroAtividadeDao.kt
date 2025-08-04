package com.joaovitor.agrotrack.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.joaovitor.agrotrack.data.entity.RegistroAtividade

@Dao
interface RegistroAtividadeDao {
    @Query("SELECT * FROM registros_atividade ORDER BY dataRegistro DESC")
    fun getAllRegistros(): LiveData<List<RegistroAtividade>>

    @Query("SELECT * FROM registros_atividade WHERE id = :id")
    suspend fun getRegistroById(id: Long): RegistroAtividade?

    @Insert
    suspend fun insertRegistro(registro: RegistroAtividade): Long

    @Update
    suspend fun updateRegistro(registro: RegistroAtividade)

    @Delete
    suspend fun deleteRegistro(registro: RegistroAtividade)

    @Query("SELECT * FROM registros_atividade WHERE DATE(dataRegistro/1000, 'unixepoch') = DATE('now') ORDER BY dataRegistro DESC")
    fun getRegistrosHoje(): LiveData<List<RegistroAtividade>>
}
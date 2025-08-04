package com.joaovitor.agrotrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.joaovitor.agrotrack.data.entity.RegistroAtividade
import com.joaovitor.agrotrack.data.entity.Tarefa
import com.joaovitor.agrotrack.data.Converter
import com.joaovitor.agrotrack.data.dao.RegistroAtividadeDao
import com.joaovitor.agrotrack.data.dao.TarefaDao

@Database(
    entities = [Tarefa::class, RegistroAtividade::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converter::class)
abstract class AgroTrackDatabase : RoomDatabase() {
    abstract fun tarefaDao(): TarefaDao
    abstract fun registroAtividadeDao(): RegistroAtividadeDao

    companion object {
        @Volatile
        private var INSTANCE: AgroTrackDatabase? = null

        fun getDatabase(context: Context): AgroTrackDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AgroTrackDatabase::class.java,
                    "agrotrack_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
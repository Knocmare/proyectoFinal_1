package ruiz.angel.proyectofinal_1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ruiz.angel.proyectofinal_1.data.database.dao.EventDao
import ruiz.angel.proyectofinal_1.data.database.dao.SubtaskDao
import ruiz.angel.proyectofinal_1.data.database.dao.TaskDao
import ruiz.angel.proyectofinal_1.data.database.dao.UserDao
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity
import ruiz.angel.proyectofinal_1.data.database.entity.UserEntity

@Database(
    entities = [UserEntity::class, EventEntity::class, TaskEntity::class, SubtaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao(): EventDao
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao

    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "proyecto_final.db")
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
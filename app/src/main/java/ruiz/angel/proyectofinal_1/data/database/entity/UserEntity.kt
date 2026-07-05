package ruiz.angel.proyectofinal_1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idUser")
    val id: Long = 0,

    @ColumnInfo(name = "nameUser")
    val name: String,

    @ColumnInfo(name = "emailUser")
    val email: String,

    @ColumnInfo(name = "passwordUser")
    val password: String
)
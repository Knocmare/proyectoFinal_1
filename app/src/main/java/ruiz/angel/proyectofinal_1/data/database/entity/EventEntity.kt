package ruiz.angel.proyectofinal_1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Events",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["idUser"],
        childColumns = ["userId"]
    )],
    indices = [Index("userId")]
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idEvent")
    val id: Long = 0,

    @ColumnInfo(name = "userId")
    val userId: Long,

    @ColumnInfo(name = "nameEvent")
    val name: String,

    @ColumnInfo(name = "dateEvent")
    val date: String
)
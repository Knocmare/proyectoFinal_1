package ruiz.angel.proyectofinal_1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Tasks",
    foreignKeys = [ForeignKey(
        entity = EventEntity::class,
        parentColumns = ["idEvent"],
        childColumns = ["eventId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("eventId")]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idTask")
    val id: Long = 0,

    @ColumnInfo(name = "eventId")
    val eventId: Long,

    @ColumnInfo(name = "nameTask")
    val name: String,

    @ColumnInfo(name = "completedTask")
    val completed: Boolean
)
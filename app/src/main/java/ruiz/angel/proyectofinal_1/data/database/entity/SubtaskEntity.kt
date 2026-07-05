package ruiz.angel.proyectofinal_1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Subtasks",
    foreignKeys = [ForeignKey(
        entity = TaskEntity::class,
        parentColumns = ["idTask"],
        childColumns = ["taskId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("taskId")]
)
data class SubtaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idSubtask")
    val id: Long = 0,

    @ColumnInfo(name = "taskId")
    val taskId: Long,

    @ColumnInfo(name = "nameSubtask")
    val name: String,

    @ColumnInfo(name = "priceSubtask")
    val price: Int,

    @ColumnInfo(name = "completedSubtask")
    val completed: Boolean
)
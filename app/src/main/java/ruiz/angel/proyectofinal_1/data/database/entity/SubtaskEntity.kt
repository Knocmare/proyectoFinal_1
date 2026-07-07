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

    @ColumnInfo(name = "descriptionSubtask")
    val description: String = "",

    @ColumnInfo(name = "estimatedPriceSubtask")
    val estimatedPrice: Int,

    @ColumnInfo(name = "realPriceSubtask")
    val realPrice: Int? = null,

    @ColumnInfo(name = "placeSubtask")
    val place: String? = null,

    @ColumnInfo(name = "completedSubtask")
    val completed: Boolean
)

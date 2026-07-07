package ruiz.angel.proyectofinal_1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "PriceOptions",
    foreignKeys = [ForeignKey(
        entity = SubtaskEntity::class,
        parentColumns = ["idSubtask"],
        childColumns = ["subtaskId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("subtaskId")]
)
data class PriceOptionEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idPriceOption")
    val id: Long = 0,

    @ColumnInfo(name = "subtaskId")
    val subtaskId: Long,

    @ColumnInfo(name = "placeOption")
    val place: String,

    @ColumnInfo(name = "costOption")
    val cost: Int
)

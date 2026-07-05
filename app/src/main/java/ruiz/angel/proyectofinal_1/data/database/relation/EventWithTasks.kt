package ruiz.angel.proyectofinal_1.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ruiz.angel.proyectofinal_1.data.database.entity.EventEntity
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity

data class EventWithTasks(
    @Embedded
    val event: EventEntity,

    @Relation(entity = TaskEntity::class, parentColumn = "idEvent", entityColumn = "eventId")
    val tasks: List<TaskWithSubtasks>
)
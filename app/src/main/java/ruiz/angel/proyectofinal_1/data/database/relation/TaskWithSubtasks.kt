package ruiz.angel.proyectofinal_1.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import ruiz.angel.proyectofinal_1.data.database.entity.SubtaskEntity
import ruiz.angel.proyectofinal_1.data.database.entity.TaskEntity

data class TaskWithSubtasks(
    @Embedded
    val task: TaskEntity,

    @Relation(parentColumn = "idTask", entityColumn = "taskId")
    val subtasks: List<SubtaskEntity>
)
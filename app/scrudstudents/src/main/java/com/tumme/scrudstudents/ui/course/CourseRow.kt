package com.tumme.scrudstudents.ui.course

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tumme.scrudstudents.data.local.model.CourseEntity

/**
 * Composant affichant une ligne de cours dans la liste.
 * Affiche les informations principales (nom, ECTS, niveau).
 * Fournit des boutons d'action : voir, modifier, supprimer, partager.
 */
@Composable
fun CourseRow(
    course: CourseEntity,
    onEdit: ()->Unit,
    onDelete: ()->Unit,
    onView: ()->Unit,
    onShare: ()->Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = course.nameCourse, modifier = Modifier.weight(0.4f))
        Text(text = course.ectsCourse.toString(), modifier = Modifier.weight(0.2f))
        Text(text = course.levelCourse.value, modifier = Modifier.weight(0.2f))
        Row(modifier = Modifier.weight(0.2f), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = onView) { Icon(Icons.Default.Info, contentDescription="View") }
            IconButton(onClick = onEdit) { Icon(Icons.Default.Edit, contentDescription="Edit") }
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription="Delete") }
            IconButton(onClick = onShare) { Icon(Icons.Default.Share, contentDescription="Share") }
        }
    }
    Divider()
}

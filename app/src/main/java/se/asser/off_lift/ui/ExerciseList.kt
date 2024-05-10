package se.asser.off_lift.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ExerciseListItem(
    val name: String,
    val colorHex: Long? = null,
    val onTap: () -> Unit
)

@Composable
fun ExerciseList(items: List<ExerciseListItem>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        items.forEach { item ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 20.dp)
                    .clickable {
                        item.onTap()
                    }
            ) {
                item.colorHex?.let {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(16.dp)
                            .clip(CircleShape)
                            .background(Color(it))
                    ) {}
                }

                Text(item.name)

            }
            HorizontalDivider()
        }
    }
}
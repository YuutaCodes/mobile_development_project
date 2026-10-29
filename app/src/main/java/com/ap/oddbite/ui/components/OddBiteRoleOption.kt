package com.ap.oddbite.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role as SemanticsRole
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ap.oddbite.R
import com.ap.oddbite.ui.theme.Inter
import com.ap.oddbite.ui.theme.OddBiteTheme

@Composable
fun OddBiteRoleOption(
    title: String,
    description: String,
    @DrawableRes icon: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(16.dp)

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
            .clip(shape)
            .background(colors.surface)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) colors.onSurface else colors.outlineVariant,
                shape = shape
            )
            .selectable(selected = selected, onClick = onClick, role = SemanticsRole.RadioButton)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(colors.surfaceVariant, CircleShape)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            RadioMark(selected = selected)
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 17.sp,
                    lineHeight = 22.sp,
                    letterSpacing = (-0.085).sp
                ),
                color = colors.onSurface
            )
            Text(
                text = description,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                ),
                color = colors.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RadioMark(selected: Boolean) {
    val colors = MaterialTheme.colorScheme

    if (selected) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .background(colors.inverseSurface, CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_check),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(14.dp)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(20.dp)
                .border(1.5.dp, colors.outlineVariant, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OddBiteRoleOptionPreview() {
    OddBiteTheme(dynamicColor = false) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .selectableGroup()
        ) {
            OddBiteRoleOption(
                title = "Customer",
                description = "I want to find bags",
                icon = R.drawable.ic_person_role,
                selected = true,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
            OddBiteRoleOption(
                title = "Merchant",
                description = "I offer bags",
                icon = R.drawable.ic_store,
                selected = false,
                onClick = {},
                modifier = Modifier.weight(1f)
            )
        }
    }
}

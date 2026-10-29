package com.ap.oddbite.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ap.oddbite.ui.theme.Inter
import com.ap.oddbite.ui.theme.OddBiteTheme

enum class OddBiteButtonStyle { Primary, Secondary }

@Composable
fun OddBiteButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: OddBiteButtonStyle = OddBiteButtonStyle.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false
) {
    val colors = MaterialTheme.colorScheme
    val isPrimary = style == OddBiteButtonStyle.Primary
    val containerColor = if (isPrimary) colors.inverseSurface else colors.surface
    val contentColor = if (isPrimary) colors.inverseOnSurface else colors.onSurface

    Button(
        onClick = { if (!isLoading) onClick() },
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor
        ),
        border = if (isPrimary) null else BorderStroke(1.dp, colors.outlineVariant),
        contentPadding = PaddingValues(horizontal = 20.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = LocalContentColor.current,
                strokeWidth = 2.dp,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = text,
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    lineHeight = 20.sp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OddBiteButtonPreview() {
    OddBiteTheme(dynamicColor = false) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OddBiteButton(text = "Log in", onClick = {})
            OddBiteButton(text = "Create account", onClick = {}, style = OddBiteButtonStyle.Secondary)
            OddBiteButton(text = "Log in", onClick = {}, isLoading = true)
            OddBiteButton(text = "Log in", onClick = {}, enabled = false)
        }
    }
}

package core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.extensions.clickableNoRipple
import core.theme.ChefAiColor
import core.theme.ChefAiTextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChefAiButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .background(color = ChefAiColor.MySin, shape = RoundedCornerShape(24.dp))
            .padding(vertical = 20.dp)
            .clickableNoRipple { onClick() }
    ) {
        Text(text = text, style = ChefAiTextStyle.button)
    }
}

@Composable
@Preview
private fun ChefAiButtonPreview() {
    ChefAiButton(text = "START", onClick = {}, modifier = Modifier.width(300.dp))
}
package core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import core.theme.ChefAiColor
import core.theme.ChefAiTextStyle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChefAiTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = TextStyle.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black)
) {
    val mergedTextStyle = textStyle.merge(
        ChefAiTextStyle.body.copy(color = ChefAiColor.SilverChalice)
    )
    Box(
        modifier = modifier
            .background(shape = RoundedCornerShape(12.dp), color = ChefAiColor.MineShaft)
            .border(width = 1.dp, color = ChefAiColor.MineShaftSolid, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 17.dp, horizontal = 14.dp)
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = enabled,
            readOnly = readOnly,
            textStyle = mergedTextStyle,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            minLines = minLines,
            visualTransformation = visualTransformation,
            onTextLayout = onTextLayout,
            interactionSource = interactionSource,
            cursorBrush = cursorBrush,
            decorationBox = { innerTextField ->
                if (value.isEmpty()) {
                    Text(
                        text = hint.orEmpty(),
                        color = ChefAiColor.SilverChalice,
                        style = mergedTextStyle,
                    )
                }
                innerTextField()
            }
        )
    }
}

@Composable
@Preview
private fun ChefAiTextFieldPreview() {
    ChefAiTextField(value = "", hint = "Search", onValueChange = {}, modifier = Modifier.width(300.dp))
}
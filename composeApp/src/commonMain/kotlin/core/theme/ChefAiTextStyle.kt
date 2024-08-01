package core.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chef_ai_mobile.composeapp.generated.resources.Res
import chef_ai_mobile.composeapp.generated.resources.grotesk_wide_bold
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

@OptIn(ExperimentalResourceApi::class)
private val GroteskFontFamily
    @Composable
    get() = FontFamily(Font(resource = Res.font.grotesk_wide_bold, weight = FontWeight.ExtraBold))

@Immutable
object ChefAiTextStyle {

    val h1: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 60.sp,
            lineHeight = 60.sp,
            letterSpacing = -(4).sp,
            //fontFamily = GroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
        )

    val h2: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 48.sp,
            lineHeight = 48.sp,
            letterSpacing = -(4).sp,
            fontWeight = FontWeight.ExtraBold,
        )

    val body: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 16.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Normal,
        )

    val button: TextStyle
        @Composable
        get() = TextStyle(
            fontSize = 20.sp,
            lineHeight = 22.sp,
            //fontFamily = GroteskFontFamily,
            fontWeight = FontWeight.ExtraBold,
        )
}
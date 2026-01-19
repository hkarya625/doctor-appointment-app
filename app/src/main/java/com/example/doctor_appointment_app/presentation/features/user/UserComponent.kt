package com.arya.bookmydoc.presentation.features.user

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arya.bookmydoc.R
@Composable
fun CustomClickableText(
    modifier: Modifier = Modifier,
    normalText: String,
    clickableText: String,
    onClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        append(normalText)

        pushStringAnnotation(
            tag = "ACTION",
            annotation = clickableText
        )

        withStyle(
            style = SpanStyle(color = colorResource(R.color.teal))
        ) {
            append(clickableText)
        }
        pop()
    }

    ClickableText(
        modifier = modifier.padding(top = 50.dp, start = 50.dp),
        text = annotatedString,
        style = TextStyle.Default.copy(fontSize = 16.sp),
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "ACTION",
                start = offset,
                end = offset
            ).firstOrNull()?.let {
                onClick()
            }
        }
    )
}

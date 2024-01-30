package com.company.watchlist.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.company.watchlist.R

@Composable
fun ErrorAlertDialog(
    errorText: String? = null,
    onDismiss: (() -> Unit),
    retry: (() -> Unit)? = null,
) {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AlertDialog(
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            confirmButton = {
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(0.dp, 30.dp, 0.dp, 0.dp)
                .align(Alignment.Center),
            text = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.25f),
                        painter = painterResource(id = R.drawable.questions_amico),
                        contentDescription = "Error Image"
                    )
                    Text(
                        text = "Oops!",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = "There seems to be an error " + if (errorText != null) "- $errorText" else "" ,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(0.dp, 5.dp, 0.dp, 20.dp),
                    )
                    retry?.let {
                        Button(
                            onClick = it,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(0.dp, 0.dp, 0.dp, 5.dp)
                                .fillMaxWidth(),
                            shape = AbsoluteRoundedCornerShape(12.dp)
                        ) {
                            Text(text = "Retry")
                        }
                    }
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(),
                        shape = AbsoluteRoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Dismiss")
                    }
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            onDismissRequest = onDismiss
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ErrorAlertDialogPreview() {
    ErrorAlertDialog("You do not have network baba", {}){

    }
}


package com.openplay.screens

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openplay.viewModels.DictionaryViewModel
import androidx.compose.material3.Text as Text1

@Composable
fun DictionaryApp(viewModel: DictionaryViewModel) {
    val wordDefinition by viewModel.wordDefinition.observeAsState()
    val isLoading by viewModel.isLoading.observeAsState(false)

    var word by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = word,
            onValueChange = { word = it },
            label = { Text1("Enter word") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (word.isNotEmpty()) {
                    viewModel.fetchWordDefinition(word)
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text1("Search")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            wordDefinition?.let { definition ->
                Text1(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                            append("Word: ")
                        }
                        append(definition.word)
                        append("\n")
                        for (meaning in definition.meanings) {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${meaning.partOfSpeech}: ")
                            }
                            append(meaning.definitions.joinToString("; ") {
                                it.definition
                            })
                            append("\n")
                        }
                    },
                    modifier = Modifier.padding(8.dp)
                )
            } ?: Text1(text = "Word not found. Please try another word.", modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DictionaryApp(viewModel = DictionaryViewModel(Application()))
}
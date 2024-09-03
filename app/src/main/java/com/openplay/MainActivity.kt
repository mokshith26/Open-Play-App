package com.openplay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.openplay.screens.DictionaryApp
import com.openplay.ui.theme.OpenPlayAppTheme
import com.openplay.viewModels.DictionaryViewModel
import com.openplay.viewModels.DictionaryViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: DictionaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use the custom ViewModelFactory
        val viewModelFactory = DictionaryViewModelFactory(application)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DictionaryViewModel::class.java)

        setContent {
            OpenPlayAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    viewModel = ViewModelProvider(this).get(DictionaryViewModel::class.java)

                    setContent {
                        DictionaryApp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OpenPlayAppTheme {
        Greeting("Android")
    }
}
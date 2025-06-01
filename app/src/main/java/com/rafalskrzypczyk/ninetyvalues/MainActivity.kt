package com.rafalskrzypczyk.ninetyvalues

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rafalskrzypczyk.ninetyvalues.ui.theme.NinetyValuesTheme
import com.rafalskrzypczyk.room.data.dao.ValueDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var valueDao: ValueDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NinetyValuesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ValuesScreen(
                        dao = valueDao,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ValuesScreen(
    dao: ValueDao,
    modifier: Modifier = Modifier
) {
    val valuesState = dao.getAllValues().collectAsState(initial = emptyList())
    val values = valuesState.value

    LazyColumn(modifier = modifier.fillMaxSize().padding(16.dp)) {
        items(values) { value ->
            Text(text = value.name)
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
    NinetyValuesTheme {
        Greeting("Android")
    }
}
package com.protodatastoredemo

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.protodatastoredemo.storagemanager.DataStoreSessionHandler
import com.protodatastoredemo.ui.theme.ProtoDataStoreDemoTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    var dataStoreSessionHandler = DataStoreSessionHandler(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val dataSaved = MutableLiveData<String>()

        GlobalScope.launch(Dispatchers.IO) {
            dataStoreSessionHandler.getCurrentUser().collect {
                Log.d("PROTO DS","GET>"+it.userName)
                dataSaved.postValue(
                    it.userName
                )
            }
        }
        setContent {
            ProtoDataStoreDemoTheme{
                SetBarColor(color = MaterialTheme.colorScheme.background)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(
                        this,dataStoreSessionHandler,dataSaved
                    )
                }
            }
        }
    }
}

@Composable
private fun SetBarColor(color: Color) {
//    val systemUiController = rememberSystemUiController()
//    SideEffect {
//        systemUiController.setStatusBarColor(color = color)
//    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(title : String) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onSecondary
        ),
        title = { Text(title) }
    )
}
@Composable
fun HomeScreen(
    context: MainActivity,
    dataStoreSessionHandler: DataStoreSessionHandler,
    dataSaved: MutableLiveData<String>
) {
    var nameState by remember {
        mutableStateOf(TextFieldValue())
    }
    var nameSavedState by remember {
        mutableStateOf("Alice")
    }

    dataSaved.observe(context) {
        Log.d("PROTO DS","OBSERVE>"+it)
        nameSavedState = it
    }


    Scaffold( topBar = { AppBar("Proto DataStore") },
        content = {paddingValues -> Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            TextField(
                value = nameState,
                onValueChange = { nameState = it },
                label = { Text(text = "Enter UserName") },
                modifier = Modifier
                    .padding(vertical = 15.dp)
                    .fillMaxWidth()
            )

            Button( modifier = Modifier
                .padding(vertical = 15.dp)
                .fillMaxWidth(),onClick = {
                GlobalScope.launch(Dispatchers.IO) {
                    Log.d("PROTO DS","SAVE >"+nameState.text)
                    dataStoreSessionHandler.setCurrentUser(nameState.text)
                }
            }) {
                Text(text = "Save")
            }


            Text(text = nameSavedState)
        }
        })
}
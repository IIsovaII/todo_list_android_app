package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter

var stickyFooterHeight = 70

class MainActivity : ComponentActivity() {
    private var state = mutableStateOf("home");

    companion object {
        private const val REQUEST_CODE_LOAD_JSON = 1
        private const val REQUEST_CODE_SAVE_JSON = 2
    }

    lateinit var listName: String
    lateinit var resultStr: String
    var allTasks: MutableMap<String, MutableMap<String, String>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen()
        }
    }

    @Composable
    fun Screen() {
        if (state.value == "home")
            MainButtons()
        else
            ListSheet()
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ListSheet() {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                stickyHeader() {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.LightGray),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            state.value = "home"
                            allTasks = mutableMapOf()
                        }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                        IconButton(onClick = {
                            // TODO
                            val resJson = JSONObject()
                            resJson.put("name", listName)
                            val tasksJSON = JSONArray(allTasks.values)
                            Log.i("tasks", tasksJSON.toString())
                            resJson.put("tasks", tasksJSON)
                            resultStr = resJson.toString()
                            Log.i("file to save", resultStr)

                            val saveFileIntent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "file/json"
                                putExtra(Intent.EXTRA_TITLE, "newBoard.json")
                            }
                            startActivityForResult(saveFileIntent, REQUEST_CODE_SAVE_JSON)
                        }) {
                            Icon(Icons.Filled.Share, contentDescription = "Save")
                        }
                    }
                }


                item {
                    Text(text = listName, fontSize = 40.sp, modifier = Modifier.padding(16.dp))
                }
                for (t in allTasks.values) {
                    item {
                        ListItem(
                            name = t["name"].toString(),
                            desc = t["description"].toString(),
                            checked = t["done"].toString() == "done"
                        )
                    }
                }
                item {
                    Spacer(
                        modifier = Modifier.height(
                            with(LocalDensity.current) {
                                stickyFooterHeight.toDp()
                            }
                        )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .onSizeChanged {
                        stickyFooterHeight = it.height
                    }
            ) {

                IconButton(onClick = {
                    // TODO
//                    allTasks.put((allTasks.keys.maxOrNull()?:0).toString(), {"name" = "Task", "descriptio" = "...", "status" = "not done"}.toString())
                }) {
                    Icon(Icons.Filled.AddCircle, contentDescription = "Add task", Modifier.size(70.dp))
                }
            }
        }
    }

    @Composable
    private fun ListItem(name: String, desc: String, checked: Boolean) {
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .clickable { Log.d("MyLog", "click") },
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier
                            .padding(start = 0.dp)
                            .weight(1f)
                    ) {
                        Text(
                            text = name,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                        Text(
                            text = desc,
                            fontSize = 12.sp,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 2
                        )
                    }

                    Checkbox(
                        checked = checked,
                        onCheckedChange = {/*TODO*/ },
                        Modifier.weight(0.2f),
                    )
                }
            }
        }
    }


    @Composable
    fun MainButtons() {
        Column(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "TO-DO list", fontSize = 60.sp)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    val getFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "application/json"
                    }
                    startActivityForResult(getFileIntent, REQUEST_CODE_LOAD_JSON)
                }) {
                    Text(text = "Load", fontSize = 30.sp)
                }
                Button(onClick = {
                    listName = "New list"
                    allTasks = mutableMapOf()
                    state.value = "list"
                }) {
                    Text(text = "New", fontSize = 30.sp)
                }
            }

        }
    }

    @Composable
    fun RedactionCard(t1: String, t2: String) {
        var text1 by remember { mutableStateOf(TextFieldValue(t1)) }
        var text2 by remember { mutableStateOf(TextFieldValue(t2)) }

        Column {
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Name") }
            )
            TextField(
                value = text2,
                onValueChange = { text2 = it },
                label = { Text("Description") }
            )

            Row {
                Button(onClick = { /* Handle button 1 click */ }) {
                    Text("Delete")
                }
                Button(onClick = { /* Handle button 2 click */ }) {
                    Text("Apply")
                }
            }
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_LOAD_JSON && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val file = File(uri.path!!)

                val stringBuilder = StringBuilder()
                contentResolver.openInputStream(uri).use { inputStream ->
                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
                        var line: String? = reader.readLine()
                        while (line != null) {
                            stringBuilder.append(line)
                            line = reader.readLine()
                        }
                    }
                }

                val jsonObject = JSONObject(stringBuilder.toString())
                listName = jsonObject.get("name") as String
                Log.i("name", listName)
                val tasks: JSONArray = jsonObject.getJSONArray("tasks")

                for (i in 0..<tasks.length()) {
                    allTasks[i.toString()] = mutableMapOf(
                        "name" to (tasks.get(i) as JSONObject).get("name") as String,
                        "description" to (tasks.get(i) as JSONObject).get("description") as String,
                        "status" to (tasks.get(i) as JSONObject).get("status") as String
                    )
                }

                Log.i("tag", allTasks.toString())
                state.value = "list"
            }
        } else if (requestCode == REQUEST_CODE_SAVE_JSON && resultCode == RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                val outputStream = OutputStreamWriter(contentResolver.openOutputStream(uri))
                outputStream.write(resultStr)
                outputStream.close()
            }
        }
    }
}



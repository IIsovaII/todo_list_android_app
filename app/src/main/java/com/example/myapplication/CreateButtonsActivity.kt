//package com.example.myapplication
//
//import android.app.Activity
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import org.json.JSONArray
//import org.json.JSONObject
//import java.io.BufferedReader
//import java.io.File
//import java.io.InputStreamReader
//
//
//class CreateButtonsActivity : ComponentActivity() {
//    companion object {
//        private const val REQUEST_CODE_LOAD_JSON = 1
//        private const val REQUEST_CODE_SAVE_JSON = 2
//    }
//
//    lateinit var listName: String
//    var allTasks: MutableMap<String, MutableMap<String, String>> = mutableMapOf()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MainButtons()
//        }
//    }
//
//    @Composable
//    fun MainButtons() {
//        Column(
//            modifier = Modifier
//                .background(Color.White)
//                .fillMaxSize(),
//            verticalArrangement = Arrangement.SpaceEvenly,
//            horizontalAlignment = Alignment.CenterHorizontally,
//            ) {
//            Text(text = "TO-DO list", fontSize = 60.sp)
//
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally,
//                modifier = Modifier.height(200.dp),
//                verticalArrangement = Arrangement.SpaceEvenly
//            ) {
//                Button(onClick = {
//                    val getFileIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
//                        addCategory(Intent.CATEGORY_OPENABLE)
//                        type = "application/json"
//                    }
//                    startActivityForResult(getFileIntent, REQUEST_CODE_LOAD_JSON)
//                }) {
//                    Text(text = "Load", fontSize = 30.sp)
//                }
//                Button(onClick = { /*TODO*/ }) {
//                    Text(text = "New", fontSize = 30.sp)
//                }
//            }
//
//        }
//    }
//
//    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == REQUEST_CODE_LOAD_JSON && resultCode == RESULT_OK) {
//            val uri = data?.data
//            if (uri != null) {
//                val file = File(uri.path!!)
//
//
//                val stringBuilder = StringBuilder()
//                contentResolver.openInputStream(uri).use { inputStream ->
//                    BufferedReader(InputStreamReader(inputStream)).use { reader ->
//                        var line: String? = reader.readLine()
//                        while (line != null) {
//                            stringBuilder.append(line)
//                            line = reader.readLine()
//                        }
//                    }
//                }
//
//                val jsonObject = JSONObject(stringBuilder.toString())
//                listName = jsonObject.get("name") as String
//                Log.i("name", listName)
//                val tasks: JSONArray = jsonObject.getJSONArray("tasks")
//
//                for (i in 0..<tasks.length()) {
//                    allTasks.put(
//                        i.toString(), mutableMapOf(
//                            "name" to (tasks.get(i) as JSONObject).get("name") as String,
//                            "description" to (tasks.get(i) as JSONObject).get("description") as String,
//                            "status" to (tasks.get(i) as JSONObject).get("status") as String
//                        )
//                    )
//                }
//
//                Log.i("tag", allTasks.toString())
//            }
//        }
//    }
//}
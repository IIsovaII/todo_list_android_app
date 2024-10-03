package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.retrofitSingleton.Companion.apiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.UUID

var stickyFooterHeight = 70


class MainActivity : ComponentActivity() {
    private var state = mutableStateOf("home")

    private var allTasks = mutableStateOf(listOf<TaskItem>())

    var retrofit = retrofitSingleton.getApiInterface()

    fun getTasks() {
        apiInterface.getList().enqueue(object : Callback<List<TaskItem>> {
            override fun onResponse(
                call: Call<List<TaskItem>>,
                response: Response<List<TaskItem>>
            ) {
                if (response.body() == null) {
                    allTasks.value = listOf<TaskItem>()
                } else
                    allTasks.value = response.body()!!.toList()
            }

            override fun onFailure(p0: Call<List<TaskItem>>, p1: Throwable) {
            }
        })
    }

    private fun editTask(id: String, ticket: TaskItem) {
        val call = apiInterface.changeTask(id, ticket)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(p0: Call<Boolean>, p1: Response<Boolean>) {
                state.value = "list2"
            }

            override fun onFailure(p0: Call<Boolean>, p1: Throwable) {
                state.value = "list2"
            }

        })
    }

    private fun addTask() {
        val newTicket = TaskItem(
            id = UUID.randomUUID().toString(),
            name = "new task",
            isComplete = false
        )

        apiInterface
            .loadTask(newTicket)
            .enqueue(object : Callback<Boolean> {
                override fun onResponse(p0: Call<Boolean>, p1: Response<Boolean>) {
                    state.value = "list2"
                }

                override fun onFailure(p0: Call<Boolean>, p1: Throwable) {
                }
            })
    }

    private fun deleteTask(id: String) {
        val call = apiInterface.deleteTask(id)
        call.enqueue(object : Callback<Boolean> {
            override fun onResponse(p0: Call<Boolean>, p1: Response<Boolean>) {
                state.value = "list2"
            }

            override fun onFailure(p0: Call<Boolean>, p1: Throwable) {

            }

        })
    }

    companion object {
        private const val REQUEST_CODE_LOAD_JSON = 1
        private const val REQUEST_CODE_SAVE_JSON = 2
    }

    private lateinit var editT: TaskItem
    lateinit var resultStr: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Screen()
        }
    }

    @Composable
    fun Screen() {
        getTasks()
        if (state.value == "edit")
            RedactionCard()
        else {
            ListSheet()
            state.value = "list"
        }

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
                            allTasks.value = listOf()
                        }) {
                            Icon(Icons.Filled.Refresh, contentDescription = "Back")
                        }
                    }
                }

                for (t in allTasks.value) {
                    item {
                        ListItem(
                            id = t.id,
                            name = t.name,
                            checked = (t.isComplete)
                        )
                        Log.i("TAG", "ListSheet: ${t.isComplete}")
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
                FloatingActionButton(onClick = { addTask() }) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun ListItem(id: String, name: String, checked: Boolean) {
        fun check(id: String) {
            val call = apiInterface.changeStatus(id)
            call.enqueue(object : Callback<Boolean> {
                override fun onResponse(p0: Call<Boolean>, p1: Response<Boolean>) {
                }

                override fun onFailure(p0: Call<Boolean>, p1: Throwable) {
                }

            })
        }


        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        editT = TaskItem(id, name, checked)
                        state.value = "edit"
                    },
                    onLongClick = {
                        deleteTask(id)
                    }
                ),
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
                    }
                    Checkbox(

                        checked = checked,
                        onCheckedChange = {
                            check(id)
                            state.value = "list2"
                        },
                        Modifier.weight(0.2f),
                    )
                }
            }
        }
    }


    @Composable
    fun RedactionCard() {
        var text1 by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Enter new name of task", fontSize = 40.sp)
            TextField(
                value = text1,
                onValueChange = { text1 = it },
                label = { Text("Name") }
            )

            Row {
                Button(onClick = { state.value = "list" }) {
                    Text("Cansel")
                }
                Button(onClick = {
                    editTask(
                        editT.id,
                        TaskItem(id = editT.id, name = text1.text, isComplete = editT.isComplete)
                    )

                    state.value = "list"
                }) {
                    Text("Apply")
                }
            }
        }
    }

}



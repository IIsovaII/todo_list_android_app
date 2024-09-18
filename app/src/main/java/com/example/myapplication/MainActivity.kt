package com.example.myapplication

import android.graphics.drawable.Icon
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.materialIcon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

var stickyFooterHeight = 30

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ListSheet()
        }
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
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                    IconButton(onClick = { /* doSomething() */ }) {
                        Icon(Icons.Filled.Share, contentDescription = "Save")
                    }
                }
            }
            item {
                Text(text = "Test TO-DO list", fontSize = 40.sp)
            }
            items(7) { index ->

                ListItem(
                    name = "Task1",
                    desc = "Some text about something... La-la-la... Many symbols: sjidsjjfsjfisjdf shfsjsjdfj pjijsd dsjidjjsdlfk kjdfjf kd",
                    checked = true
                )
                ListItem(name = "Task2", desc = "...", checked = false)

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
                .background(Color.Gray)
        ) {
            Button(onClick = { /*TODO*/ }) {
                Text(text = "+")
            }
        }
    }
}

@Composable
private fun ListItem(name: String, desc: String, checked: Boolean) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
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

                Column(modifier = Modifier
                    .padding(start = 0.dp)
                    .weight(1f)) {
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

                Checkbox(checked = checked, onCheckedChange = {/*TODO*/ }, Modifier.weight(0.2f), )
            }
        }
    }
}
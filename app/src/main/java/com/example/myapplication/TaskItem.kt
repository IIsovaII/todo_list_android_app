package com.example.myapplication

class TaskItem(
    var id: String,
    var name: String,
    var isComplete: Boolean
) {
    fun copy(id: String, name: String, isComplete: Boolean): TaskItem {
        return TaskItem(id, name, isComplete)
    }


}
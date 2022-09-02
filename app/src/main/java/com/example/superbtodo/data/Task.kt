package com.example.superbtodo.data
// Task data class is also an entity representing a table within the database
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "task_table")
data class Task(
    //id is the PrimaryKey which is auto generated by Android Room Database
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val date: String,
    val content: String,
    var timeLeft: String,
    var isDone: Boolean
):Parcelable

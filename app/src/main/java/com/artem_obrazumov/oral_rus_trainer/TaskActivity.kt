package com.artem_obrazumov.oral_rus_trainer

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.artem_obrazumov.oral_rus_trainer.Models.TaskSetModel
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskActivity : AppCompatActivity() {
    private val wrappersList: ArrayList<ConstraintLayout> = ArrayList()
    private var rightButton: ImageView? = null
    private var leftButton: ImageView? = null
    private var textSwitchButton: Button? = null
    private lateinit var finishButton: Button
    private var text1Repeat: TextView? = null
    private lateinit var task: TaskSetModel
    private var currentIndex = 0
    private var task3Select = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        resetState(savedInstanceState)

        wrappersList.add(findViewById(R.id.task1wrapper))
        wrappersList.add(findViewById(R.id.task2wrapper))
        wrappersList.add(findViewById(R.id.task3selectwrapper))
        wrappersList.add(findViewById(R.id.task3wrapper))
        wrappersList.add(findViewById(R.id.task4wrapper))

        initializeAppBar()
        initializeButtons()
        initializeTextSwitchButton()
        initializeTask3Select()
        updateCard()
        getTask()
    }

    private fun initializeAppBar() {
        supportActionBar!!.setTitle("")
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initializeButtons() {
        rightButton = findViewById(R.id.arrow_right)
        leftButton = findViewById(R.id.arrow_left)
        rightButton!!.setOnClickListener { cardGoRight() }
        leftButton!!.setOnClickListener { cardGoLeft() }
        finishButton = findViewById(R.id.button_finish)
        finishButton!!.setOnClickListener { finish() }
    }

    private fun initializeTextSwitchButton() {
        textSwitchButton = findViewById(R.id.text_switch_button)
        text1Repeat = findViewById(R.id.task1text_repeat)
        textSwitchButton!!.setOnClickListener {
            if (text1Repeat!!.visibility == View.GONE) {
                text1Repeat!!.visibility = View.VISIBLE
                textSwitchButton!!.text = "Скрыть текст"
            } else {
                text1Repeat!!.visibility = View.GONE
                textSwitchButton!!.text = "Показать текст"
            }
        }
    }

    private fun initializeTask3Select() {
        val task3Select1: Button = findViewById(R.id.task3_1)
        task3Select1.setOnClickListener { task3Select(1) }
        val task3Select2: Button = findViewById(R.id.task3_2)
        task3Select2.setOnClickListener { task3Select(2) }
        val task3Select3: Button = findViewById(R.id.task3_3)
        task3Select3.setOnClickListener { task3Select(3) }
    }

    private fun task3Select(selection: Int) {
        task3Select = selection
        initializeLastTasks()
        if (currentIndex == 2) {
            cardGoRight()
        }
    }

    private fun cardGoLeft() {
        if (currentIndex > 0) {
            currentIndex--
            updateCard()
        }
    }

    private fun cardGoRight() {
        if (currentIndex < wrappersList.size-1 && task3Selected(currentIndex)) {
            currentIndex++
            updateCard()
        }
    }

    private fun task3Selected(currentIndex: Int): Boolean {
        if (wrappersList[currentIndex].id == R.id.task3selectwrapper && task3Select == 0) {
            return false
        }
        return true
    }

    private fun updateCard() {
        for(index in 0 until wrappersList.size) {
            if (index != currentIndex) {
                wrappersList[index].visibility = View.GONE
            } else {
                wrappersList[index].visibility = View.VISIBLE
            }
        }
    }

    private fun getTask() {
        val taskId = intent.extras?.get("TaskId")
        FirebaseDatabase.getInstance().getReference("taskSets/"+taskId)
            .addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    task = snapshot.getValue(TaskSetModel::class.java)!!
                    initializeTask(task)
                    task3Select(task3Select)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun initializeTask(task: TaskSetModel) {
        val text1Task: TextView = findViewById(R.id.task1)
        text1Task.append(" "+task.text1Task)
        val text1Text: TextView = findViewById(R.id.task1text)
        val text1TextRepeat: TextView = findViewById(R.id.task1text_repeat)
        text1Text.text = task.text1
        text1TextRepeat.text = task.text1
        val text1Image: ImageView = findViewById(R.id.task1Image)
        Glide.with(this).load(task.text1Image)
            .into(text1Image)
        val task2Text: TextView = findViewById(R.id.task2text)
        task2Text.text = task.task2
    }

    private fun initializeLastTasks() {
        val task3: TextView = findViewById(R.id.task3)
        val task3Image: ImageView = findViewById(R.id.task3Image)
        val task3text: TextView = findViewById(R.id.task3text)
        val task4text: TextView = findViewById(R.id.task4text)
        Glide.with(this).load(task.task3Image)
            .into(task3Image)
        if (task3Select == 1) {
            task3.text = "Задание 3. Описание картинки"
            task3Image.visibility = View.VISIBLE
            task3text.text = task.task3ImageText.replace("/n", "\n")
            task4text.text = task.Questions1.replace("/n", "\n")
        } else if (task3Select == 2) {
            task3.text = "Задание 3. Повествование на основе жизненного опыта"
            task3Image.visibility = View.GONE
            task3text.text = task.task3StoryText.replace("/n", "\n")
            task4text.text = task.Questions2.replace("/n", "\n")
        } else if (task3Select == 3) {
            task3.text = "Задание 3. Рассуждение по поставленному вопросу"
            task3Image.visibility = View.GONE
            task3text.text = task.task3SpeakingText.replace("/n", "\n")
            task4text.text = task.Questions3.replace("/n", "\n")
        }
    }

    private fun resetState(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt("index")
            task3Select = savedInstanceState.getInt("task3select")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("index", currentIndex)
        outState.putInt("task3select", task3Select)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }
}
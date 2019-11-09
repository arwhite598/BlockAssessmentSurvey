package com.example.blockassessmentsurvey

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
//import kotlinx.android.synthetic.main.activity_counter.*

class CounterActivity : AppCompatActivity() {

    private var questionText: TextView? = null
    private var countText: EditText? = null
    private var addCount: ImageButton? = null
    private var removeCount: ImageButton? = null
    private var doneBtn: ImageButton? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_counter)
        //setSupportActionBar(toolbar)

        intent = getIntent()

        questionText = findViewById(R.id.holder)
        questionText?.text = intent.getStringExtra(QUESTION_STRING)

        countText = findViewById(R.id.counter)

        addCount = findViewById(R.id.add)
        removeCount = findViewById(R.id.remove)

        doneBtn = findViewById(R.id.done)

        addCount?.setOnClickListener({
            val txt = countText?.text.toString()

            if(txt.compareTo("") == 0){
                countText?.setText(1.toString())
            }
            else {
                var cnt = txt.toInt()
                cnt = cnt + 1
                countText?.setText(cnt.toString())
            }
        })

        removeCount?.setOnClickListener({

            val txt = countText?.text.toString()
            if(txt.compareTo("") == 0){
                countText?.setText("0")
            }
            else {
                var cnt = txt.toInt()

                if(cnt <= 0){
                    cnt = 0
                }

                else {
                    cnt = cnt - 1
                }
                countText?.setText(cnt.toString())
            }
        })

        //When the user clicks on the done button returns to survey manager
        doneBtn?.setOnClickListener({

            val data = Intent()

            // puts the counter in the intent
            data.putExtra(QANSWER_STRING,countText?.text.toString())

            //put the question id in the intent
            data.putExtra(QID_STRING,intent.getStringExtra(QID_STRING))

            setResult(Activity.RESULT_OK,data)
            finish()
        })

    }

    companion object {

        private val QUESTION_STRING = "questionstring"
        private val QANSWER_STRING = "qanswer"
        private val QID_STRING = "qid"
    }

}
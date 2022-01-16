package org.chenx6.easydict

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import kotlinx.coroutines.*

class SelectedTextProcessActivity : Activity() {
    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selected_text_process)
        // Get result showing method
        val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val processMethod = prefManager.getString("result_show", "toast")!!
        // Get query word from intent
        val word = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        mainScope.launch {
            // Query word
            val resultList = withContext(Dispatchers.IO) {
                WordDatabase.getInstance(applicationContext).getWordDao().searchWord(word, 1)
            }
            val text = if (resultList.isEmpty()) {
                "没有查续到 $word"
            } else {
                resultList[0].translation?.replace("\\n", "\n") ?: "$word 缺少翻译"
            }
            // Show the result in different method
            when (processMethod) {
                "toast" -> {
                    val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
                    toast.show()
                    finish()
                }
                "float" -> {
                    val selectedWordView: TextView = findViewById(R.id.selectedWordView)
                    val selectedTranslateView: TextView = findViewById(R.id.selectedTranslateView)
                    val closeResultButton: ImageButton = findViewById(R.id.closeResultButton)
                    selectedWordView.text = word
                    selectedTranslateView.text = text
                    closeResultButton.setOnClickListener { finish() }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}
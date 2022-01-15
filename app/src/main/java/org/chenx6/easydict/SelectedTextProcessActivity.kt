package org.chenx6.easydict

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import kotlinx.coroutines.*

class SelectedTextProcessActivity : Activity() {
    private val mainScope = MainScope()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_selected_text_process)
        val word = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT).toString()
        mainScope.launch {
            val resultList = withContext(Dispatchers.IO) {
                WordDatabase.getInstance(applicationContext).getWordDao().searchWord(word, 1)
            }
            val text = if (resultList.isEmpty()) {
                "没有查续到 $word"
            } else {
                resultList[0].translation?.replace("\\n", "\n") ?: "$word 缺少翻译"
            }
            val toast = Toast.makeText(applicationContext, text, Toast.LENGTH_LONG)
            toast.show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainScope.cancel()
    }
}
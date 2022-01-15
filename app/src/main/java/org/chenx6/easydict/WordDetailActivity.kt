package org.chenx6.easydict

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chenx6.easydict.databinding.ActivityWordDetailBinding
import org.chenx6.easydict.entities.WordEntity
import org.chenx6.easydict.entities.WordFavorite
import org.chenx6.easydict.entities.WordHistory

class WordDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWordDetailBinding
    private lateinit var selectedWord: WordEntity

    private val tagReplaceTable = hashMapOf(
        "zk" to "中学",
        "gk" to "高中",
        "ky" to "研考",
        "cet4" to "四级",
        "cet6" to "六级",
        "toefl" to "托福",
        "ielts" to "雅思",
        "gre" to "GRE"
    )
    private val exchangeTable = hashMapOf(
        "p" to "过去式",
        "d" to "过去分词",
        "i" to "现在分词",
        "3" to "第三人称单数",
        "r" to "比较级",
        "t" to "最高级",
        "s" to "复数",
        "0" to "原型",
        "1" to "类别"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Top app Bar
        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        // Intent
        val word = intent.getStringExtra("word") ?: "abandon"
        lifecycleScope.launch {
            // Fetch data from db and render
            val database = getDatabase()
            val resultList = withContext(Dispatchers.IO) {
                database.searchWord(word)
            }
            selectedWord = resultList[0]
            initCard(selectedWord)
            // Add history
            withContext(Dispatchers.IO) {
                database.addHistory(
                    WordHistory(
                        selectedWord.id,
                        System.currentTimeMillis().toInt()
                    )
                )
            }
            // Replace favorite button's status
            withContext(Dispatchers.IO) {
                setFavoriteIcon(database.isFavorite(selectedWord.id) != null)
            }
        }
        // Favorite button
        binding.favoriteButton.setOnClickListener {
            lifecycleScope.launch {
                val database = getDatabase()
                // if this word is favorite
                if (database.isFavorite(selectedWord.id) == null) {
                    database.addFavorite(
                        WordFavorite(
                            selectedWord.id,
                            System.currentTimeMillis().toInt()
                        )
                    )
                    setFavoriteIcon(true)
                    Toast.makeText(applicationContext, "收藏成功", Toast.LENGTH_SHORT).show()
                } else {
                    database.deleteFavorite(
                        WordFavorite(
                            selectedWord.id,
                            System.currentTimeMillis().toInt()
                        )
                    )
                    setFavoriteIcon(false)
                    Toast.makeText(applicationContext, "取消收藏成功", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initCard(word: WordEntity) {
        binding.wordCardView.text = word.word
        binding.phoneticCardView.text = word.phonetic
        binding.definitionCardView.text = word.definition?.replace("\\n", "\n")
        binding.translationCardView.text = word.translation?.replace("\\n", "\n")
        binding.exchangeView.text = parseExchange(word.exchange)
        binding.collinsCardView.text = "${word.collins} 星" // TODO
        binding.oxfordCardView.text = if (word.oxford == 0) "否" else "是"
        binding.tagsCardView.text = parseTags(word.tag)
        binding.bncCardView.text = word.bnc?.toString() ?: "无"
        binding.frqCardView.text = word.frq?.toString() ?: "无"
    }

    private fun getDatabase() =
        WordDatabase.getInstance(applicationContext).getWordDao()

    private fun setFavoriteIcon(isFavorite: Boolean) =
        binding.favoriteButton.setImageResource(
            if (isFavorite) {
                R.drawable.ic_baseline_star_24
            } else {
                R.drawable.ic_baseline_star_border_24
            }
        )

    private fun parseExchange(exchange: String?): String =
        if (exchange == null || exchange.isEmpty()) {
            "无"
        } else {
            exchange
                .split("/")
                .joinToString("\n") {
                    val (pre, word) = it.split(":")
                    arrayOf(exchangeTable[pre] ?: "", word).joinToString()
                }
        }

    private fun parseTags(tag: String?): String =
        if (tag == null || tag.isEmpty()) {
            "无"
        } else {
            tag.split(" ")
                .map { tagReplaceTable[it] }
                .joinToString(", ")
        }
}
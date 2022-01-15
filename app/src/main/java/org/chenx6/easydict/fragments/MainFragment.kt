package org.chenx6.easydict.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.chenx6.easydict.R
import org.chenx6.easydict.WordAdapter
import org.chenx6.easydict.WordDatabase
import org.chenx6.easydict.entities.WordEntity

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    private lateinit var recycleView: RecyclerView
    private lateinit var searchWordView: SearchView
    lateinit var wordAdapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycleView = view.findViewById(R.id.wordListView)
        searchWordView = view.findViewById(R.id.searchWordView)
        lifecycleScope.launch {
            // Recycle View
            val initDataset: Array<WordEntity> = withContext(Dispatchers.IO) {
                getHistory().toTypedArray()
            }
            wordAdapter = WordAdapter(requireContext(), initDataset)
            val layout = LinearLayoutManager(requireContext())
            recycleView.layoutManager = layout
            recycleView.adapter = wordAdapter
        }

        // Search View
        // searchWordView.onActionViewExpanded()  // Don't close view when view is empty
        searchWordView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                lifecycleScope.launch {
                    val result = withContext(Dispatchers.IO) {
                        if (p0 == null || p0.isEmpty()) {
                            getHistory()
                        } else {
                            searchWords(p0)
                        }
                    }
                    wordAdapter.updateDataset(result.toTypedArray())
                }
                return true
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MainFragment()
    }

    suspend fun searchWords(word: String): List<WordEntity> =
        WordDatabase.getInstance(requireContext()).getWordDao().searchWord(word)

    suspend fun getHistory(): List<WordEntity> =
        WordDatabase
            .getInstance(requireContext())
            .getWordDao()
            .getHistory()
            .sortedByDescending { it.wordHistory.queryTime }
            .map { it.word }
}
package org.chenx6.easydict.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
 * Use the [FavoriteWordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoriteWordFragment : Fragment() {
    private lateinit var favoriteWordRecyclerView: RecyclerView
    private lateinit var wordAdapter: WordAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_word, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteWordRecyclerView = requireView().findViewById(R.id.favoriteWordRecycleView)
        lifecycleScope.launch {
            val initDataset = withContext(Dispatchers.IO) {
                getFavorite()
            }
            wordAdapter = WordAdapter(requireContext(), initDataset)
            val layout = LinearLayoutManager(requireContext())
            favoriteWordRecyclerView.layoutManager = layout
            favoriteWordRecyclerView.adapter = wordAdapter
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FavoriteWordFragment.
         */
        @JvmStatic
        fun newInstance() =
            FavoriteWordFragment()
    }

    private suspend fun getFavorite() =
        WordDatabase
            .getInstance(requireContext())
            .getWordDao()
            .getFavorite()
            .sortedByDescending { it.favorite.favoriteTime }
            .map { it.word }
            .toTypedArray()
}
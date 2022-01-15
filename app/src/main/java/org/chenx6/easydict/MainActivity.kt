package org.chenx6.easydict

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.chenx6.easydict.fragments.FavoriteWordFragment
import org.chenx6.easydict.fragments.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainFragment = MainFragment()
        val favoriteFragment = FavoriteWordFragment()
        setVisibleFragment(mainFragment)

        navigationView = findViewById(R.id.navigationView)
        navigationView.setOnItemSelectedListener { selector ->
            val replaceFragment = when (selector.itemId) {
                R.id.menu_home -> mainFragment
                R.id.menu_favorite -> favoriteFragment
                else -> mainFragment
            }
            setVisibleFragment(replaceFragment)
            true
        }
    }

    private fun setVisibleFragment(fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commitNow()
}
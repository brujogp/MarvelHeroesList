package com.test.marvel.iu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.test.marvel.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.binding!!.root)

        setNav()
    }

    private fun setNav() {
        this.binding!!.let {
            this@MainActivity.navHostFragment =
                supportFragmentManager.findFragmentById(it.fragmentContainer.id) as NavHostFragment
            this@MainActivity.navController = this@MainActivity.navHostFragment.navController
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.binding = null
    }
}
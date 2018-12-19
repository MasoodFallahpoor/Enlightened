package ir.fallahpoor.enlightened.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import ir.fallahpoor.enlightened.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar as Toolbar)

        setupActionBar()
        setupNavigationView()
        setupBottomNavigationView()

    }

    private fun setupActionBar() {
        NavigationUI.setupActionBarWithNavController(
            this,
            navHostFragment.findNavController(),
            drawerLayout
        )
    }

    private fun setupNavigationView() {
        NavigationUI.setupWithNavController(navigationView, navHostFragment.findNavController())
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView.setupWithNavController(navHostFragment.findNavController())
    }

    override fun onSupportNavigateUp(): Boolean =
        NavigationUI.navigateUp(navHostFragment.findNavController(), drawerLayout)

}

package com.example.legocatalog

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.legocatalog.databinding.ActivityMainBinding
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasAndroidInjector,
    View.OnGenericMotionListener {

    private var dX: Float = 0.0F
    private var dY: Float = 0.0F
    private var isFirstAcc: Boolean = true

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>
    lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        drawerLayout = binding.drawerLayout

        navController = findNavController(R.id.nav_fragment)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // Set up ActionBar
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)

//        binding.ivProfileIncTopBar.setOnTouchListener(this)
        binding.ivProfileIncTopBar.setOnTouchListener(object :
            OnSwipeTouchListener(this@MainActivity) {

            override fun onSwipeTop() {
                super.onSwipeTop()
                Toast.makeText(this@MainActivity, "onSwipeTop", Toast.LENGTH_SHORT).show();
//                slideDown( binding.ivProfileIncTopBar)
                val aniSlide: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
                binding.ivProfileIncTopBar.startAnimation(aniSlide)
                binding.ivProfileIncTopBar.setImageDrawable(getDrawable(R.drawable.ic_account_circle_black_24dp))

            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()

                Toast.makeText(this@MainActivity, "onSwipeBottom", Toast.LENGTH_SHORT).show();
                val aniSlide: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.slide_down)
                binding.ivProfileIncTopBar.startAnimation(aniSlide)
                binding.ivProfileIncTopBar.setImageDrawable(getDrawable(R.drawable.ic_account_box_black_24dp))

            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
                Toast.makeText(this@MainActivity, "onSwipeLeft", Toast.LENGTH_SHORT).show();

            }

            override fun onSwipeRight() {
                super.onSwipeRight()
                Toast.makeText(this@MainActivity, "onSwipeRight", Toast.LENGTH_SHORT).show();

            }
        })
        binding.ivProfileIncTopBar.setOnGenericMotionListener(this)
        // Set up navigation menu
        binding.navigationView.setupWithNavController(navController)
    }

    private fun updateProfile() {
        if (binding.ivProfileIncTopBar.drawable == getDrawable(R.drawable.ic_account_circle_black_24dp)) {
            binding.ivProfileIncTopBar.setImageDrawable(getDrawable(R.drawable.ic_account_box_black_24dp))
        } else {
            binding.ivProfileIncTopBar.setImageDrawable(getDrawable(R.drawable.ic_account_circle_black_24dp))

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun slideDown(view: View) {
        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            0F,  // fromYDelta
            view.height.toFloat()
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = false
        view.startAnimation(animate)
    }

    fun slideUp(view: View) {
        view.visibility = View.VISIBLE
//        view.setImageDrawable(getDrawable(R.drawable.ic_account_box_black_24dp))

        val animate = TranslateAnimation(
            0F,  // fromXDelta
            0F,  // toXDelta
            view.height.toFloat(),  // fromYDelta
            0F
        ) // toYDelta
        animate.duration = 500
        animate.fillAfter = true
        view.startAnimation(animate)
    }

/*    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                Toast.makeText(
                    this,
                    "ACTION_DOWN", Toast.LENGTH_SHORT
                )
                    .show();
            }
            MotionEvent.ACTION_UP -> {
                view.performClick();

                val aniSlide: Animation =
                    AnimationUtils.loadAnimation(applicationContext, R.anim.slide_up)
               view.startAnimation(aniSlide)
                ivProfileIncTopBar.setImageDrawable(getDrawable(R.drawable.ic_account_box_black_24dp))

                *//*ivProfileIncTopBar.animate()
                    .x(event.rawX + dX)
                    .y(event.rawY + dY)
                    .setDuration(200)
                    .start();*//*
//                slideUp(view)
                Toast.makeText(
                    this,
                    "ACTION_UP", Toast.LENGTH_SHORT
                )
                    .show();
            }
            *//* MotionEvent.ACTION_MOVE -> view.animate()
                 .x(event.rawX + dX - view.getWidth() / 2)
                 .y(event.rawY + dY - view.getHeight() / 2)
                 .setDuration(0)
                 .start()*//*
            else -> return false
        }
        return true


    }*/

    override fun onGenericMotion(p0: View?, p1: MotionEvent?): Boolean {
        Toast.makeText(
            this,
            p1!!.action.toString(), Toast.LENGTH_SHORT
        ).show()
        return true
    }
}

open class OnSwipeTouchListener(ctx: Context) : View.OnTouchListener {

    private val gestureDetector: GestureDetector

    companion object {

        private val SWIPE_THRESHOLD = 50
        private val SWIPE_VELOCITY_THRESHOLD = 50
    }

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {


        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }


    }

    open fun onSwipeRight() {}

    open fun onSwipeLeft() {}

    open fun onSwipeTop() {}

    open fun onSwipeBottom() {}
}
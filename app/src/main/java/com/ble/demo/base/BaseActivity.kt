package com.ble.demo.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.ble.demo.R
import com.ble.demo.utils.LoadingProgress
import com.ble.demo.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.toolbar_layout.*

abstract class BaseActivity : AppCompatActivity(),EditTextIntractor.IKeyBoardVisibleListener {
    lateinit var tv_title: TextView
    lateinit var iv_back: ImageView
    val editTextIntractor by lazy {
        EditTextIntractor().apply {
            addOnSoftKeyBoardVisibleListener(this@BaseActivity,this@BaseActivity)
        }
    }
     val loadingProgress  by lazy {
        LoadingProgress(this)
    }
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initParams()
        super.setContentView(R.layout.activity_base)
        setContentView(getLlayoutId())
        initToolbar()
        StatusBarUtil.setColor(this,ContextCompat.getColor(this,R.color.tool_bar))
        initView()
        initValue()

    }
    protected  fun setHeadColor(rescolor:Int){
        StatusBarUtil.setColor(this,ContextCompat.getColor(this,rescolor))
        toolbar.setBackgroundColor(ContextCompat.getColor(this,rescolor))

    }
    protected fun hideBack(){
        iv_back.visibility = View.GONE
    }

    protected fun setTitles(msg: CharSequence, color: Int = R.color.white) {

        tv_title.takeIf { tv_title.visibility == View.VISIBLE }?.apply {
            tv_title.text = msg
            tv_title.setTextColor(ContextCompat.getColor(this@BaseActivity,color))

        }


    }

    protected fun disToolbar() {
        toolbar?.visibility = View.GONE
    }

    private fun initToolbar() {
        toolbar?.let {
            setSupportActionBar(toolbar)
           // it.setBackgroundColor(ContextCompat.getColor(this,R.color.tool_bar))
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        tv_title = findViewById<TextView>(R.id.title)
        iv_back = findViewById(R.id.img_back)
        iv_back.setOnClickListener { finish() }
        tv_title.setOnClickListener { finish() }


    }

    /**
     * 点击外部软件盘消失
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (editTextIntractor.isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }
    override fun setContentView(layoutResID: Int) {
        val child = LayoutInflater.from(this).inflate(layoutResID,null)
        root_layout?.let {
            it.addView(child, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
        }
        super.setContentView(root_layout)
    }

    override fun onSoftKeyBoardVisible(visible: Boolean, windowBottom: Int) {
        val v = currentFocus
        if (v is EditText){
            v.isCursorVisible = visible
        }


    }
    abstract fun getLlayoutId(): Int
    abstract fun initParams()
    abstract fun initView()
    abstract fun initValue()


}



































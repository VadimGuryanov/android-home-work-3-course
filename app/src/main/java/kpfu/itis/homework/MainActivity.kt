package kpfu.itis.homework

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_operations_line_1.*
import kotlinx.android.synthetic.main.item_operations_line_2.*
import kotlinx.android.synthetic.main.item_operations_line_3.*
import kotlinx.android.synthetic.main.item_operations_line_4.*
import kotlinx.android.synthetic.main.item_operations_line_5.*

class MainActivity : AppCompatActivity() {

    private var exp: String = ""
    private var res: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        setSupportActionBar(toolbar)
        initOperations()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_theme, menu)
        initMenuItems(menu)
        return true
    }

    private fun initMenuItems(menu: Menu?) {
//        menu?.apply {
//            if (getDefaultNightMode() == MODE_NIGHT_YES) {
//                getItem(0).isVisible = false
//                getItem(1).isVisible = true
//            } else {
//                getItem(0).isVisible = true
//                getItem(1).isVisible = false
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.ic_luna -> setNightMode()
            R.id.ic_star -> setNightMode()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setNightMode() {
        if (getDefaultNightMode() == MODE_NIGHT_YES) {
            setDefaultNightMode(MODE_NIGHT_NO)
        } else {
            setDefaultNightMode(MODE_NIGHT_YES)
        }
    }

    private fun initOperations() {
        val listener = View.OnClickListener {
            val (expression, result) = when(it) {
                is TextView -> {
                    if (it.text.toString() == getString(R.string.value_equals)) {
                        tv_value.textSize += 8F
                        tv_expression.textSize -= + 8F
                    }
                    Calculator.applyCommand(it.text.toString(), this)
                }
                else -> {
                    Calculator.applyCommand(getString(R.string.value_clear_one), this)
                }
            }
            setTextViewSize(expression.length, tv_expression)
            setTextViewSize(result.toString().length, tv_value)
            tv_expression.text = expression
            tv_value.text = ("=$result")
            exp = expression
            res = result.toString()
        }
        tv_0.setOnClickListener(listener)
        tv_7.setOnClickListener(listener)
        tv_8.setOnClickListener(listener)
        tv_9.setOnClickListener(listener)
        tv_1.setOnClickListener(listener)
        tv_2.setOnClickListener(listener)
        tv_3.setOnClickListener(listener)
        tv_4.setOnClickListener(listener)
        tv_5.setOnClickListener(listener)
        tv_6.setOnClickListener(listener)
        tv_plus.setOnClickListener(listener)
        tv_minus.setOnClickListener(listener)
        tv_clear.setOnClickListener(listener)
        tv_delete.setOnClickListener(listener)
        tv_equals.setOnClickListener(listener)
    }

    private fun setTextViewSize(length: Int, tv: TextView) {
        when(length) {
            in 0..15 -> tv.textSize = 40f
            in 16..30 -> tv.textSize = 32f
            else -> tv.textSize = 24f
        }
    }
}
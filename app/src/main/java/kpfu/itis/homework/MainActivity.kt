package kpfu.itis.homework

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kpfu.itis.homework.store.CalculateAction
import kpfu.itis.homework.store.CalculateState
import kpfu.itis.homework.store.CalculateStore
import kpfu.itis.homework.store.side_effects.CalculateSideEffectsImpl

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG_PROGRAMMATICALLY = "programmatically"
        private const val TAG_EMPTY = ""
    }

    private lateinit var viewModel: MainViewModel
    private var TAG: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        et_number_1.doAfterTextChanged { text ->
            wrapperTextChanged(text) { viewModel.wroteOne(it) }
        }
        et_number_2.doAfterTextChanged { text ->
            wrapperTextChanged(text) { viewModel.wroteTwo(it) }
        }
        et_number_result.doAfterTextChanged { text ->
            wrapperTextChanged(text) {  viewModel.wroteResult(it) }
        }

        initObserves()
    }

    private fun wrapperTextChanged(text: Editable?, action: (Int) -> Unit) {
        if (TAG != TAG_PROGRAMMATICALLY) {
            text?.toString()?.let {
                if (it.isNotEmpty()) {
                    action(it.toInt())
                }
            }
        }
    }

    private fun initObserves() {
        with(viewModel) {
            liveDataOne.observe(this@MainActivity, Observer {
                wrapperObserver { et_number_1.setText(it.toString()) }
            })
            liveDataTwo.observe(this@MainActivity, Observer {
                wrapperObserver { et_number_2.setText(it.toString()) }
            })
            liveDataResult.observe(this@MainActivity, Observer {
                wrapperObserver { et_number_result.setText(it.toString()) }
            })
            liveDataLodding.observe(this@MainActivity, Observer {
                progressBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
            })
            liveDataError.observe(this@MainActivity, Observer {
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            })
        }
    }

    private fun wrapperObserver(function: () -> Unit) {
        TAG = TAG_PROGRAMMATICALLY
        function()
        TAG = TAG_EMPTY
    }
}
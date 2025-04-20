package otus.homework.flowcats

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val diContainer = DiContainer()
    private val catsViewModel by viewModels<CatsViewModel> {
        CatsViewModel.CatsViewModelFactory(
            diContainer.repository
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val view = layoutInflater.inflate(R.layout.activity_main, null) as CatsView
        setContentView(view)
        onFactStateSuccess(view)
        onFactStateError(view)
    }

    private fun onFactStateSuccess(view: CatsView) {
        lifecycleScope.launch {
            catsViewModel.factState.collect { result ->
                view.populate(result.fact)
            }
        }
    }

    private fun onFactStateError(view: CatsView) {
        lifecycleScope.launch {
            catsViewModel.factErrorState.collect { error ->
                view.showToast(error.message)
            }
        }
    }

    override fun onStop() {
        lifecycleScope.cancel()
        super.onStop()
    }
}
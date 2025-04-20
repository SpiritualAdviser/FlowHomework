package otus.homework.flowcats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsViewModel(
    private val catsRepository: CatsRepository
) : ViewModel() {

    val factErrorState = MutableStateFlow(Result.Error(""))
    val factState = MutableStateFlow(Result.Success(Fact("", 0)))

    init {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                getRandomFact()
            }
        }
    }

    private suspend fun getRandomFact() {
        catsRepository.listenForCatFacts()
            .collect {
                when (it) {
                    is Result.Success -> {
                        factState.value = it
                    }

                    is Result.Error -> {
                        factErrorState.value = it
                    }
                }
            }
    }

    class CatsViewModelFactory(private val catsRepository: CatsRepository) :
        ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            CatsViewModel(catsRepository) as T
    }
}

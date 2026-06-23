package mx.utng.ccdm.memorymatch.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material3.MaterialTheme
import mx.utng.ccdm.memorymatch.data.datasource.BestTimeDataSource
import mx.utng.ccdm.memorymatch.data.repository.BestTimeRepositoryImpl
import mx.utng.ccdm.memorymatch.domain.usecase.CheckMatchUseCase
import mx.utng.ccdm.memorymatch.domain.usecase.FlipCardUseCase
import mx.utng.ccdm.memorymatch.domain.usecase.GetBestTimeUseCase
import mx.utng.ccdm.memorymatch.domain.usecase.SaveBestTimeUseCase
import mx.utng.ccdm.memorymatch.domain.usecase.ShuffleBoardUseCase
import mx.utng.ccdm.memorymatch.presentation.board.BoardScreen
import mx.utng.ccdm.memorymatch.presentation.board.MemoryViewModel

class MemoryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dataSource  = BestTimeDataSource(context)
        val repository  = BestTimeRepositoryImpl(dataSource)
        return MemoryViewModel(
            shuffleBoard = ShuffleBoardUseCase(),
            flipCard = FlipCardUseCase(),
            checkMatch = CheckMatchUseCase(),
            saveBestTime = SaveBestTimeUseCase(repository),
            getBestTime = GetBestTimeUseCase(repository),
        ) as T
    }
}

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm: MemoryViewModel = viewModel(
                factory = MemoryViewModelFactory(applicationContext)
            )
            MaterialTheme { BoardScreen(viewModel = vm) }
        }
    }
}


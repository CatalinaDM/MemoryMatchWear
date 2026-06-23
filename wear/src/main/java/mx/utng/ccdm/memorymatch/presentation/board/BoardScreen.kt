package mx.utng.ccdm.memorymatch.presentation.board

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.curvedText
import androidx.wear.compose.material.scrollAway
import androidx.wear.compose.material3.TimeText
import androidx.wear.compose.material3.timeTextSeparator
import kotlinx.coroutines.delay
import mx.utng.ccdm.memorymatch.domain.model.GamePhase
import mx.utng.ccdm.memorymatch.domain.model.GameState
import androidx.compose.ui.Alignment
import androidx.wear.compose.material3.CircularProgressIndicator


@Composable
fun BoardScreen(viewModel: MemoryViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    val haptic = LocalHapticFeedback.current

    // Efectos de una sola vez (haptics)
    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                GameEffect.HapticMatch   -> haptic.performHapticFeedback(
                    HapticFeedbackType.LongPress)
                GameEffect.HapticMiss    -> haptic.performHapticFeedback(
                    HapticFeedbackType.LongPress)
                GameEffect.HapticVictory -> repeat(3) {  // triple vibración
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    delay(150L)
                }
            }
        }
    }

    if (state.phase == GamePhase.WON) {
        VictoryScreen(state = state, onRestart = viewModel::startNewGame)
        return
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF0A0A1E))) {

        // HUD superior: tiempo
        TimeText(modifier = Modifier.scrollAway(rememberScalingLazyListState())) {
            timeTextSeparator()
            curvedText("${state.elapsedSeconds}s · ${state.moves} mov · ${state.matchesFound}/ ${GameState.TOTAL_PAIRS}")
        }

        // Cuadrícula 3×4 centrada en la pantalla circular
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .padding(top = 32.dp, start = 4.dp, end = 4.dp, bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement   = Arrangement.spacedBy(6.dp),
        ) {
            itemsIndexed(state.board) { index, card ->
                CardItem(
                    card  = card,
                    onTap = { viewModel.onCardTapped(index) },
                )
            }
        }

        // Progreso
        CircularProgressIndicator(
            progress = {state.matchesFound.toFloat() / GameState.TOTAL_PAIRS},
            modifier = Modifier
                .align(Alignment.BottomCenter),
            strokeWidth = 3.dp
        )
    }
}
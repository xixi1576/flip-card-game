package cn.edu.sicnu.cs.zx.fourth_work.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import cn.edu.sicnu.cs.zx.fourth_work.R
import cn.edu.sicnu.cs.zx.fourth_work.model.Card
import cn.edu.sicnu.cs.zx.fourth_work.model.CardMatchingGame

private const val CARD_COUNT = 16
private const val COLS = 4
private const val ROWS = 4

/** 游戏牌在界面上的可观察状态快照（Controller 同步 Model → View 的载体）。 */
private data class CardUi(
    val text: String,
    val redSuit: Boolean,
    val faceUp: Boolean,
    val matched: Boolean
)

/**
 * 翻牌游戏 Activity 的 View：4x4 牌面 + 得分 + 重置/退出。
 * Controller 逻辑（点击处理、模型同步、重新开始）在本 Composable 内完成。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(onExit: (Int) -> Unit) {
    var game by remember { mutableStateOf(CardMatchingGame(CARD_COUNT)) }
    var cards by remember { mutableStateOf(snapshotOf(game)) }
    var score by remember { mutableStateOf(game.score) }
    var showGameOver by remember { mutableStateOf(false) }

    fun choose(index: Int) {
        if (game.cardAtIndex(index).isMatched) return
        game.chooseCardAtIndex(index)
        cards = snapshotOf(game)
        score = game.score
        if (cards.all { it.matched }) {
            showGameOver = true
        }
    }

    fun reset() {
        game = CardMatchingGame(CARD_COUNT)
        cards = snapshotOf(game)
        score = 0
        showGameOver = false
    }

    BackHandler { onExit(score) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.game_title)) })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.score_format, score),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            repeat(ROWS) { row ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    repeat(COLS) { col ->
                        val index = row * COLS + col
                        GameCard(
                            cardUi = cards[index],
                            index = index,
                            onClick = { choose(index) },
                            modifier = Modifier.weight(1f).padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { reset() }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.btn_reset))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Button(onClick = { onExit(score) }, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.btn_exit))
                }
            }
        }
    }

    if (showGameOver) {
        AlertDialog(
            onDismissRequest = { showGameOver = false },
            title = { Text(text = stringResource(R.string.game_over_title)) },
            text = { Text(text = stringResource(R.string.game_over_message, score)) },
            confirmButton = {
                TextButton(onClick = { showGameOver = false }) {
                    Text(text = stringResource(R.string.btn_confirm))
                }
            }
        )
    }
}

@Composable
private fun GameCard(
    cardUi: CardUi,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val redSuit = cardUi.redSuit
    val shape = RoundedCornerShape(8.dp)
    val cardDescription = stringResource(R.string.card_description, index + 1)
    val onPrimary = MaterialTheme.colorScheme.onPrimary

    Box(
        modifier = modifier
            .aspectRatio(0.75f)
            .clip(shape)
            .background(
                if (cardUi.faceUp) MaterialTheme.colorScheme.surfaceVariant
                else MaterialTheme.colorScheme.primary
            )
            .border(width = 1.dp, color = MaterialTheme.colorScheme.outline, shape = shape)
            .clickable(onClick = onClick)
            .semantics { contentDescription = cardDescription },
        contentAlignment = Alignment.Center
    ) {
        if (cardUi.faceUp) {
            Text(
                text = cardUi.text,
                color = if (redSuit) Color(0xFFCC0000) else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.alpha(if (cardUi.matched) 0.55f else 1f)
            )
        } else {
            // 牌背：与设计图一致的“叉”形图案
            Canvas(modifier = Modifier.fillMaxSize().padding(14.dp)) {
                val strokeWidth = 5.dp.toPx()
                drawLine(
                    color = onPrimary,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
                drawLine(
                    color = onPrimary,
                    start = Offset(size.width, 0f),
                    end = Offset(0f, size.height),
                    strokeWidth = strokeWidth
                )
            }
        }
    }
}

private fun snapshotOf(game: CardMatchingGame): List<CardUi> =
    (0 until CARD_COUNT).map { i ->
        val card = game.cardAtIndex(i)
        val text = card.toString()
        CardUi(
            text = text,
            redSuit = text.startsWith(Card.validSuits[0]) || text.startsWith(Card.validSuits[1]),
            faceUp = card.isChosen || card.isMatched,
            matched = card.isMatched
        )
    }

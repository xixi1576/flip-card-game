package cn.edu.sicnu.cs.zx.fourth_work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import cn.edu.sicnu.cs.zx.fourth_work.model.Player
import cn.edu.sicnu.cs.zx.fourth_work.ui.PersonalInfoScreen
import cn.edu.sicnu.cs.zx.fourth_work.ui.theme.Fourth_workTheme

/**
 * 主 Activity（Controller）：
 * 显示玩家基本信息；负责启动信息修改 Activity 与翻牌游戏 Activity，并接收返回结果。
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fourth_workTheme {
                MainScreen()
            }
        }
    }
}

@Composable
private fun MainScreen() {
    val initialPlayer = defaultPlayer()
    var player by remember { mutableStateOf(initialPlayer) }
    val context = LocalContext.current

    val editLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let { player = it.toPlayer() }
        }
    }

    val gameLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val score = result.data?.getIntExtra(EXTRA_SCORE, 0) ?: 0
            player = player.copy(highScore = maxOf(player.highScore, score))
        }
    }

    PersonalInfoScreen(
        player = player,
        onEditInfo = {
            editLauncher.launch(Intent(context, InfoEditActivity::class.java).putPlayer(player))
        },
        onPlayGame = {
            gameLauncher.launch(Intent(context, GameActivity::class.java))
        }
    )
}

/** 默认玩家信息（全部来自字符串资源，不在代码中写死字符串）。 */
@Composable
private fun defaultPlayer(): Player {
    return Player(
        name = stringResource(R.string.default_name),
        gender = stringResource(R.string.male),
        studentId = stringResource(R.string.default_student_id),
        phone = stringResource(R.string.default_phone),
        email = stringResource(R.string.default_email),
        hobbies = emptyList(),
        highScore = 0
    )
}

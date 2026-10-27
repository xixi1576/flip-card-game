package cn.edu.sicnu.cs.zx.fourth_work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cn.edu.sicnu.cs.zx.fourth_work.ui.GameScreen
import cn.edu.sicnu.cs.zx.fourth_work.ui.theme.Fourth_workTheme

/**
 * 翻牌游戏 Activity（Controller）：
 * 承载游戏 View，退出/返回时把本局得分通过结果 Intent 回传给主 Activity。
 */
class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Fourth_workTheme {
                GameScreen(
                    onExit = { score ->
                        setResult(Activity.RESULT_OK, Intent().putExtra(EXTRA_SCORE, score))
                        finish()
                    }
                )
            }
        }
    }
}

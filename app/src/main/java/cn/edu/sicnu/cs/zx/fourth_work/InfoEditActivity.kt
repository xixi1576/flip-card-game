package cn.edu.sicnu.cs.zx.fourth_work

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cn.edu.sicnu.cs.zx.fourth_work.model.Player
import cn.edu.sicnu.cs.zx.fourth_work.ui.InfoEditScreen
import cn.edu.sicnu.cs.zx.fourth_work.ui.theme.Fourth_workTheme

/**
 * 信息修改 Activity（Controller）：
 * 接收主 Activity 传入的玩家信息，编辑后通过结果 Intent 返回（无需保存到文件）。
 */
class InfoEditActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialPlayer = intent.toPlayer()
        setContent {
            Fourth_workTheme {
                InfoEditScreen(
                    initialPlayer = initialPlayer,
                    onSave = { player: Player ->
                        setResult(Activity.RESULT_OK, Intent().putPlayer(player))
                        finish()
                    },
                    onCancel = {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                )
            }
        }
    }
}

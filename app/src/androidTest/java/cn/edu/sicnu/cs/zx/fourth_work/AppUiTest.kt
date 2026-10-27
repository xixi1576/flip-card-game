package cn.edu.sicnu.cs.zx.fourth_work

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * 翻牌游戏应用的 UI 测试（Android 设备/模拟器上运行）。
 * 覆盖：主页面显示、信息修改往返、翻牌游戏交互与重置。
 */
@RunWith(AndroidJUnit4::class)
class AppUiTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val context: Context
        get() = InstrumentationRegistry.getInstrumentation().targetContext

    private fun waitForText(text: String) {
        composeRule.waitUntil(timeoutMillis = 5_000) {
            composeRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun mainScreen_showsAppTitlePlayerInfoAndButtons() {
        composeRule.onNodeWithText(context.getString(R.string.app_name)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.info_name, "周鑫")).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.info_student_id, "2024110550")).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.info_high_score, 0)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.btn_edit_info)).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.btn_game)).assertIsDisplayed()
    }

    @Test
    fun editAndSave_updatesMainScreenPlayerInfo() {
        // 进入信息修改页
        composeRule.onNodeWithText(context.getString(R.string.btn_edit_info)).performClick()
        waitForText(context.getString(R.string.edit_title))

        // 修改姓名与学号
        composeRule.onNodeWithTag("name_input").performTextReplacement("张三")
        composeRule.onNodeWithTag("student_id_input").performTextReplacement("2024999999")

        // 保存并返回主页
        composeRule.onNodeWithText(context.getString(R.string.btn_save)).performClick()
        waitForText(context.getString(R.string.info_name, "张三"))
        composeRule.onNodeWithText(context.getString(R.string.info_name, "张三")).assertIsDisplayed()
        composeRule.onNodeWithText(context.getString(R.string.info_student_id, "2024999999")).assertIsDisplayed()
    }

    @Test
    fun editCancel_keepsOriginalInfo() {
        composeRule.onNodeWithText(context.getString(R.string.btn_edit_info)).performClick()
        waitForText(context.getString(R.string.edit_title))

        composeRule.onNodeWithTag("name_input").performTextReplacement("李四")

        composeRule.onNodeWithText(context.getString(R.string.btn_cancel)).performClick()
        waitForText(context.getString(R.string.app_name))
        composeRule.onNodeWithText(context.getString(R.string.info_name, "周鑫")).assertIsDisplayed()
    }

    @Test
    fun game_flow_flipCardUpdatesScoreAndResetRestores() {
        // 进入翻牌游戏
        composeRule.onNodeWithText(context.getString(R.string.btn_game)).performClick()
        waitForText(context.getString(R.string.game_title))

        // 初始得分为 0
        composeRule.onNodeWithText(context.getString(R.string.score_format, 0)).assertIsDisplayed()

        // 翻开第一张牌：得分 -1
        composeRule.onNodeWithContentDescription(context.getString(R.string.card_description, 1)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.score_format, -1)).assertIsDisplayed()

        // 重置游戏：得分恢复为 0
        composeRule.onNodeWithText(context.getString(R.string.btn_reset)).performClick()
        composeRule.onNodeWithText(context.getString(R.string.score_format, 0)).assertIsDisplayed()

        // 退出游戏：返回主页面
        composeRule.onNodeWithText(context.getString(R.string.btn_exit)).performClick()
        waitForText(context.getString(R.string.app_name))
        composeRule.onNodeWithText(context.getString(R.string.btn_game)).assertIsDisplayed()
    }
}

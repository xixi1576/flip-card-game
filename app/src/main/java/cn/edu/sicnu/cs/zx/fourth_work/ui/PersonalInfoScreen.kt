package cn.edu.sicnu.cs.zx.fourth_work.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cn.edu.sicnu.cs.zx.fourth_work.R
import cn.edu.sicnu.cs.zx.fourth_work.model.Player

/**
 * 主 Activity 的 View：显示玩家基本信息（姓名、性别、学号、电话、邮件地址、爱好、游戏最高分）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoScreen(
    player: Player,
    onEditInfo: () -> Unit,
    onPlayGame: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.app_name)) })
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
            // 头像占位框
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.avatar),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(20.dp))

            InfoRow(text = stringResource(R.string.info_name, displayText(player.name)))
            InfoRow(text = stringResource(R.string.info_gender, displayText(player.gender)))
            InfoRow(text = stringResource(R.string.info_student_id, displayText(player.studentId)))
            InfoRow(text = stringResource(R.string.info_phone, displayText(player.phone)))
            InfoRow(text = stringResource(R.string.info_email, displayText(player.email)))
            InfoRow(text = stringResource(R.string.info_hobby, displayHobbies(player.hobbies)))
            InfoRow(text = stringResource(R.string.info_high_score, player.highScore))

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = onEditInfo, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.btn_edit_info))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onPlayGame, modifier = Modifier.fillMaxWidth()) {
                Text(text = stringResource(R.string.btn_game))
            }
        }
    }
}

@Composable
private fun InfoRow(text: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        )
    }
}

@Composable
private fun displayText(text: String): String =
    text.ifBlank { stringResource(R.string.not_filled) }

@Composable
private fun displayHobbies(hobbies: List<String>): String =
    if (hobbies.isEmpty()) {
        stringResource(R.string.not_filled)
    } else {
        hobbies.joinToString(separator = stringResource(R.string.hobby_separator))
    }

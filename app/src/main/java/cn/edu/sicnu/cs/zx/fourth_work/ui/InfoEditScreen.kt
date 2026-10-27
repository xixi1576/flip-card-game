package cn.edu.sicnu.cs.zx.fourth_work.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cn.edu.sicnu.cs.zx.fourth_work.R
import cn.edu.sicnu.cs.zx.fourth_work.model.Player

/**
 * 信息修改 Activity 的 View：修改姓名、学号、电话、邮件、性别、爱好，
 * 修改结果通过 [onSave] 回调返回（无需保存到文件）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoEditScreen(
    initialPlayer: Player,
    onSave: (Player) -> Unit,
    onCancel: () -> Unit
) {
    val defaultGender = stringResource(R.string.male)
    var name by remember { mutableStateOf(initialPlayer.name) }
    var studentId by remember { mutableStateOf(initialPlayer.studentId) }
    var phone by remember { mutableStateOf(initialPlayer.phone) }
    var email by remember { mutableStateOf(initialPlayer.email) }
    var gender by remember { mutableStateOf(initialPlayer.gender.ifBlank { defaultGender }) }
    var hobbies by remember { mutableStateOf(initialPlayer.hobbies.toSet()) }

    val hobbyOptions = listOf(
        stringResource(R.string.hobby_travel),
        stringResource(R.string.hobby_reading),
        stringResource(R.string.hobby_game),
        stringResource(R.string.hobby_shopping)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = stringResource(R.string.edit_title)) })
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
                    .size(80.dp)
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

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(text = stringResource(R.string.label_name)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("name_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = { Text(text = stringResource(R.string.label_student_id)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().testTag("student_id_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text(text = stringResource(R.string.label_phone)) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier.fillMaxWidth().testTag("phone_input")
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(text = stringResource(R.string.label_email)) },
                singleLine = true,
                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth().testTag("email_input")
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.label_gender),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf(stringResource(R.string.male), stringResource(R.string.female)).forEach { option ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = gender == option,
                            onClick = { gender = option }
                        )
                        Text(text = option)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.label_hobby),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                hobbyOptions.forEach { option ->
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = option in hobbies,
                            onCheckedChange = { checked ->
                                hobbies = if (checked) hobbies + option else hobbies - option
                            }
                        )
                        Text(text = option, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        onSave(
                            Player(
                                name = name.trim(),
                                gender = gender,
                                studentId = studentId.trim(),
                                phone = phone.trim(),
                                email = email.trim(),
                                hobbies = hobbyOptions.filter { it in hobbies },
                                highScore = initialPlayer.highScore
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = stringResource(R.string.btn_save))
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedButton(onClick = onCancel, modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(R.string.btn_cancel))
                }
            }
        }
    }
}

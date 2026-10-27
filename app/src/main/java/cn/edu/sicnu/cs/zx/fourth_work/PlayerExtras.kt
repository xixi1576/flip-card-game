package cn.edu.sicnu.cs.zx.fourth_work

import android.content.Intent
import cn.edu.sicnu.cs.zx.fourth_work.model.Player

/** Activity 间传递玩家信息的 Intent extra 键（信息无需持久化到文件）。 */
const val EXTRA_NAME = "extra_name"
const val EXTRA_GENDER = "extra_gender"
const val EXTRA_STUDENT_ID = "extra_student_id"
const val EXTRA_PHONE = "extra_phone"
const val EXTRA_EMAIL = "extra_email"
const val EXTRA_HOBBIES = "extra_hobbies"
const val EXTRA_HIGH_SCORE = "extra_high_score"

/** 翻牌游戏结束时回传的分数键。 */
const val EXTRA_SCORE = "extra_score"

fun Intent.putPlayer(player: Player): Intent {
    return putExtra(EXTRA_NAME, player.name)
        .putExtra(EXTRA_GENDER, player.gender)
        .putExtra(EXTRA_STUDENT_ID, player.studentId)
        .putExtra(EXTRA_PHONE, player.phone)
        .putExtra(EXTRA_EMAIL, player.email)
        .putExtra(EXTRA_HOBBIES, player.hobbies.toTypedArray())
        .putExtra(EXTRA_HIGH_SCORE, player.highScore)
}

fun Intent.toPlayer(): Player {
    return Player(
        name = getStringExtra(EXTRA_NAME) ?: "",
        gender = getStringExtra(EXTRA_GENDER) ?: "",
        studentId = getStringExtra(EXTRA_STUDENT_ID) ?: "",
        phone = getStringExtra(EXTRA_PHONE) ?: "",
        email = getStringExtra(EXTRA_EMAIL) ?: "",
        hobbies = (getStringArrayExtra(EXTRA_HOBBIES) ?: emptyArray()).toList(),
        highScore = getIntExtra(EXTRA_HIGH_SCORE, 0)
    )
}

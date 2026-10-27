package cn.edu.sicnu.cs.zx.fourth_work.model

/**
 * 玩家信息模型（MVC 中的 Model）。
 * 实验要求：修改后的信息无需保存到文件，仅保存在内存中即可。
 */
data class Player(
    val name: String = "",
    val gender: String = "",
    val studentId: String = "",
    val phone: String = "",
    val email: String = "",
    val hobbies: List<String> = emptyList(),
    val highScore: Int = 0
)

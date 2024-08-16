package org.kgusarov.krono.locales.ja

object Ja {
    @JvmStatic
    val configuration = JaDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration()

    @JvmStatic
    val strict = configuration.createConfiguration()
}

internal fun toHankaku(text: String): String {
    return text
        .replace("\u2019", "\u0027")
        .replace("\u201D", "\u0022")
        .replace("\u3000", "\u0020")
        .replace("\uFFE5", "\u00A5")
        .replace(
            Regex("[\uFF01\uFF03-\uFF06\uFF08\uFF09\uFF0C-\uFF19\uFF1C-\uFF1F\uFF21-\uFF3B\uFF3D\uFF3F\uFF41-\uFF5B\uFF5D\uFF5E]"),
        ) {
            alphaNum(it.value)
        }
}

internal fun alphaNum(token: String): String {
    return String(charArrayOf((token[0].code - 65248).toChar()))
}

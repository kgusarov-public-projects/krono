package org.kgusarov.krono.locales.en

object En {
    @JvmStatic
    val configuration = ENDefaultConfiguration()

    @JvmStatic
    val casual = configuration.createCasualConfiguration(false)

    @JvmStatic
    val strict = configuration.createConfiguration(strictMode = true, littleEndian = false)
}

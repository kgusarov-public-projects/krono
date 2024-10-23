package org.kgusarov.krono.extensions

operator fun MatchResult.get(group: Int): String = groupValues[group]

package nicestring

fun String.isNice(): Boolean {
    val containSubstring = !contains("b[uae]".toRegex())
    val containVowels = count { it in "aeiou" } >= 3
    val doubleLetter = zipWithNext().any { (a, b) -> a == b }

    return listOf(containSubstring, containVowels, doubleLetter)
            .count { it } >= 2
}

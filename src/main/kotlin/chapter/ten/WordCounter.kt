package chapter.ten

import chapter.three.List.Companion
import chapter.three.List.Companion.foldMap
import kotlin.jvm.Throws
import kotlin.math.min

sealed class WC

/**
 * A Stub is an accumulation of characters that form a partial word.
 */
data class Stub(val chars: String) : WC()

/**
 * A Part contains a left stub, a word count, and a right stub.
 */
data class Part(val ls: String, val words: Int, val rs: String) : WC()

@Throws
fun wordCount(s: String): Int {

    fun wc(c: Char): WC =
        if (c.isWhitespace()) Part("", 0, "")
        else Stub("$c")

    fun unstub(s: String): Int = min(s.length, 1)

    val WCM = wcMonoid()

    val list = chapter.three.List.of(*s.toCharArray().toTypedArray())
    return when (val wc = foldMap(list, WCM) { wc(it) }) {
        is Stub -> unstub(wc.chars)
        is Part -> unstub(wc.rs) + wc.words + unstub(wc.rs)
    }
}


fun wcMonoid(): Monoid<WC> = object : Monoid<WC> {
    override fun combine(a1: WC, a2: WC): WC =
        when (a1) {
            is Stub -> when (a2) {
                is Stub -> Stub(a1.chars + a2.chars)
                is Part -> Part(a1.chars + a2.ls, a2.words, a2.rs)
            }
            is Part -> when (a2) {
                is Stub -> Part(a1.ls, a1.words, a1.rs + a2.chars)
                is Part ->
                    Part(
                        ls = a1.ls,
                        words = a1.words + a2.words + (if ((a1.rs + a2.ls).isEmpty()) 0 else 1),
                        rs = a2.rs
                    )
            }
        }

    override val nil: WC
        get() = Stub("")
}


package chapter.seven

import chapter.four.getOrElse
import chapter.seven.OldPar.Companion.fork
import chapter.seven.OldPar.Companion.lazyUnit
import chapter.seven.OldPar.Companion.map2


fun sum(ints: List<Int>): OldPar<Int> =
    if (ints.size <= 1)
        lazyUnit { ints.firstOption().getOrElse { 0 } }
    else {
        val (l, r) = ints.splitAt(ints.size / 2)
        map2(
            fork { sum(l) },
            fork { sum(r) }
        )
        { lx: Int, rx: Int ->
            println("Evaluating $lx and $rx")
            lx + rx
        }
    }

package chapter.nine

import chapter.four.Either
import chapter.four.Left
import chapter.four.Right

interface Parser<A> {
    val content: A
}
class ParserImpl<A>(private val a: A) : Parser<A> {
    override val content: A
        get() = a

}

data class ParseError(val msg: String)

interface Parsers<PE> {
    companion object {
        val Error = ParseError("Generic Error")
    }

    fun char(c: Char): Parser<Char>

    fun string(s: String): Parser<String>

    fun <A> run(p: Parser<A>, input: String): Either<PE, A>

    fun <A> or(pa: Parser<A>, pb: Parser<A>): Parser<A>

    infix fun String.or(other: String)= or(string(this), string(other))

    fun <A> listOfN(n: Int, p: Parser<A>): Parser<List<A>>
}
class ParsersImpl: Parsers<ParseError> {
    override fun char(c: Char): Parser<Char> =
        ParserImpl(c)

    override fun string(s: String): Parser<String> =
        ParserImpl(s)

    override fun <A> run(p: Parser<A>, input: String) =
        if( p.content.toString() == input) Right(p.content) else Left(Parsers.Error)

    override fun <A> or(pa: Parser<A>, pb: Parser<A>): Parser<A> {
        TODO()
    }

    override fun <A> listOfN(n: Int, p: Parser<A>): Parser<List<A>> {
        TODO("Not yet implemented")
    }

}

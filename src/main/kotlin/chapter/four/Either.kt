package chapter.four

sealed class Either<out E, out A>

data class Left<out E>(val value: E) : Either<E, Nothing>()

data class Right<out A>(val value: A) : Either<Nothing, A>()


fun <E, A, B> Either<E, A>.map(f: (A) -> B): Either<E, B> =
    when(this) {
        is Left -> this
        is Right -> Right(f(this.value))
    }

fun <E, A, B> Either<E, A>.flatMap(f: (A) -> Either<E, B>): Either<E, B> =
    when(this) {
        is Left -> this
        is Right -> when(val either = f(this.value)) {
            is Left -> either
            is Right -> Right(either.value)
        }
    }

fun <E, A> Either<E, A>.orElse(f: () -> Either<E, A>): Either<E, A> =
    when(this) {
        is Left -> f()
        is Right -> this
    }

fun <E, A, B, C> map2(ae: Either<E, A>, be: Either<E, B>, f: (A, B) -> C): Either<E, C> =
    ae.flatMap { a -> be.map { b -> f(a,b) } }
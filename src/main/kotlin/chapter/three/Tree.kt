package chapter.three

/**
 * This is a binary Tree structure
 * A binary Tree is an Algebraic Data Structure
 */
sealed class Tree<out A>

data class Leaf<A>(val value: A) : Tree<A>()

data class Branch<A>(val left: Tree<A>, val right: Tree<A>) : Tree<A>()

// Extension functions
fun <A> Tree<A>.size(): Int {
    return when (val tree = this) {
        is Leaf -> 1
        is Branch -> 1 + tree.left.size() + tree.right.size()
    }
}

fun <A> Tree<A>.sizeF(): Int = this.fold({ 1 }, { bl, br -> 1 + bl + br })

fun <A, B> Tree<A>.map(f: (A) -> B) : Tree<B> =
    this.fold({ a: A -> Leaf(f(a)) }, { bl: Tree<B>, br: Tree<B> -> Branch(bl, br) })

fun <A, B> Tree<A>.fold(l: (A) -> B, b: (B, B) -> B): B {
    return when(val tree = this) {
        is Leaf -> l(tree.value)
        is Branch -> b(tree.left.fold(l, b), tree.right.fold(l,b))
    }
}

// Random functions
fun max(tree: Tree<Int>) : Int {
    return when(tree) {
        is Leaf -> tree.value
        is Branch -> maxOf(max(tree.left), max(tree.right))
    }
}



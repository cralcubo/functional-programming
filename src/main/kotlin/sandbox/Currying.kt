package sandbox

data class Coffee(val coffeeType: String, val milkType: String, val sugarType: String)

fun orderCoffee(coffee: String): (String) -> (String) -> Coffee = { milk -> { sugar -> Coffee(coffee, milk, sugar) } }

fun main() {
    val arabica = orderCoffee("Arabica")
    val coffee = arabica("creamy milk")("bolivian sugar")
    println("Ordered coffee: $coffee")
}
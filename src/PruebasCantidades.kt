import model.ItemPedido
import model.Pedido
import service.DatosPruebaFactory
import service.IngredientesPedidoService
import service.ReporteService

fun main() {

    println("=== PRUEBAS DE CANTIDADES EXPLÍCITAS S3 ===")

    val datos = DatosPruebaFactory.crearDatos()

    val pedido1 = datos.pedidosDelDia[0]
    val pedido2 = datos.pedidosDelDia[1]

    println()
    println("=== PRUEBA 1: COMPATIBILIDAD S2 ===")

    check(pedido1.items.size == 1)
    check(pedido1.calcularCantidadPizzas() == 3)
    check(pedido1.calcularSubtotal() == 26970.0)
    check(pedido1.calcularDescuento() == 0.0)
    check(kotlin.math.abs(pedido1.calcularTotal() - 32094.3) < 0.001)

    println("PASS: Tres Pepperoni conservan los totales de S2.")

    check(pedido2.items.size == 2)
    check(pedido2.calcularCantidadPizzas() == 2)
    check(pedido2.calcularSubtotal() == 18480.0)
    check(kotlin.math.abs(pedido2.calcularTotal() - 21991.2) < 0.001)

    println("PASS: Pedido mixto conserva sus importes.")

    println()
    println("=== PRUEBA 2: DESCUENTO POR CANTIDAD ===")

    val pedidoGrande = Pedido(
        cliente = datos.clientePrincipal,
        items = listOf(
            ItemPedido(datos.pizzaPepperoni, 3),
            ItemPedido(datos.pizzaQueso, 2)
        ),
        estado = "En preparación"
    )

    check(pedidoGrande.calcularCantidadPizzas() == 5)
    check(pedidoGrande.calcularSubtotal() == 45950.0)
    check(pedidoGrande.calcularDescuento() == 4595.0)
    check(kotlin.math.abs(pedidoGrande.calcularTotal() - 50085.5) < 0.001)

    println("PASS: Cinco pizzas aplican descuento del 10%.")
    println("Subtotal: ${pedidoGrande.calcularSubtotal()}")
    println("Descuento: ${pedidoGrande.calcularDescuento()}")
    println("Total: ${pedidoGrande.calcularTotal()}")

    println()
    println("=== PRUEBA 3: INGREDIENTES AUTOMÁTICOS ===")

    val ingredientes =
        IngredientesPedidoService.calcularIngredientesNecesarios(
            pedidoGrande.items
        )

    check(ingredientes["Masa"] == 5)
    check(ingredientes["Salsa de tomate"] == 5)
    check(ingredientes["Queso"] == 7)
    check(ingredientes["Pepperoni"] == 3)

    for ((ingrediente, cantidad) in ingredientes) {
        println("$ingrediente: $cantidad")
    }

    println("PASS: Ingredientes calculados desde cantidades explícitas.")

    println()
    println("=== PRUEBA 4: CANTIDADES INVÁLIDAS ===")

    var rechazadas = 0

    for (cantidad in listOf(0, -1, 101)) {

        try {
            ItemPedido(datos.pizzaPepperoni, cantidad)
        } catch (e: IllegalArgumentException) {
            rechazadas++
            println("PASS: Cantidad $cantidad rechazada.")
        }
    }

    check(rechazadas == 3)

    println()
    println("=== PRUEBA 5: REPORTE ===")
    println(ReporteService.generarResumenPedido(pedidoGrande, 4))

    println()
    println("=== RESULTADO: TODAS LAS PRUEBAS APROBADAS ===")
}

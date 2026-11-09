import model.Repartidor
import service.InventarioService
import service.RepartidorService

fun main() {

    println("=== PRUEBAS DE INTEGRIDAD S3 ===")

    println()
    println("=== PRUEBA 1: REPARTIDORES ===")

    val repartidores = listOf(
        Repartidor("Carlos", "912345678", 0.4, disponible = false),
        Repartidor("Andrea", "923456789", 1.2),
        Repartidor("Felipe", "934567890", 2.5)
    )

    val seleccionado =
        RepartidorService.asignarRepartidorMasCercano(repartidores)

    check(seleccionado?.nombre == "Andrea") {
        "ERROR: Se asignó un repartidor incorrecto."
    }

    println("Repartidor seleccionado: ${seleccionado.nombre}")
    println("PASS: Se excluyó a Carlos porque estaba ocupado.")

    seleccionado.entregarPedido()

    check(!seleccionado.disponible)
    println("PASS: Andrea quedó ocupada al iniciar la entrega.")

    seleccionado.finalizarEntrega()

    check(seleccionado.disponible)
    println("PASS: Andrea volvió a estar disponible.")

    val sinDisponibles = RepartidorService.asignarRepartidorMasCercano(
        listOf(
            Repartidor("Carlos", "912345678", 0.4, disponible = false)
        )
    )

    check(sinDisponibles == null)
    println("PASS: Sin repartidores disponibles devuelve null.")

    println()
    println("=== PRUEBA 2: INVENTARIO ===")

    val inventario = mutableMapOf(
        "Masa" to 3,
        "Queso" to 2
    )

    val servicio = InventarioService(inventario)

    val stockOriginal = inventario.toMap()

    val pedidoInvalido = mapOf(
        "Masa" to 2,
        "Queso" to 3
    )

    val rechazado = servicio.descontarIngredientes(pedidoInvalido)

    check(!rechazado)
    check(inventario == stockOriginal)

    println("PASS: Pedido insuficiente rechazado sin descontar nada.")

    val ingredienteAusente = mapOf(
        "Masa" to 1,
        "Pepperoni" to 1
    )

    check(!servicio.descontarIngredientes(ingredienteAusente))
    check(inventario == stockOriginal)

    println("PASS: Ingrediente ausente no genera stock negativo.")

    val pedidoValido = mapOf(
        "Masa" to 2,
        "Queso" to 1
    )

    check(servicio.descontarIngredientes(pedidoValido))

    check(inventario["Masa"] == 1)
    check(inventario["Queso"] == 1)

    println("PASS: Pedido válido descontado correctamente.")

    println()
    servicio.mostrarInventario()

    println()
    println("=== RESULTADO: TODAS LAS PRUEBAS APROBADAS ===")
}

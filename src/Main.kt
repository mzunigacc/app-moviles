import model.Cliente
import model.Pedido
import model.Producto
import model.Repartidor
import model.Usuario

fun main() {

    println("=== SISTEMA PIZZERÍA ===")
    println()

    val cliente = Cliente(
        nombre = "Matías",
        contacto = "987654321",
        correo = "matias@email.com"
    )

    val repartidor = Repartidor(
        nombre = "Carlos",
        contacto = "912345678",
        distanciaKm = 2.5
    )

    val pizzaPepperoni = Producto(
        nombre = "Pizza Pepperoni",
        precio = 8990.0,
        ingredientes = listOf(
            "Masa",
            "Salsa de tomate",
            "Queso",
            "Pepperoni"
        )
    )

    val pedido = Pedido(
        cliente = cliente,
        productos = listOf(pizzaPepperoni),
        estado = "En preparación"
    )

    println(cliente.obtenerDescripcion())
    println(repartidor.obtenerDescripcion())
    println()
    println(pizzaPepperoni.detallarProducto())
    println()
    println("Subtotal del pedido: $${pedido.calcularSubtotal()}")
    println("Estado: ${pedido.estado}")

    pedido.cambiarEstado("Listo")

    println("Nuevo estado: ${pedido.estado}")
    println()
    println(repartidor.entregarPedido())

    println()
    println("=== POLIMORFISMO ===")

    val usuarios: List<Usuario> = listOf(cliente, repartidor)

    for (usuario in usuarios) {
        println(usuario.obtenerDescripcion())
    }
}

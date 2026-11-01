import model.Cliente
import model.Pedido
import model.Producto
import model.Repartidor
import model.Usuario
import service.InventarioService
import service.ValidacionService

fun main() {

    println("=== SISTEMA PIZZERÍA ===")
    println()

    val telefonoValidado =
        ValidacionService.validarTelefono("987654321")

    val correoValidado =
        ValidacionService.validarCorreo("matias@email.com")

    val cliente = Cliente(
        nombre = "Matías",
        contacto = telefonoValidado,
        correo = correoValidado
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
        productos = listOf(
            pizzaPepperoni,
            pizzaPepperoni,
            pizzaPepperoni
        ),
        estado = "En preparación"
    )

    println(cliente.obtenerDescripcion())
    println(repartidor.obtenerDescripcion())
    println()

    println(pizzaPepperoni.detallarProducto())
    println()

    println("Subtotal: $${pedido.calcularSubtotal()}")
    println("Impuesto: $${pedido.calcularImpuesto()}")
    println("Descuento: $${pedido.calcularDescuento()}")
    println("Total final: $${pedido.calcularTotal()}")
    println("Estado: ${pedido.estado}")

    pedido.cambiarEstado("Listo")

    println("Nuevo estado: ${pedido.estado}")
    println()

    println(repartidor.entregarPedido())

    println()
    println("=== POLIMORFISMO ===")

    val usuarios: List<Usuario> = listOf(
        cliente,
        repartidor
    )

    for (usuario in usuarios) {
        println(usuario.obtenerDescripcion())
    }

    println()
    println("=== PRUEBA DE INVENTARIO ===")

    val inventario = mutableMapOf(
        "Masa" to 10,
        "Salsa de tomate" to 8,
        "Queso" to 6,
        "Pepperoni" to 4
    )

    val inventarioService =
        InventarioService(inventario)

    inventarioService.mostrarInventario()

    println()
    println("Verificando ingredientes para 2 pizzas...")

    val ingredientesNecesarios = mapOf(
        "Masa" to 2,
        "Salsa de tomate" to 2,
        "Queso" to 2,
        "Pepperoni" to 2
    )

    val pedidoDisponible =
        inventarioService.verificarDisponibilidad(
            ingredientesNecesarios
        )

    if (pedidoDisponible) {

        inventarioService.descontarIngredientes(
            ingredientesNecesarios
        )

        println()
        println("Pedido aceptado. Inventario actualizado:")

        inventarioService.mostrarInventario()

    } else {

        println(
            "Pedido rechazado por falta de stock."
        )
    }

    println()
    println("=== PRUEBA DE VALIDACIONES ===")

    val telefonoIncorrecto =
        ValidacionService.validarTelefono(
            "ABC123"
        )

    val correoIncorrecto =
        ValidacionService.validarCorreo(
            "correo-mal-formado"
        )

    val telefonoNulo =
        ValidacionService.validarTelefono(
            null
        )

    val correoNulo =
        ValidacionService.validarCorreo(
            null
        )

    println(
        "Teléfono válido: $telefonoValidado"
    )

    println(
        "Correo válido: $correoValidado"
    )

    println(
        "Teléfono incorrecto: $telefonoIncorrecto"
    )

    println(
        "Correo incorrecto: $correoIncorrecto"
    )

    println(
        "Teléfono nulo: $telefonoNulo"
    )

    println(
        "Correo nulo: $correoNulo"
    )
}

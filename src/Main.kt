import model.Cliente
import model.Pedido
import model.Producto
import model.Repartidor
import model.Usuario
import service.InventarioService
import service.RepartidorService
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

    val cliente2 = Cliente(
        nombre = "Camila",
        contacto = ValidacionService.validarTelefono("976543210"),
        correo = ValidacionService.validarCorreo("camila@email.com")
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

    val pizzaQueso = Producto(
        nombre = "Pizza Cuatro Quesos",
        precio = 9490.0,
        ingredientes = listOf(
            "Masa",
            "Salsa de tomate",
            "Queso mozzarella",
            "Queso azul",
            "Queso parmesano",
            "Queso gouda"
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

    val pedido2 = Pedido(
        cliente = cliente2,
        productos = listOf(
            pizzaQueso,
            pizzaPepperoni
        ),
        estado = "En camino"
    )

    val pedido3 = Pedido(
        cliente = cliente,
        productos = listOf(
            pizzaQueso
        ),
        estado = "Entregado"
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
    println("=== ASIGNACIÓN DE REPARTIDOR ===")

    val repartidoresDisponibles = listOf(
        Repartidor(
            nombre = "Carlos",
            contacto = "912345678",
            distanciaKm = 2.5
        ),
        Repartidor(
            nombre = "Andrea",
            contacto = "923456789",
            distanciaKm = 1.2
        ),
        Repartidor(
            nombre = "Felipe",
            contacto = "934567890",
            distanciaKm = 4.8
        )
    )

    val repartidorAsignado =
        RepartidorService.asignarRepartidorMasCercano(
            repartidoresDisponibles
        )

    if (repartidorAsignado != null) {

        println(
            "Repartidor asignado: ${repartidorAsignado.nombre}"
        )

        println(
            "Distancia: ${repartidorAsignado.distanciaKm} km"
        )

    } else {

        println("No hay repartidores disponibles.")
    }

    println()
    println("=== REPORTE DE PEDIDOS DEL DÍA ===")

    val pedidosDelDia = arrayOf(
        pedido,
        pedido2,
        pedido3
    )

    for ((indice, pedidoActual) in pedidosDelDia.withIndex()) {

        val nombresProductos =
            pedidoActual.productos.joinToString { producto ->
                producto.nombre
            }

        println(
            """
            Pedido ${indice + 1}
            Cliente: ${pedidoActual.cliente.nombre}
            Pizzas: $nombresProductos
            Total: $${pedidoActual.calcularTotal()}
            Estado: ${pedidoActual.estado}
            """.trimIndent()
        )

        println()
    }

    val pedidosCompletados =
        pedidosDelDia.filter { pedidoActual ->
            pedidoActual.estado == "Entregado"
        }

    val pedidosPendientes =
        pedidosDelDia.filter { pedidoActual ->
            pedidoActual.estado != "Entregado"
        }

    println(
        "Pedidos completados: ${pedidosCompletados.size}"
    )

    println(
        "Pedidos pendientes: ${pedidosPendientes.size}"
    )

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

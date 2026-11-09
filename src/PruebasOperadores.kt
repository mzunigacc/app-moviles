import model.Cliente
import model.ItemPedido
import model.Pedido
import model.Producto
import service.DatosPruebaFactory
import kotlin.math.abs

fun main() {

    println("=== PRUEBAS DE OPERADORES E IGUALDAD S3 ===")

    val datos = DatosPruebaFactory.crearDatos()

    println()
    println("=== PRUEBA 1: IGUALDAD DE CLIENTES ===")

    val clienteDuplicado = Cliente(
        "matías",
        "900000000",
        "MATIAS@email.com"
    )

    check(datos.clientePrincipal == clienteDuplicado)

    val clientes = listOf(
        datos.clientePrincipal,
        clienteDuplicado,
        datos.clienteSecundario
    )

    val clientesUnicos = clientes.toSet()

    check(clientes.size == 3)
    check(clientesUnicos.size == 2)

    println("PASS: Clientes duplicados identificados.")
    println("Clientes registrados: ${clientes.size}")
    println("Clientes únicos: ${clientesUnicos.size}")

    println()
    println("=== PRUEBA 2: IGUALDAD DE PRODUCTOS ===")

    val pizzaDuplicada = Producto(
        nombre = "pizza pepperoni",
        precio = 8990.0,
        ingredientes = datos.pizzaPepperoni.ingredientes,
        categoria = "PIZZA"
    )

    check(datos.pizzaPepperoni == pizzaDuplicada)

    val productos = listOf(
        datos.pizzaPepperoni,
        pizzaDuplicada,
        datos.pizzaQueso
    )

    check(productos.toSet().size == 2)

    println("PASS: Productos duplicados identificados.")
    println("Productos registrados: ${productos.size}")
    println("Productos únicos: ${productos.toSet().size}")

    println()
    println("=== PRUEBA 3: COMBINAR PEDIDOS ===")

    val pedidoA = Pedido(
        cliente = datos.clientePrincipal,
        items = listOf(
            ItemPedido(datos.pizzaPepperoni, 2),
            ItemPedido(datos.pizzaQueso, 1)
        ),
        estado = "Listo",
        notas = "Sin cebolla"
    )

    val pedidoB = Pedido(
        cliente = clienteDuplicado,
        items = listOf(
            ItemPedido(pizzaDuplicada, 1),
            ItemPedido(datos.pizzaQueso, 1)
        ),
        estado = "Listo",
        notas = "Entregar en recepción"
    )

    val combinado = pedidoA + pedidoB

    check(combinado.items.size == 2)
    check(combinado.calcularCantidadPizzas() == 5)
    check(combinado.calcularSubtotal() == 45950.0)
    check(combinado.calcularDescuento() == 4595.0)
    check(abs(combinado.calcularTotal() - 50085.5) < 0.001)

    check(
        combinado.items.find {
            it.producto == datos.pizzaPepperoni
        }?.cantidad == 3
    )

    check(
        combinado.items.find {
            it.producto == datos.pizzaQueso
        }?.cantidad == 2
    )

    check(pedidoA.calcularCantidadPizzas() == 3)
    check(pedidoB.calcularCantidadPizzas() == 2)

    println("PASS: Pedidos combinados sin modificar originales.")
    println("Productos distintos: ${combinado.items.size}")
    println("Pizzas totales: ${combinado.calcularCantidadPizzas()}")
    println("Subtotal: ${combinado.calcularSubtotal()}")
    println("Descuento: ${combinado.calcularDescuento()}")
    println("Total final: ${combinado.calcularTotal()}")
    println("Estado: ${combinado.estado}")
    println("Notas: ${combinado.obtenerNotas()}")

    println()
    println("=== PRUEBA 4: CLIENTES DIFERENTES ===")

    val pedidoOtroCliente = Pedido(
        cliente = datos.clienteSecundario,
        items = listOf(ItemPedido(datos.pizzaPepperoni, 1)),
        estado = "Listo"
    )

    var rechazoCorrecto = false

    try {
        pedidoA + pedidoOtroCliente
    } catch (e: IllegalArgumentException) {
        rechazoCorrecto = true
        println("PASS: ${e.message}")
    }

    check(rechazoCorrecto)

    println()
    println("=== PRUEBA 5: DESESTRUCTURACIÓN ===")

    val (nombre, correo, telefono) = datos.clientePrincipal

    check(nombre == "Matías")
    check(correo == "matias@email.com")
    check(telefono == "987654321")

    println("Cliente: $nombre")
    println("Correo: $correo")
    println("Teléfono: $telefono")

    val (clientePedido, itemsPedido, totalPedido) = combinado

    check(clientePedido == datos.clientePrincipal)
    check(itemsPedido.size == 2)
    check(abs(totalPedido - 50085.5) < 0.001)

    println("Cliente del pedido: ${clientePedido.nombre}")
    println("Productos distintos: ${itemsPedido.size}")
    println("Total desestructurado: $totalPedido")

    println()
    println("=== RESULTADO: TODAS LAS PRUEBAS APROBADAS ===")
}

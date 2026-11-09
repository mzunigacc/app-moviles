import model.Cliente
import model.ComboPromocional
import model.DatosApp
import model.ItemPedido
import model.Pedido
import model.PizzaPromocional
import model.Producto
import service.DatosPruebaFactory
import service.IngredientesPedidoService
import service.InventarioService
import service.PromocionFechaService
import service.PromocionService
import service.ReflectionService
import service.RepartidorService
import service.ReporteService
import service.ValidacionesS3Service
import java.time.LocalDate
import kotlin.math.abs

fun main() {
    val datos = DatosPruebaFactory.crearDatos()

    println("==========================================")
    println("       SISTEMA PIZZERÍA - SUMATIVA S3")
    println("==========================================")

    ejecutarFlujoPedidos(datos)
    demostrarValidacionesS3()
    demostrarOperadoresYDesestructuracion(datos)
    demostrarAnotacionesYPromociones(datos)
    demostrarReflection()

    println()
    println("==========================================")
    println("       SUMATIVA S3: VALIDACIONES OK")
    println("==========================================")
}

fun ejecutarFlujoPedidos(datos: DatosApp) {
    println()
    println("=== 1. PROCESAMIENTO DE PEDIDOS E INVENTARIO ===")

    val inventarioActual = mutableMapOf(
        "Masa" to 10,
        "Salsa de tomate" to 10,
        "Queso" to 12,
        "Pepperoni" to 5
    )

    val inventarioService = InventarioService(inventarioActual)

    println("Inventario inicial:")
    inventarioService.mostrarInventario()

    val pedidosAceptados = mutableListOf<Pedido>()

    for ((indice, pedido) in datos.pedidosDelDia.withIndex()) {
        println()
        println("Procesando pedido ${indice + 1}...")

        val ingredientes =
            IngredientesPedidoService.calcularIngredientesNecesarios(
                pedido.items
            )

        val descontado = inventarioService.descontarIngredientes(
            ingredientes
        )

        if (descontado) {
            pedidosAceptados.add(pedido)
            println("PASS: Pedido ${indice + 1} aceptado.")
        } else {
            println("Pedido ${indice + 1} rechazado por falta de stock.")
        }
    }

    check(pedidosAceptados.size == 3)

    // Cuatro Pepperoni y dos Cuatro Quesos:
    // Masa 6, Salsa 6, Queso 8 y Pepperoni 4.
    check(inventarioActual["Masa"] == 4)
    check(inventarioActual["Salsa de tomate"] == 4)
    check(inventarioActual["Queso"] == 4)
    check(inventarioActual["Pepperoni"] == 1)

    println()
    println("Inventario después de los tres pedidos:")
    inventarioService.mostrarInventario()
    println("PASS: Inventario consistente con los pedidos reportados.")

    println()
    println("=== 2. ESTADO Y ASIGNACIÓN DE REPARTIDOR ===")

    val primerPedido = pedidosAceptados.first()

    primerPedido.cambiarEstado("En preparación")
    println("Estado inicial del flujo: ${primerPedido.estado}")

    primerPedido.cambiarEstado("Listo")
    println("Estado tras preparación: ${primerPedido.estado}")

    val repartidor = RepartidorService.asignarRepartidorMasCercano(
        datos.repartidoresDisponibles
    )

    check(repartidor != null)
    check(repartidor.disponible)

    println("Repartidor asignado: ${repartidor.nombre}")
    println("Distancia: ${repartidor.distanciaKm} km")

    primerPedido.cambiarEstado("En camino")
    println(repartidor.entregarPedido())

    check(!repartidor.disponible)
    check(primerPedido.estado == "En camino")

    primerPedido.cambiarEstado("Entregado")
    repartidor.finalizarEntrega()

    check(repartidor.disponible)
    check(primerPedido.estado == "Entregado")

    println("Estado final: ${primerPedido.estado}")
    println("Repartidor nuevamente disponible: ${repartidor.disponible}")
    println("PASS: Evolución del pedido y disponibilidad verificadas.")

    println()
    println("=== 3. RECHAZO POR STOCK INSUFICIENTE ===")

    val inventarioLimitado = mutableMapOf(
        "Masa" to 1,
        "Salsa de tomate" to 1,
        "Queso" to 1,
        "Pepperoni" to 0
    )

    val servicioLimitado = InventarioService(inventarioLimitado)
    val inventarioOriginal = inventarioLimitado.toMap()

    val ingredientesRequeridos =
        IngredientesPedidoService.calcularIngredientesNecesarios(
            listOf(ItemPedido(datos.pizzaPepperoni, 2))
        )

    val aceptado = servicioLimitado.descontarIngredientes(
        ingredientesRequeridos
    )

    check(!aceptado)
    check(inventarioLimitado == inventarioOriginal)

    println("PASS: Pedido rechazado sin alterar el inventario.")

    println()
    println("=== 4. REPORTE DE PEDIDOS ACEPTADOS ===")

    ReporteService.generarReporteDiario(
        pedidosAceptados.toTypedArray()
    )

    check(pedidosAceptados.count { it.estado == "Entregado" } == 2)
    check(pedidosAceptados.count { it.estado != "Entregado" } == 1)

    println("PASS: Reporte corresponde a pedidos con stock descontado.")
}

fun demostrarValidacionesS3() {
    println()
    println("=== 5. REGEX, TELÉFONOS Y RANGES ===")

    val entradasCorreo = ArrayDeque(
        listOf("correo-invalido", "cliente@pizzeria.cl")
    )

    var intentosCorreo = 0

    val correo = ValidacionesS3Service.solicitarCorreo {
        intentosCorreo++
        entradasCorreo.removeFirstOrNull()
    }

    check(correo == "cliente@pizzeria.cl")
    check(intentosCorreo == 2)

    val entradasTelefono = ArrayDeque(
        listOf("12345", "+569123456789")
    )

    var intentosTelefono = 0

    val telefono = ValidacionesS3Service.solicitarTelefono {
        intentosTelefono++
        entradasTelefono.removeFirstOrNull()
    }

    check(telefono == "+56 (912) 345-6789")
    check(intentosTelefono == 2)

    val entradasCantidad = ArrayDeque(
        listOf("ABC", "0", "101", "5")
    )

    var intentosCantidad = 0

    val cantidad = ValidacionesS3Service.solicitarCantidad {
        intentosCantidad++
        entradasCantidad.removeFirstOrNull()
    }

    check(cantidad == 5)
    check(intentosCantidad == 4)

    println("PASS: Correo, teléfono y cantidad validados con reingreso.")
    println("PASS: Cantidad dentro del rango 1..100.")
}

fun demostrarOperadoresYDesestructuracion(datos: DatosApp) {
    println()
    println("=== 6. IGUALDAD Y ELIMINACIÓN DE DUPLICADOS ===")

    val clienteDuplicado = Cliente(
        nombre = "matías",
        contacto = "900000000",
        correo = "MATIAS@email.com"
    )

    val clientes = listOf(
        datos.clientePrincipal,
        clienteDuplicado,
        datos.clienteSecundario
    )

    val clientesUnicos = clientes.toSet()

    check(datos.clientePrincipal == clienteDuplicado)
    check(clientesUnicos.size == 2)

    val pizzaDuplicada = Producto(
        nombre = "pizza pepperoni",
        precio = datos.pizzaPepperoni.precio,
        ingredientes = datos.pizzaPepperoni.ingredientes,
        categoria = "PIZZA"
    )

    val productos = listOf(
        datos.pizzaPepperoni,
        pizzaDuplicada,
        datos.pizzaQueso
    )

    check(datos.pizzaPepperoni == pizzaDuplicada)
    check(productos.toSet().size == 2)

    println("Clientes registrados: ${clientes.size}")
    println("Clientes únicos: ${clientesUnicos.size}")
    println("Productos registrados: ${productos.size}")
    println("Productos únicos: ${productos.toSet().size}")
    println("PASS: equals(), hashCode() y Set verificados.")

    println()
    println("=== 7. SOBRECARGA DEL OPERADOR + ===")

    val pedidoA = Pedido(
        cliente = datos.clientePrincipal,
        items = listOf(
            ItemPedido(datos.pizzaPepperoni, 2),
            ItemPedido(datos.pizzaQueso, 1)
        ),
        estado = "Listo"
    )

    val pedidoB = Pedido(
        cliente = clienteDuplicado,
        items = listOf(
            ItemPedido(pizzaDuplicada, 1),
            ItemPedido(datos.pizzaQueso, 1)
        ),
        estado = "Listo"
    )

    val combinado = pedidoA + pedidoB

    check(combinado.items.size == 2)
    check(combinado.calcularCantidadPizzas() == 5)
    check(combinado.calcularSubtotal() == 45950.0)
    check(combinado.calcularDescuento() == 4595.0)
    check(abs(combinado.calcularTotal() - 50085.5) < 0.001)

    println(ReporteService.generarResumenPedido(combinado, 4))
    println("PASS: Operador + consolida productos y recalcula totales.")

    var rechazoClientesDiferentes = false

    try {
        pedidoA + datos.pedidosDelDia[1]
    } catch (e: IllegalArgumentException) {
        rechazoClientesDiferentes = true
        println("PASS: ${e.message}")
    }

    check(rechazoClientesDiferentes)

    println()
    println("=== 8. DESESTRUCTURACIÓN ===")

    val (nombre, correo, telefono) = datos.clientePrincipal

    println("Cliente: $nombre")
    println("Correo: $correo")
    println("Teléfono: $telefono")

    val (clientePedido, itemsPedido, totalPedido) = combinado

    println("Cliente del pedido: ${clientePedido.nombre}")
    println("Ítems diferentes: ${itemsPedido.size}")
    println("Total desestructurado: $totalPedido")

    check(nombre == "Matías")
    check(itemsPedido.size == 2)
    check(abs(totalPedido - combinado.calcularTotal()) < 0.001)

    println("PASS: Cliente y Pedido desestructurados correctamente.")
}

fun demostrarAnotacionesYPromociones(datos: DatosApp) {
    println()
    println("=== 9. ANOTACIONES PERSONALIZADAS ===")

    val pizzaEspecial = PizzaPromocional(
        nombre = "Pizza Especial",
        precio = 10000.0,
        ingredientes = mapOf(
            "Masa" to 1,
            "Queso" to 2
        )
    )

    val comboEspecial = ComboPromocional(
        nombre = "Combo Familiar",
        precio = 18990.0,
        ingredientes = mapOf(
            "Masa" to 2,
            "Queso" to 3
        )
    )

    val catalogo: List<Producto> = listOf(
        datos.pizzaPepperoni,
        datos.pizzaQueso,
        pizzaEspecial,
        comboEspecial
    )

    val promocionables =
        PromocionService.identificarPromocionables(catalogo)

    check(promocionables.size == 2)

    PromocionService.mostrarPromociones(catalogo)

    println("PASS: Dos clases con @Promocionable identificadas.")

    println()
    println("=== 10. DESCUENTO SEGÚN FECHA ===")

    val pedidoPromocional = Pedido(
        cliente = datos.clientePrincipal,
        items = listOf(
            ItemPedido(pizzaEspecial, 2),
            ItemPedido(comboEspecial, 1)
        ),
        estado = "En preparación"
    )

    val promocionService = PromocionFechaService(
        fechaInicio = LocalDate.of(2026, 11, 1),
        fechaFin = LocalDate.of(2026, 11, 30)
    )

    val fechaFuera = LocalDate.of(2026, 12, 1)
    val fechaDentro = LocalDate.of(2026, 11, 15)

    val resultadoFuera = promocionService.calcularPromocion(
        pedidoPromocional,
        fechaFuera
    )

    val resultadoDentro = promocionService.calcularPromocion(
        pedidoPromocional,
        fechaDentro
    )

    check(!resultadoFuera.aplicaPromocion)
    check(resultadoDentro.aplicaPromocion)
    check(resultadoDentro.productosElegibles.size == 2)

    // 2 pizzas de $10.000 al 5% = $1.000
    // 1 combo de $18.990 al 8% = $1.519,20
    check(
        abs(resultadoDentro.descuentoPromocional - 2519.2) < 0.001
    )

    check(
        abs(
            resultadoDentro.totalFinal -
                (pedidoPromocional.calcularTotal() - 2519.2)
        ) < 0.001
    )

    promocionService.mostrarResumen(
        pedidoPromocional,
        fechaDentro
    )

    println("PASS: Descuento aplicado solamente dentro del rango.")
    println("PASS: Promociones detectadas mediante anotaciones.")
}

fun demostrarReflection() {
    println()
    println("=== 11. REFLECTION ===")

    ReflectionService.inspeccionarClase(Producto::class.java)

    println()

    ReflectionService.inspeccionarClase(Pedido::class.java)

    val propiedadesProducto = Producto::class.java.declaredFields
        .map { it.name }

    val metodosPedido = Pedido::class.java.declaredMethods
        .map { it.name }

    check("categoria" in propiedadesProducto)
    check("ingredientes" in propiedadesProducto)
    check("plus" in metodosPedido)
    check("component3" in metodosPedido)

    println()
    println("PASS: Inspección dinámica de dos clases completada.")
}

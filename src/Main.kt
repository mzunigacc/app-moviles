import model.DatosApp
import model.Pedido
import model.Usuario
import service.DatosPruebaFactory
import service.EntradaService
import service.InventarioService
import service.RepartidorService
import service.ReporteService
import service.ValidacionService

fun main() {

    val datos =
        DatosPruebaFactory.crearDatos()

    mostrarResumenInicial(datos)
    demostrarPolimorfismo(datos)
    probarAsignacionRepartidor(datos)
    generarReporteDiario(datos)
    mostrarTiposPizzaVendidos(datos)
    probarInventarioAutomatico(datos)
    probarStockInsuficiente(datos)
    probarValidaciones()
    probarManejoErrores()
    probarLet(datos)
}

fun mostrarResumenInicial(
    datos: DatosApp
) {

    println(
        "=== SISTEMA PIZZERÍA ==="
    )

    println()

    println(
        datos.clientePrincipal
            .obtenerDescripcion()
    )

    println(
        datos.repartidorBase
            .obtenerDescripcion()
    )

    println()

    println(
        datos.pizzaPepperoni
            .detallarProducto()
    )

    println()

    val pedidoPrincipal =
        datos.pedidosDelDia.first()

    println(
        "Subtotal: " +
        "$${pedidoPrincipal.calcularSubtotal()}"
    )

    println(
        "Impuesto: " +
        "$${pedidoPrincipal.calcularImpuesto()}"
    )

    println(
        "Descuento: " +
        "$${pedidoPrincipal.calcularDescuento()}"
    )

    println(
        "Total final: " +
        "$${pedidoPrincipal.calcularTotal()}"
    )

    println(
        "Estado: ${pedidoPrincipal.estado}"
    )

    println(
        "Notas: ${pedidoPrincipal.obtenerNotas()}"
    )

    println()

    println(
        datos.repartidorBase
            .entregarPedido()
    )
}

fun demostrarPolimorfismo(
    datos: DatosApp
) {

    println()
    println(
        "=== POLIMORFISMO ==="
    )

    val usuarios: List<Usuario> =
        listOf(
            datos.clientePrincipal,
            datos.repartidorBase
        )

    for (usuario in usuarios) {
        println(
            usuario.obtenerDescripcion()
        )
    }
}

fun probarAsignacionRepartidor(
    datos: DatosApp
) {

    println()
    println(
        "=== ASIGNACIÓN DE REPARTIDOR ==="
    )

    val repartidorAsignado =
        RepartidorService
            .asignarRepartidorMasCercano(
                datos.repartidoresDisponibles
            )

    repartidorAsignado?.let {
        repartidor ->

        println(
            "Repartidor asignado: " +
            repartidor.nombre
        )

        println(
            "Distancia: " +
            "${repartidor.distanciaKm} km"
        )

    } ?: println(
        "No hay repartidores disponibles."
    )
}

fun generarReporteDiario(
    datos: DatosApp
) {

    println()

    ReporteService.generarReporteDiario(
        datos.pedidosDelDia
    )
}

fun mostrarTiposPizzaVendidos(
    datos: DatosApp
) {

    println()
    println(
        "=== TIPOS DE PIZZA VENDIDOS ==="
    )

    val tiposPizzaVendidos: Set<String> =
        datos.pedidosDelDia
            .flatMap { pedido ->
                pedido.productos
            }
            .map { producto ->
                producto.nombre
            }
            .toSet()

    for (
        tipoPizza
        in tiposPizzaVendidos
    ) {
        println("- $tipoPizza")
    }

    println(
        "Cantidad de tipos distintos: " +
        tiposPizzaVendidos.size
    )
}

fun probarInventarioAutomatico(
    datos: DatosApp
) {

    println()
    println(
        "=== PRUEBA DE INVENTARIO AUTOMÁTICO ==="
    )

    val inventario =
        mutableMapOf(
            "Masa" to 10,
            "Salsa de tomate" to 8,
            "Queso" to 10,
            "Pepperoni" to 4
        )

    val servicio =
        InventarioService(
            inventario
        )

    servicio.mostrarInventario()

    println()

    val pedidoPrincipal =
        datos.pedidosDelDia.first()

    val ingredientesNecesarios =
        servicio.calcularIngredientesNecesarios(
            pedidoPrincipal.productos
        )

    mostrarIngredientesNecesarios(
        ingredientesNecesarios
    )

    val disponible =
        servicio.verificarDisponibilidad(
            ingredientesNecesarios
        )

    if (disponible) {

        servicio.descontarIngredientes(
            ingredientesNecesarios
        )

        println()
        println(
            "Pedido aceptado. " +
            "Inventario actualizado:"
        )

        servicio.mostrarInventario()
    }
}

fun probarStockInsuficiente(
    datos: DatosApp
) {

    println()
    println(
        "=== PRUEBA DE STOCK INSUFICIENTE AUTOMÁTICO ==="
    )

    val inventario =
        mutableMapOf(
            "Masa" to 10,
            "Salsa de tomate" to 10,
            "Queso" to 10,
            "Pepperoni" to 4
        )

    val servicio =
        InventarioService(
            inventario
        )

    val pedidoPrueba =
        Pedido(
            cliente =
                datos.clientePrincipal,
            productos = List(5) {
                datos.pizzaPepperoni
            },
            estado = "En preparación"
        )

    servicio.mostrarInventario()

    println()

    val ingredientesNecesarios =
        servicio.calcularIngredientesNecesarios(
            pedidoPrueba.productos
        )

    mostrarIngredientesNecesarios(
        ingredientesNecesarios
    )

    val disponible =
        servicio.verificarDisponibilidad(
            ingredientesNecesarios
        )

    if (!disponible) {
        println()
        println(
            "Pedido de prueba rechazado " +
            "correctamente por falta de stock."
        )
    }
}

fun mostrarIngredientesNecesarios(
    ingredientes: Map<String, Int>
) {

    println(
        "=== INGREDIENTES NECESARIOS ==="
    )

    for (
        (ingrediente, cantidad)
        in ingredientes
    ) {

        println(
            "$ingrediente: $cantidad"
        )
    }

    println()
}

fun probarValidaciones() {

    println()
    println(
        "=== PRUEBA DE VALIDACIONES ==="
    )

    val telefonoValido =
        ValidacionService.validarTelefono(
            "987654321"
        )

    val correoValido =
        ValidacionService.validarCorreo(
            "matias@email.com"
        )

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
        "Teléfono válido: $telefonoValido"
    )

    println(
        "Correo válido: $correoValido"
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

fun probarManejoErrores() {

    println()
    println(
        "=== PRUEBA DE MANEJO DE ERRORES ==="
    )

    val precioValido =
        EntradaService.convertirPrecio(
            "12990"
        )

    val precioTextoInvalido =
        EntradaService.convertirPrecio(
            "ABC"
        )

    val precioNegativo =
        EntradaService.convertirPrecio(
            "-5000"
        )

    val inventarioValido =
        EntradaService
            .validarCantidadInventario(
                10
            )

    val inventarioNegativo =
        EntradaService
            .validarCantidadInventario(
                -5
            )

    println(
        "Precio válido: $precioValido"
    )

    println(
        "Precio inválido: $precioTextoInvalido"
    )

    println(
        "Precio negativo: $precioNegativo"
    )

    println(
        "Inventario válido: $inventarioValido"
    )

    println(
        "Inventario negativo: $inventarioNegativo"
    )
}

fun probarLet(
    datos: DatosApp
) {

    println()
    println(
        "=== PRUEBA DE LET ==="
    )

    val correoParaNotificacion:
        String? =
        datos.clientePrincipal.correo

    correoParaNotificacion?.let {
        correo ->

        println(
            "Notificación enviada a $correo"
        )
    }

    val correoAusente:
        String? = null

    correoAusente?.let {
        correo ->

        println(
            "Notificación enviada a $correo"
        )
    }
}

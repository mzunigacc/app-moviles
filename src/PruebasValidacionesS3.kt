import model.ItemPedido
import model.Pedido
import model.PizzaPromocional
import service.DatosPruebaFactory
import service.PromocionFechaService
import service.ValidacionesS3Service
import java.time.LocalDate
import kotlin.math.abs

fun main() {

    println("=== PRUEBAS REGEX, RANGES Y PROMOCIONES S3 ===")

    println()
    println("=== PRUEBA 1: CORREO CON REINGRESO ===")

    val entradasCorreo = ArrayDeque(
        listOf("correo-invalido", "usuario@dominio.com")
    )

    var intentosCorreo = 0

    val correo = ValidacionesS3Service.solicitarCorreo {
        intentosCorreo++
        entradasCorreo.removeFirstOrNull()
    }

    check(correo == "usuario@dominio.com")
    check(intentosCorreo == 2)

    println("PASS: Correo corregido en segundo intento.")

    println()
    println("=== PRUEBA 2: FORMATO DE TELÉFONO ===")

    val telefono = ValidacionesS3Service.formatearTelefono(
        "+569123456789"
    )

    check(telefono == "+56 (912) 345-6789")
    check(
        ValidacionesS3Service.formatearTelefono("12345") == null
    )

    println("PASS: Teléfono formateado: $telefono")
    println("PASS: Teléfono inválido rechazado.")

    println()
    println("=== PRUEBA 3: CANTIDAD CON REINGRESO ===")

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

    println("PASS: Cantidad válida después de cuatro intentos.")

    val minimo = ValidacionesS3Service.solicitarCantidad {
        "1"
    }

    val maximo = ValidacionesS3Service.solicitarCantidad {
        "100"
    }

    check(minimo == 1)
    check(maximo == 100)

    println("PASS: Límites 1 y 100 aceptados.")

    println()
    println("=== PRUEBA 4: PROMOCIONES POR FECHA ===")

    val datos = DatosPruebaFactory.crearDatos()

    val pizzaEspecial = PizzaPromocional(
        nombre = "Pizza Especial",
        precio = 10000.0,
        ingredientes = mapOf(
            "Masa" to 1,
            "Queso" to 2
        )
    )

    val pedido = Pedido(
        cliente = datos.clientePrincipal,
        items = listOf(
            ItemPedido(pizzaEspecial, 2),
            ItemPedido(datos.pizzaPepperoni, 1)
        ),
        estado = "En preparación"
    )

    val servicio = PromocionFechaService(
        fechaInicio = LocalDate.of(2026, 11, 1),
        fechaFin = LocalDate.of(2026, 11, 30)
    )

    val antes = servicio.calcularPromocion(
        pedido,
        LocalDate.of(2026, 10, 31)
    )

    val durante = servicio.calcularPromocion(
        pedido,
        LocalDate.of(2026, 11, 15)
    )

    val despues = servicio.calcularPromocion(
        pedido,
        LocalDate.of(2026, 12, 1)
    )

    check(!antes.aplicaPromocion)
    check(durante.aplicaPromocion)
    check(!despues.aplicaPromocion)

    check(antes.descuentoPromocional == 0.0)
    check(durante.descuentoPromocional == 1000.0)
    check(despues.descuentoPromocional == 0.0)

    check(
        abs(
            durante.totalFinal -
                (pedido.calcularTotal() - 1000.0)
        ) < 0.001
    )

    println("PASS: Fuera del periodo no aplica descuento.")
    println("PASS: Dentro del periodo aplica descuento.")
    println("PASS: El importe final se recalcula correctamente.")

    check(servicio.estaEnPeriodo(LocalDate.of(2026, 11, 1)))
    check(servicio.estaEnPeriodo(LocalDate.of(2026, 11, 30)))

    println("PASS: Fechas límite incluidas.")

    println()
    servicio.mostrarResumen(
        pedido,
        LocalDate.of(2026, 11, 15)
    )

    println()
    println("=== RESULTADO: TODAS LAS PRUEBAS APROBADAS ===")
}

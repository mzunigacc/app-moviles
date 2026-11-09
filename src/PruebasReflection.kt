import model.ComboPromocional
import model.Pedido
import model.PizzaPromocional
import model.Producto
import service.DatosPruebaFactory
import service.PromocionService
import service.ReflectionService

fun main() {

    println("=== PRUEBAS DE ANOTACIONES Y REFLECTION S3 ===")

    val datos = DatosPruebaFactory.crearDatos()

    val pizzaEspecial = PizzaPromocional(
        nombre = "Pizza Especial",
        precio = 10990.0,
        ingredientes = mapOf(
            "Masa" to 1,
            "Queso" to 2,
            "Salsa de tomate" to 1
        )
    )

    val comboEspecial = ComboPromocional(
        nombre = "Combo Familiar",
        precio = 18990.0,
        ingredientes = mapOf(
            "Masa" to 2,
            "Queso" to 3,
            "Salsa de tomate" to 2
        )
    )

    val productos: List<Producto> = listOf(
        datos.pizzaPepperoni,
        datos.pizzaQueso,
        pizzaEspecial,
        comboEspecial
    )

    println()
    println("=== PRUEBA 1: IDENTIFICACIÓN DINÁMICA ===")

    val elegibles = PromocionService.identificarPromocionables(
        productos
    )

    check(elegibles.size == 2)
    check(pizzaEspecial in elegibles)
    check(comboEspecial in elegibles)
    check(datos.pizzaPepperoni !in elegibles)

    println("PASS: Se identificaron exactamente 2 productos.")
    println()

    PromocionService.mostrarPromociones(productos)

    println("=== PRUEBA 2: METADATOS ===")

    val promoPizza = PromocionService.obtenerPromocion(
        pizzaEspecial
    )

    val promoCombo = PromocionService.obtenerPromocion(
        comboEspecial
    )

    check(promoPizza?.descuentoPorcentaje == 5)
    check(promoCombo?.descuentoPorcentaje == 8)

    println("PASS: La anotación de pizza informa 5%.")
    println("PASS: La anotación de combo informa 8%.")

    println()
    println("=== PRUEBA 3: INSPECCIÓN DE CLASES ===")

    ReflectionService.inspeccionarClase(Producto::class.java)

    println()

    ReflectionService.inspeccionarClase(Pedido::class.java)

    val camposProducto = Producto::class.java.declaredFields
        .map { it.name }

    val camposPedido = Pedido::class.java.declaredFields
        .map { it.name }

    val metodosPedido = Pedido::class.java.declaredMethods
        .map { it.name }

    check("nombre" in camposProducto)
    check("categoria" in camposProducto)
    check("cliente" in camposPedido)
    check("items" in camposPedido)
    check("plus" in metodosPedido)

    println()
    println("PASS: Reflection detectó campos y métodos esperados.")
    println()
    println("=== RESULTADO: TODAS LAS PRUEBAS APROBADAS ===")
}

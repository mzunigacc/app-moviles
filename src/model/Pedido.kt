package model

class Pedido(
    val cliente: Cliente,
    val items: List<ItemPedido>,
    var estado: String,
    val notas: String? = null
) {

    init {
        require(items.isNotEmpty()) {
            "El pedido debe contener al menos un producto."
        }
    }

    // Constructor temporal para mantener compatibilidad con Semana 2.
    // Convierte cada producto antiguo en un ItemPedido de cantidad 1.
    constructor(
        cliente: Cliente,
        productos: List<Producto>,
        estado: String,
        notas: String? = null,
        @Suppress("UNUSED_PARAMETER")
        compatibilidadS2: Boolean = true
    ) : this(
        cliente = cliente,
        items = productos.map { producto ->
            ItemPedido(producto, 1)
        },
        estado = estado,
        notas = notas
    )

    // Adaptador temporal para las funciones antiguas de Main.kt.
    val productos: List<Producto>
        get() = items.flatMap { item ->
            List(item.cantidad) {
                item.producto
            }
        }

    fun calcularCantidadPizzas(): Int {
        return items.sumOf { item ->
            item.cantidad
        }
    }

    fun calcularSubtotal(): Double {
        return items.sumOf { item ->
            item.calcularSubtotal()
        }
    }

    fun calcularImpuesto(
        tasaImpuesto: Double = 0.19
    ): Double {

        require(tasaImpuesto in 0.0..1.0) {
            "La tasa de impuesto debe estar entre 0 y 1."
        }

        return calcularSubtotal() * tasaImpuesto
    }

    fun calcularDescuento(): Double {
        return if (calcularCantidadPizzas() > 3) {
            calcularSubtotal() * 0.10
        } else {
            0.0
        }
    }

    fun calcularTotal(
        tasaImpuesto: Double = 0.19
    ): Double {

        val subtotal = calcularSubtotal()
        val impuesto = calcularImpuesto(tasaImpuesto)
        val descuento = calcularDescuento()

        return subtotal + impuesto - descuento
    }

    fun obtenerNotas(): String {
        return notas ?: "Sin notas"
    }

    fun cambiarEstado(nuevoEstado: String) {
        estado = nuevoEstado
    }
}

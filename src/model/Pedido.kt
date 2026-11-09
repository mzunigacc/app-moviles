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

    // Compatibilidad temporal con las llamadas antiguas de Semana 2.
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

    val productos: List<Producto>
        get() = items.flatMap { item ->
            List(item.cantidad) { item.producto }
        }

    fun calcularCantidadPizzas(): Int {
        return items.sumOf { it.cantidad }
    }

    fun calcularSubtotal(): Double {
        return items.sumOf { it.calcularSubtotal() }
    }

    fun calcularImpuesto(tasaImpuesto: Double = 0.19): Double {
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

    fun calcularTotal(tasaImpuesto: Double = 0.19): Double {
        return calcularSubtotal() +
            calcularImpuesto(tasaImpuesto) -
            calcularDescuento()
    }

    fun obtenerNotas(): String {
        return notas ?: "Sin notas"
    }

    fun cambiarEstado(nuevoEstado: String) {
        estado = nuevoEstado
    }

    operator fun plus(otro: Pedido): Pedido {
        require(cliente == otro.cliente) {
            "No se pueden combinar pedidos de clientes diferentes."
        }

        val itemsCombinados = mutableListOf<ItemPedido>()

        for (item in items + otro.items) {
            val indiceExistente = itemsCombinados.indexOfFirst {
                it.producto == item.producto
            }

            if (indiceExistente == -1) {
                itemsCombinados.add(item)
            } else {
                val existente = itemsCombinados[indiceExistente]

                require(
                    existente.producto.precio == item.producto.precio &&
                    existente.producto.ingredientes == item.producto.ingredientes
                ) {
                    "Un mismo producto tiene precios o recetas diferentes."
                }

                val nuevaCantidad = Math.addExact(
                    existente.cantidad,
                    item.cantidad
                )

                itemsCombinados[indiceExistente] = ItemPedido(
                    producto = existente.producto,
                    cantidad = nuevaCantidad
                )
            }
        }

        val notasCombinadas = listOfNotNull(notas, otro.notas)
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(" | ")
            .ifBlank { null }

        return Pedido(
            cliente = cliente,
            items = itemsCombinados,
            estado = "En preparación",
            notas = notasCombinadas
        )
    }

    operator fun component1(): Cliente = cliente

    operator fun component2(): List<ItemPedido> = items

    operator fun component3(): Double = calcularTotal()
}

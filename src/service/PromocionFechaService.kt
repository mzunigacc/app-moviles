package service

import model.Pedido
import java.time.LocalDate

data class ResultadoPromocion(
    val aplicaPromocion: Boolean,
    val descuentoPromocional: Double,
    val totalFinal: Double,
    val productosElegibles: List<String>
)

class PromocionFechaService(
    private val fechaInicio: LocalDate,
    private val fechaFin: LocalDate
) {

    init {
        require(!fechaFin.isBefore(fechaInicio)) {
            "La fecha final no puede ser anterior a la inicial."
        }
    }

    fun estaEnPeriodo(fechaPedido: LocalDate): Boolean {
        return fechaPedido in fechaInicio..fechaFin
    }

    fun calcularPromocion(
        pedido: Pedido,
        fechaPedido: LocalDate
    ): ResultadoPromocion {

        val totalBase = pedido.calcularTotal()

        if (!estaEnPeriodo(fechaPedido)) {
            return ResultadoPromocion(
                aplicaPromocion = false,
                descuentoPromocional = 0.0,
                totalFinal = totalBase,
                productosElegibles = emptyList()
            )
        }

        var descuento = 0.0
        val productosElegibles = mutableListOf<String>()

        for (item in pedido.items) {

            val anotacion = PromocionService.obtenerPromocion(
                item.producto
            ) ?: continue

            val porcentaje = anotacion.descuentoPorcentaje

            require(porcentaje in 1..100) {
                "El porcentaje promocional debe estar entre 1 y 100."
            }

            descuento += item.calcularSubtotal() *
                porcentaje / 100.0

            productosElegibles.add(item.producto.nombre)
        }

        return ResultadoPromocion(
            aplicaPromocion = productosElegibles.isNotEmpty(),
            descuentoPromocional = descuento,
            totalFinal = totalBase - descuento,
            productosElegibles = productosElegibles.distinct()
        )
    }

    fun mostrarResumen(
        pedido: Pedido,
        fechaPedido: LocalDate
    ) {

        val resultado = calcularPromocion(pedido, fechaPedido)

        println("=== PROMOCIÓN POR FECHA ===")
        println("Fecha del pedido: $fechaPedido")
        println("Periodo: $fechaInicio al $fechaFin")
        println("Dentro del periodo: ${estaEnPeriodo(fechaPedido)}")

        println(
            "Productos elegibles: " +
            resultado.productosElegibles.ifEmpty {
                listOf("Ninguno")
            }.joinToString()
        )

        println("Total sin promoción: ${pedido.calcularTotal()}")
        println(
            "Descuento promocional: ${resultado.descuentoPromocional}"
        )
        println("Total final: ${resultado.totalFinal}")
    }
}

package service

import model.ItemPedido

object IngredientesPedidoService {

    fun calcularIngredientesNecesarios(
        items: List<ItemPedido>
    ): Map<String, Int> {

        val necesidades = mutableMapOf<String, Int>()

        for (item in items) {

            for ((ingrediente, cantidadPorPizza) in item.producto.ingredientes) {

                require(cantidadPorPizza > 0) {
                    "Cantidad inválida del ingrediente $ingrediente."
                }

                val cantidadNecesaria =
                    Math.multiplyExact(cantidadPorPizza, item.cantidad)

                val acumulado = necesidades[ingrediente] ?: 0

                necesidades[ingrediente] =
                    Math.addExact(acumulado, cantidadNecesaria)
            }
        }

        return necesidades
    }
}

package service

import model.Pedido

object ReporteService {

    fun generarResumenPedido(
        pedido: Pedido,
        numeroPedido: Int
    ): String {

        val productosAgrupados =
            pedido.productos.groupBy { producto ->
                producto.nombre
            }

        return buildString {

            appendLine("Pedido $numeroPedido")
            appendLine()

            appendLine("Cliente:")
            appendLine("Nombre: ${pedido.cliente.nombre}")
            appendLine("Teléfono: ${pedido.cliente.contacto}")
            appendLine("Correo: ${pedido.cliente.correo}")
            appendLine()

            appendLine("Productos:")

            for ((_, productos) in productosAgrupados) {

                val producto =
                    productos.first()

                val cantidad =
                    productos.size

                appendLine(
                    "- ${producto.nombre} | " +
                    "Precio: $${producto.precio} | " +
                    "Cantidad: $cantidad"
                )
            }

            appendLine()

            appendLine("Totales:")
            appendLine(
                "Subtotal: $${pedido.calcularSubtotal()}"
            )
            appendLine(
                "Impuesto: $${pedido.calcularImpuesto()}"
            )
            appendLine(
                "Descuento: $${pedido.calcularDescuento()}"
            )
            appendLine(
                "Total final: $${pedido.calcularTotal()}"
            )

            appendLine()

            appendLine(
                "Estado: ${pedido.estado}"
            )

            append(
                "Notas: ${pedido.obtenerNotas()}"
            )
        }
    }

    fun generarReporteDiario(
        pedidos: Array<Pedido>
    ) {

        println(
            "=== REPORTE DE PEDIDOS DEL DÍA ==="
        )

        for (
            (indice, pedido)
            in pedidos.withIndex()
        ) {

            println(
                generarResumenPedido(
                    pedido,
                    indice + 1
                )
            )

            println()
        }

        val completados =
            pedidos.filter { pedido ->
                pedido.estado == "Entregado"
            }

        val pendientes =
            pedidos.filter { pedido ->
                pedido.estado != "Entregado"
            }

        println(
            "Pedidos completados: ${completados.size}"
        )

        println(
            "Pedidos pendientes: ${pendientes.size}"
        )
    }
}

package service

import model.Cliente
import model.DatosApp
import model.Pedido
import model.Producto
import model.Repartidor

object DatosPruebaFactory {

    fun crearDatos(): DatosApp {

        val clientePrincipal = Cliente(
            nombre = "Matías",
            contacto = ValidacionService.validarTelefono(
                "987654321"
            ),
            correo = ValidacionService.validarCorreo(
                "matias@email.com"
            )
        )

        val clienteSecundario = Cliente(
            nombre = "Camila",
            contacto = ValidacionService.validarTelefono(
                "976543210"
            ),
            correo = ValidacionService.validarCorreo(
                "camila@email.com"
            )
        )

        val pizzaPepperoni = Producto(
            nombre = "Pizza Pepperoni",
            precio = 8990.0,
            ingredientes = mapOf(
                "Masa" to 1,
                "Salsa de tomate" to 1,
                "Queso" to 1,
                "Pepperoni" to 1
            )
        )

        val pizzaQueso = Producto(
            nombre = "Pizza Cuatro Quesos",
            precio = 9490.0,
            ingredientes = mapOf(
                "Masa" to 1,
                "Salsa de tomate" to 1,
                "Queso" to 2
            )
        )

        val pedido1 = Pedido(
            cliente = clientePrincipal,
            productos = listOf(
                pizzaPepperoni,
                pizzaPepperoni,
                pizzaPepperoni
            ),
            estado = "Listo",
            notas = null
        )

        val pedido2 = Pedido(
            cliente = clienteSecundario,
            productos = listOf(
                pizzaQueso,
                pizzaPepperoni
            ),
            estado = "En camino",
            notas = "Entregar en recepción"
        )

        val pedido3 = Pedido(
            cliente = clientePrincipal,
            productos = listOf(
                pizzaQueso
            ),
            estado = "Entregado"
        )

        val repartidorBase = Repartidor(
            nombre = "Carlos",
            contacto = "912345678",
            distanciaKm = 2.5
        )

        val repartidoresDisponibles = listOf(
            repartidorBase,
            Repartidor(
                nombre = "Andrea",
                contacto = "923456789",
                distanciaKm = 1.2
            ),
            Repartidor(
                nombre = "Felipe",
                contacto = "934567890",
                distanciaKm = 4.8
            )
        )

        return DatosApp(
            clientePrincipal = clientePrincipal,
            clienteSecundario = clienteSecundario,
            repartidorBase = repartidorBase,
            pizzaPepperoni = pizzaPepperoni,
            pizzaQueso = pizzaQueso,
            pedidosDelDia = arrayOf(
                pedido1,
                pedido2,
                pedido3
            ),
            repartidoresDisponibles = repartidoresDisponibles
        )
    }
}

package service

import model.Cliente
import model.DatosApp
import model.ItemPedido
import model.Pedido
import model.Producto
import model.Repartidor

object DatosPruebaFactory {

    fun crearDatos(): DatosApp {

        // Clientes
        val clientePrincipal = Cliente(
            nombre = "Matías",
            contacto = "987654321",
            correo = "matias@email.com"
        )

        val clienteSecundario = Cliente(
            nombre = "Camila",
            contacto = "976543210",
            correo = "camila@email.com"
        )

        // Productos
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

        // Pedido 1: tres pizzas del mismo tipo.
        // Ahora corresponde a un solo ItemPedido con cantidad 3.
        val pedido1 = Pedido(
            cliente = clientePrincipal,
            items = listOf(
                ItemPedido(
                    producto = pizzaPepperoni,
                    cantidad = 3
                )
            ),
            estado = "Listo"
        )

        // Pedido 2: dos tipos de pizza.
        val pedido2 = Pedido(
            cliente = clienteSecundario,
            items = listOf(
                ItemPedido(
                    producto = pizzaQueso,
                    cantidad = 1
                ),
                ItemPedido(
                    producto = pizzaPepperoni,
                    cantidad = 1
                )
            ),
            estado = "En camino",
            notas = "Entregar en recepción"
        )

        // Pedido 3: una pizza entregada.
        val pedido3 = Pedido(
            cliente = clientePrincipal,
            items = listOf(
                ItemPedido(
                    producto = pizzaQueso,
                    cantidad = 1
                )
            ),
            estado = "Entregado"
        )

        // Repartidores
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

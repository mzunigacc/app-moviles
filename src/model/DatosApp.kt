package model

data class DatosApp(
    val clientePrincipal: Cliente,
    val clienteSecundario: Cliente,
    val repartidorBase: Repartidor,
    val pizzaPepperoni: Producto,
    val pizzaQueso: Producto,
    val pedidosDelDia: Array<Pedido>,
    val repartidoresDisponibles: List<Repartidor>
)

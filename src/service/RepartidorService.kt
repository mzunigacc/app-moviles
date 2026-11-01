package service

import model.Repartidor

object RepartidorService {

    fun asignarRepartidorMasCercano(
        repartidores: List<Repartidor>
    ): Repartidor? {

        val distanciaMinima =
            repartidores.minOfOrNull { repartidor ->
                repartidor.distanciaKm
            }

        return repartidores.find { repartidor ->
            repartidor.distanciaKm == distanciaMinima
        }
    }
}

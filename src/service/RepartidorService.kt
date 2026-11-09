package service

import model.Repartidor

object RepartidorService {

    fun asignarRepartidorMasCercano(
        repartidores: List<Repartidor>
    ): Repartidor? {

        return repartidores
            .filter { repartidor ->
                repartidor.disponible
            }
            .minByOrNull { repartidor ->
                repartidor.distanciaKm
            }
    }
}

package model

class Cliente(
    nombre: String,
    contacto: String,
    val correo: String
) : Usuario(nombre, contacto) {

    override fun obtenerDescripcion(): String {
        return "Cliente: $nombre | Teléfono: $contacto | Correo: $correo"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cliente) return false

        return nombre.trim().lowercase() == other.nombre.trim().lowercase() &&
            correo.trim().lowercase() == other.correo.trim().lowercase()
    }

    override fun hashCode(): Int {
        var resultado = nombre.trim().lowercase().hashCode()
        resultado = 31 * resultado + correo.trim().lowercase().hashCode()
        return resultado
    }

    operator fun component1(): String = nombre

    operator fun component2(): String = correo

    operator fun component3(): String = contacto
}

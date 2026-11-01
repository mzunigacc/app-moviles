package service

object ValidacionService {

    fun validarTelefono(telefono: String?): String {
        val telefonoLimpio = telefono?.trim()

        return if (
            telefonoLimpio != null &&
            telefonoLimpio.matches(Regex("^\\d{9}$"))
        ) {
            telefonoLimpio
        } else {
            "(000) 000-0000"
        }
    }

    fun validarCorreo(correo: String?): String {
        val correoLimpio = correo?.trim()

        return if (
            correoLimpio != null &&
            correoLimpio.matches(
                Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
            )
        ) {
            correoLimpio
        } else {
            "correo@invalido.com"
        }
    }
}

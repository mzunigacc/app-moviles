package annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Promocionable(
    val descuentoPorcentaje: Int,
    val descripcion: String
)

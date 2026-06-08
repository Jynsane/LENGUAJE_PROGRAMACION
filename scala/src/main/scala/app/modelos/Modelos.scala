package app.modelos

/**
 * Representa un paciente con sus síntomas reportados.
 *
 * @param nombre   Nombre del paciente
 * @param edad     Edad del paciente
 * @param sintomas Lista de síntomas reportados
 */
case class Paciente(
  nombre: String,
  edad: Int,
  sintomas: List[String]
)

/**
 * Resultado del diagnóstico para un paciente.
 *
 * @param paciente      Paciente evaluado
 * @param enfermedades  Lista de posibles enfermedades detectadas
 * @param esUrgente     Indica si el caso requiere atención inmediata
 */
case class ResultadoDiagnostico(
  paciente: Paciente,
  enfermedades: List[String],
  esUrgente: Boolean
) {
  override def toString: String = {
    val urgenciaStr = if (esUrgente) "⚠️  URGENTE — Acuda a emergencias" else "✅ No urgente"
    val enfermedadesStr = if (enfermedades.isEmpty) "Ninguna detectada" else enfermedades.mkString(", ")
    s"""
    |━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    |  Paciente : ${paciente.nombre} (${paciente.edad} años)
    |  Síntomas : ${paciente.sintomas.mkString(", ")}
    |  Posible(s): $enfermedadesStr
    |  Estado   : $urgenciaStr
    |━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
    """.stripMargin
  }
}

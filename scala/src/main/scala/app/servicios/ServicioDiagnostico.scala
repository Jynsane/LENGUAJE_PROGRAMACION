package app.servicios

import app.modelos.{Paciente, ResultadoDiagnostico}

/**
 * Servicio principal de diagnóstico.
 * Coordina la validación de síntomas, la consulta a Prolog
 * y la evaluación de urgencia.
 */
object ServicioDiagnostico {

  // Síntomas que indican urgencia médica
  private val sintomasUrgentes = Set(
    "dificultad_para_respirar",
    "dolor_de_pecho"
  )

  // Lista de síntomas válidos (debe coincidir con hechos.pl)
  private val sintomasValidos = Set(
    "fiebre", "tos", "dolor_de_garganta", "congestion_nasal",
    "dolor_de_cabeza", "fatiga", "dolor_muscular", "escalofrios",
    "nauseas", "vomitos", "diarrea", "dolor_abdominal",
    "perdida_de_olfato", "perdida_de_gusto", "dificultad_para_respirar",
    "erupcion_cutanea", "dolor_de_pecho", "mareos",
    "sudoracion_nocturna", "perdida_de_peso",
    // nuevos
    "dolor_de_oido", "disuria", "dolor_facial", "tos_con_esputo", "perdida_de_apetito"
  )

  /**
   * Valida los síntomas ingresados contra la lista oficial.
   * @return Left con síntomas inválidos, Right con síntomas válidos
   */
  def validarSintomas(sintomas: List[String]): Either[List[String], List[String]] = {
    val invalidos = sintomas.filterNot(sintomasValidos.contains)
    if (invalidos.nonEmpty) Left(invalidos)
    else Right(sintomas)
  }

  /**
   * Evalúa si el caso del paciente requiere atención urgente.
   */
  def esUrgente(sintomas: List[String]): Boolean =
    sintomas.exists(sintomasUrgentes.contains)

  /**
   * Realiza el diagnóstico completo de un paciente.
   * 1. Valida los síntomas
   * 2. Consulta el motor Prolog
   * 3. Evalúa urgencia
   * 4. Retorna el resultado
   */
  def diagnosticar(paciente: Paciente): Either[String, ResultadoDiagnostico] = {
    validarSintomas(paciente.sintomas) match {
      case Left(invalidos) =>
        Left(s"Síntomas no reconocidos: ${invalidos.mkString(", ")}")

      case Right(sintomasOk) =>
        ServicioPrologBridge.consultarProlog(sintomasOk) match {
          case Left(error) =>
            // Si Prolog no está disponible, devolvemos diagnóstico vacío
            println(s"[ADVERTENCIA] $error — Ejecutando sin motor Prolog.")
            val resultado = ResultadoDiagnostico(
              paciente     = paciente,
              enfermedades = List.empty,
              esUrgente    = esUrgente(sintomasOk)
            )
            Right(resultado)

          case Right(salida) =>
            val enfermedades = ServicioPrologBridge.parsearDiagnosticos(salida)
            val resultado = ResultadoDiagnostico(
              paciente     = paciente,
              enfermedades = enfermedades,
              esUrgente    = esUrgente(sintomasOk)
            )
            Right(resultado)
        }
    }
  }

  /**
   * Retorna la lista de síntomas válidos para mostrar al usuario.
   */
  def sintomasDisponibles: List[String] = sintomasValidos.toList.sorted
}

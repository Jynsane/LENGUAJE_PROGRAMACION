package app

import app.modelos.Paciente
import app.servicios.ServicioDiagnostico

import scala.io.StdIn
import scala.annotation.tailrec
import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import ujson.{Arr, Obj}
import scala.util.{Try, Success, Failure}



/**
 * ============================================================
 *  SISTEMA EXPERTO DE DIAGNÓSTICO DE ENFERMEDADES
 *  Punto de entrada principal
 *
 *  Arquitectura:
 *    Scala  →  lógica principal + UI de consola
 *    Prolog →  motor de inferencia (reglas y hechos médicos)
 * ============================================================
 */
object Main extends App {

  mostrarBienvenida()
  menuPrincipal()

  // ─────────────────────────────────────────────────────────
  //  MENÚ PRINCIPAL
  // ─────────────────────────────────────────────────────────

  @tailrec
  def menuPrincipal(): Unit = {
    println(
      """
      |╔══════════════════════════════════════╗
      |║   SISTEMA DE DIAGNÓSTICO MÉDICO      ║
      |╠══════════════════════════════════════╣
      |║  1. Nuevo diagnóstico                ║
      |║  2. Ver síntomas disponibles         ║
      |║  3. Ejecutar casos de prueba         ║
      |║  4. Procesar archivo de entrada      ║
      |║  5. Salir                            ║
      |╚══════════════════════════════════════╝
      """.stripMargin
    )
    print("Seleccione una opción: ")

    StdIn.readLine().trim match {
      case "1" => flujoNuevoDiagnostico(); menuPrincipal()
      case "2" => mostrarSintomas();       menuPrincipal()
      case "3" => ejecutarCasosPrueba();   menuPrincipal()
      case "4" => procesarArchivoEntrada(); menuPrincipal()
      case "5" => println("\n  Hasta luego. ¡Cuide su salud!\n")
      case _   => println("  Opción inválida."); menuPrincipal()
    }
  }

  // ─────────────────────────────────────────────────────────
  //  FLUJO: NUEVO DIAGNÓSTICO
  // ─────────────────────────────────────────────────────────

  def flujoNuevoDiagnostico(): Unit = {
    println("\n─── Ingrese los datos del paciente ───")

    print("Nombre del paciente : ")
    val nombre = StdIn.readLine().trim

    print("Edad                : ")
    val edad = StdIn.readLine().trim.toIntOption.getOrElse(0)

    println("Síntomas (separados por coma):")
    println("  Ejemplo: fiebre,tos,fatiga,escalofrios")
    print("  > ")
    val sintomasInput = StdIn.readLine().trim
    val sintomas = sintomasInput.split(",").map(_.trim.toLowerCase).toList

    val paciente = Paciente(nombre, edad, sintomas)

    println("\n  Consultando motor de inferencia Prolog...")

    ServicioDiagnostico.diagnosticar(paciente) match {
      case Left(error)      => println(s"\n  ❌ Error: $error")
      case Right(resultado) => println(resultado)
    }
  }

  // ─────────────────────────────────────────────────────────
  //  FLUJO: MOSTRAR SÍNTOMAS DISPONIBLES
  // ─────────────────────────────────────────────────────────

  def mostrarSintomas(): Unit = {
    println("\n─── Síntomas reconocidos por el sistema ───")
    ServicioDiagnostico.sintomasDisponibles.zipWithIndex.foreach { case (s, i) =>
      println(s"  ${i + 1}. $s")
    }
    println()
  }

  // ─────────────────────────────────────────────────────────
  //  FLUJO: CASOS DE PRUEBA
  // ─────────────────────────────────────────────────────────

  def ejecutarCasosPrueba(): Unit = {
    println("\n─── Ejecutando casos de prueba ───\n")

    val casosDePrueba = List(
      Paciente("Ana García",      28, List("fiebre","dolor_muscular","fatiga","escalofrios")),
      Paciente("Luis Torres",     45, List("fiebre","perdida_de_olfato","perdida_de_gusto","fatiga")),
      Paciente("María López",     32, List("tos","congestion_nasal","dolor_de_garganta")),
      Paciente("Carlos Ruiz",     55, List("fiebre","erupcion_cutanea","dolor_muscular","dolor_de_cabeza")),
      Paciente("Elena Vargas",    19, List("nauseas","vomitos","diarrea","dolor_abdominal")),
      Paciente("Pedro Salas",     67, List("fiebre","dificultad_para_respirar","dolor_de_pecho","tos")),
      Paciente("Rosa Mendoza",    40, List("dolor_de_oido","fiebre")),
      Paciente("Jorge Medina",    60, List("tos","dificultad_para_respirar","fatiga")),
      Paciente("Lucía Fernández", 23, List("congestion_nasal","dolor_facial","dolor_de_cabeza")),
      Paciente("Pablo Gómez",     50, List("dolor_de_cabeza","nauseas","vomitos","mareos")),
      Paciente("Sofía Castro",    27, List("disuria","fiebre")),
      Paciente("Ignacio Peña",    38, List("erupcion_cutanea","fiebre","perdida_de_apetito")),
      Paciente("Valeria Ruiz",    16, List("tos","fatiga","fiebre")),
      Paciente("Diego Navarro",   72, List("tos","sudoracion_nocturna","perdida_de_peso","fatiga")),
      Paciente("Marta Blanco",    34, List("fiebre","dolor_muscular","congestion_nasal")),
      Paciente("Óscar Vidal",     48, List("dificultad_para_respirar","tos")),
      Paciente("Paula Ortiz",     29, List("nauseas","vomitos","dolor_abdominal")),
      Paciente("Ricardo Fuentes", 53, List("fiebre","erupcion_cutanea","dolor_de_cabeza")),
      Paciente("Beatriz Cano",    21, List("dolor_de_oido","fiebre","dolor_de_cabeza")),
      Paciente("Héctor Salazar",  44, List("perdida_de_olfato","perdida_de_gusto","fatiga"))
    )

    casosDePrueba.foreach { paciente =>
      ServicioDiagnostico.diagnosticar(paciente) match {
        case Left(error)      => println(s"  ❌ ${paciente.nombre}: $error")
        case Right(resultado) => println(resultado)
      }
    }
  }

  // ─────────────────────────────────────────────────────────
  //  FLUJO: PROCESAR ARCHIVO DE ENTRADA -> SALIDA JSON
  // ─────────────────────────────────────────────────────────

  def procesarArchivoEntrada(): Unit = {
    println("\n─── Procesando archivo de entrada (pacientes_ampliados.json) ───")

    val entradaPath = Paths.get("../data/entrada/pacientes_ampliados.json")
    val salidaPath  = Paths.get("../data/salida/resultados_ampliados.json")

    if (!Files.exists(entradaPath)) {
      println(s"  Archivo de entrada no encontrado: ${entradaPath.toString}")
      return
    }

    val contenido = new String(Files.readAllBytes(entradaPath), StandardCharsets.UTF_8)

    Try { ujson.read(contenido) } match {
      case Failure(ex) => println(s"  Error parseando JSON de entrada: ${ex.getMessage}")
      case Success(json) =>
        val arr = json.arr
        val resultados = arr.map { item =>
          val nombre = item("nombre").str
          val edad   = item("edad").num.toInt
          val sintomas = item("sintomas").arr.map(_.str).toList
          val paciente = app.modelos.Paciente(nombre, edad, sintomas)

          ServicioDiagnostico.diagnosticar(paciente) match {
            case Left(error) =>
              Obj(
                "nombre" -> nombre,
                "edad"   -> edad,
                "sintomas" -> ujson.Arr.from(sintomas.map(ujson.Str(_))),
                "error"  -> error
              )

            case Right(res) =>
              Obj(
                "nombre" -> nombre,
                "edad"   -> edad,
                "sintomas" -> ujson.Arr.from(sintomas.map(ujson.Str(_))),
               "enfermedades" -> ujson.Arr.from(res.enfermedades.map(ujson.Str(_))),
                "esUrgente" -> res.esUrgente
              )
          }
        }

        // Escribir archivo de salida (formateado)
        val salidaJson = ujson.Arr.from(resultados)
        Files.createDirectories(salidaPath.getParent)
        Files.write(salidaPath, ujson.write(salidaJson, indent = 2).getBytes(StandardCharsets.UTF_8))
        println(s"  Procesamiento finalizado — resultados escritos en: ${salidaPath.toString}")
    }
  }

  // ─────────────────────────────────────────────────────────
  //  UTILIDAD: BIENVENIDA
  // ─────────────────────────────────────────────────────────

  def mostrarBienvenida(): Unit = {
    println("""
    |╔══════════════════════════════════════════════════════╗
    |║                                                      ║
    |║   SISTEMA EXPERTO DE DIAGNÓSTICO DE ENFERMEDADES     ║
    |║                                                      ║
    |║   Motor de inferencia : Prolog (SWI-Prolog)          ║
    |║   Lógica principal    : Scala                        ║
    |║                                                      ║
    |║   ADVERTENCIA: Este sistema es académico.            ║
    |║   No reemplaza la consulta médica profesional.       ║
    |║                                                      ║
    |╚══════════════════════════════════════════════════════╝
    """.stripMargin)
  }
}

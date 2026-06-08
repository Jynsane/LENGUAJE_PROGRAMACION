# 🏥 Sistema Experto de Diagnóstico de Enfermedades

> Sistema multilenguaje (Scala + Prolog) para diagnóstico médico basado en síntomas.

## 📋 Descripción

Este sistema experto recibe síntomas de un paciente y utiliza un motor de inferencia lógica en **Prolog** para determinar posibles enfermedades. La lógica principal, validación y la interfaz de consola están implementadas en **Scala**.

## 🗂️ Estructura del Proyecto

```
diagnostico-enfermedades/
│
├── docs/
│   └── arquitectura.md          ← Documentación técnica
│
├── scala/
│   ├── build.sbt                ← Configuración del proyecto Scala
│   └── src/main/scala/app/
│       ├── Main.scala           ← Punto de entrada + menú
│       ├── modelos/
│       │   └── Modelos.scala    ← Paciente, ResultadoDiagnostico
│       └── servicios/
│           ├── ServicioDiagnostico.scala     ← Lógica principal
│           └── ServicioPrologBridge.scala    ← Comunicación con Prolog
│
├── prolog/
│   ├── hechos.pl                ← Síntomas y enfermedades
│   ├── reglas.pl                ← Reglas de diagnóstico
│   └── consultas.pl             ← Punto de entrada Prolog + demos
│
├── integracion/
│   └── comunicacion_scala_prolog.md
│
├── data/
│   └── entrada/
│       └── pacientes_prueba.json
│
└── README.md
```

## ⚙️ Requisitos

| Herramienta | Versión mínima |
|-------------|---------------|
| JDK         | 11+           |
| Scala       | 3.3.x         |
| sbt         | 1.9+          |
| SWI-Prolog  | 8.x           |

## 🚀 Cómo ejecutar

### 1. Solo Prolog (demo rápida)
```bash
cd prolog
swipl -s consultas.pl
```

### 2. Proyecto Scala completo
```bash
cd scala
sbt run
```

> **Importante:** Ejecutar `sbt run` desde la carpeta `scala/`, pero SWI-Prolog necesita que la ruta `prolog/consultas.pl` sea accesible. Si hay problemas de ruta, ejecutar desde la raíz del proyecto:
> ```bash
> cd scala && sbt "run" 2>&1
> ```

### 3. Consulta Prolog manual
```bash
swipl -s prolog/consultas.pl \
  -g "obtener_diagnosticos([fiebre,fatiga,escalofrios,dolor_muscular], D), write(D), nl, halt"
```

## 🧠 Enfermedades diagnosticables

| Enfermedad | Síntomas clave |
|------------|---------------|
| Gripe | fiebre, dolor_muscular, fatiga, escalofrios |
| Resfriado común | tos, congestion_nasal, dolor_de_garganta |
| COVID-19 | fiebre, perdida_de_olfato, perdida_de_gusto, fatiga |
| Dengue | fiebre, erupcion_cutanea, dolor_muscular, dolor_de_cabeza |
| Gastroenteritis | nauseas, vomitos, diarrea, dolor_abdominal |
| Neumonía | fiebre, dificultad_para_respirar, dolor_de_pecho, tos |
| Amigdalitis | dolor_de_garganta, fiebre, dificultad_para_respirar |
| Tuberculosis | tos, sudoracion_nocturna, perdida_de_peso, fatiga |

## ⚠️ Aviso

Este sistema es **académico** y **no reemplaza** la consulta médica profesional.

## 📚 Referencias

- Bratko, I. *Prolog Programming for Artificial Intelligence*
- Odersky, M. *Programming in Scala*

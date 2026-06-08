# Comunicación Scala ↔ Prolog

## Mecanismo

Scala invoca a SWI-Prolog como **proceso externo** usando `scala.sys.process`.

## Flujo de comunicación

```
Paciente (Scala)
    │
    ▼
ServicioDiagnostico.diagnosticar()
    │
    ▼
ServicioPrologBridge.consultarProlog(sintomas)
    │  Construye: swipl -s prolog/consultas.pl -g "obtener_diagnosticos([...], D), write(D), nl, halt"
    ▼
SWI-Prolog evalúa reglas.pl + hechos.pl
    │  Retorna: [gripe] o [covid19, neumonia] etc.
    ▼
ServicioPrologBridge.parsearDiagnosticos(salida)
    │
    ▼
ResultadoDiagnostico (Scala)
```

## Ejemplo de comando generado

```bash
swipl -s prolog/consultas.pl \
  -g "obtener_diagnosticos([fiebre,fatiga,escalofrios,dolor_muscular], D), write(D), nl, halt"
```

## Salida esperada de Prolog

```
[gripe]
```

## Requisito

- SWI-Prolog instalado y disponible en el PATH del sistema.
- Ejecutar desde la raíz del proyecto para que la ruta relativa `prolog/consultas.pl` sea válida.

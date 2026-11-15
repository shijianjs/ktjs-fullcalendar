---
sidebar_position: 3
title: API overview
---

# API overview

## Package layout

| Package | Contents |
| --- | --- |
| `io.github.shijianjs.ktjs.fullcalendar` | `FullCalendarModule`, the six sub-path module objects, the `dayGridPlugin` / `timeGridPlugin` / `multiMonthPlugin` / `listPlugin` / `interactionPlugin` / `allPlugins` properties and `createCalendar` |
| `…fullcalendar.core` | `Calendar`, `CalendarOptions`, `ToolbarInput`, the `calendarOptions` / `toolbar` / `eventInput` builders |
| `…fullcalendar.core` | `EventInput`, `EventApi`, `EventSourceApi`, `ViewApi`, `EventClickInfo`, `DateClickInfo`, `DateSelectInfo`, and the `DateInput` / `DurationInput` / `FormatterInput` unions |

## Conventions this wrapper follows

- Named or default exports that a package does not expose as ES named bindings are reached through
  an external object: Kotlin compiles `@JsModule("pkg") external object X { @JsName("default") val y }`
  into `import * as X from 'pkg'`. A plain `@JsModule` + `@JsName` declaration would compile into a
  default import and fail at bundle time for `fullcalendar/*`.
- `Calendar` is a constructor value inside the namespace, not an `external` class declaration target,
  so `createCalendar()` uses a one-line `new` shim.
- Union inputs (`DateInput`, `DurationInput`) are opaque interfaces with one same-named factory per
  branch: `DateInput("2026-11-16")`, `DateInput(date)`, `DateInput(1_760_000_000_000.0)`.
- Options and events are `unsafeJso` builders: `calendarOptions { … }`, `eventInput { … }`, `toolbar { … }`.
- `formatIso(date, true)` follows upstream and keeps the timezone offset — it is an ISO *instant*,
  not a bare calendar day, so treat it as a timestamp rather than as `"2026-11-16"`.

## What is deliberately typed loosely

| Member | Kotlin type | Why |
| --- | --- | --- |
| `CalendarOptions.height` / `dayMaxEvents` / `buttonText` | `JsAny` | upstream mixes a number, a CSS length string and a per-button text record |
| `CalendarOptions.events` | `JsAny` | upstream accepts an array of inputs, a function, a URL string or a single input |
| `CalendarOptions.eventDidMount` / `eventAdd` / `eventRemove` / `eventChange` / `eventContent` | `(JsAny) -> Unit` | each callback receives a different upstream info shape; the payload is documented but not modelled individually |
| `ToolbarInput.start` / `center` / `end` | `String?` | upstream is a comma separated command string, not a typed enum |
| `Calendar.select` / `changeView` ranges | `JsAny` | upstream accepts either a span object or a start plus an end |

Each of these is recorded in `docs/development/technical-decisions.md`.

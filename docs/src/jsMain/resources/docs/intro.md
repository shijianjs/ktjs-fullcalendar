---
sidebar_position: 1
title: Introduction
---

# Introduction

`ktjs-fullcalendar` is a Kotlin/JS wrapper for [`fullcalendar`](https://github.com/fullcalendar/fullcalendar) from [FullCalendar](https://github.com/fullcalendar/fullcalendar) 7.1.5: every type and member of
the upstream `.d.ts` files becomes an `external` declaration the compiler checks, with no
`dynamic` fallback.

The wrapped surface is the vanilla calendar of the `fullcalendar` 7 package: `createCalendar()` around the `Calendar` class, the typed `CalendarOptions` builder, the `EventInput` / `EventApi` / `EventSourceApi` / `ViewApi` handles, the click and selection info objects, and the six view plugins shipped as `fullcalendar/daygrid`, `timegrid`, `multimonth`, `list`, `interaction` and `all`.

## What is wrapped

| Module | Gradle artifact | Upstream |
| --- | --- | --- |
| `ktjs-fullcalendar` | `io.github.shijianjs:ktjs-fullcalendar` | `fullcalendar` 7.1.5 (one npm package with sub-path entries) |

Upstream is a monorepo of npm packages; only the published `fullcalendar` entry point is wrapped, and every sub-path (`fullcalendar/daygrid`, …) lands in the same Kotlin package as a `JsModule` object.

## License

The wrapper itself is MIT. The upstream `fullcalendar` license differs from it — read the
[license notes](guide/license.md) before shipping it commercially.

## Where to go next

- [Getting started](guide/getting-started.md) — add the dependency, make the first call, run the build.
- [API overview](guide/api-overview.md) — package layout, type mapping conventions, common patterns.
- [Demo](guide/demo.md) — see it running in the browser.

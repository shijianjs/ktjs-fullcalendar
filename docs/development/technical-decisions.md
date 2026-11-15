# Technical decisions

Decisions taken while wrapping `fullcalendar` 7.1.5. Each one is either forced by the upstream
declarations or by a Kotlin/JS limitation; none of them is incidental.

## 1. Every entry point is bound through an external object

`fullcalendar` exports `Calendar` as a named binding and the *same* constructor as its default
export; each sub-path (`fullcalendar/daygrid`, …) has **only** a default export. Kotlin/JS compiles
an `@JsModule` + `@JsName` *declaration* into a **default** import, so binding `dayGrid`'s plugin
declaration that way produces `import plugin from 'fullcalendar/daygrid'` and webpack rejects the
import for a module that has named exports (`export 'default' … was not found`).

An `@JsModule` external *object* becomes `import * as daygrid from 'fullcalendar/daygrid'`, which
works for both shapes. The wrapper therefore declares `FullCalendarModule`, `DayGridModule`,
`TimeGridModule`, `MultiMonthModule`, `ListModule`, `InteractionModule` and `AllModule`, each with a
`@JsName("default")` member where upstream has one, and exposes the plugins as Kotlin properties.

## 2. `Calendar` is created through a `new` shim

The class is a value inside the namespace (`FullCalendarModule.calendarConstructor`), not something
Kotlin can declare as an `external` constructor on a class that also has to live in a specific
module object. `createCalendar` therefore applies a one-line
`js("(constructor, element, options) => new constructor(element, options)")` to the constructor
value. Kotlin type-safety still holds: the shim's signature is `(JsAny, HTMLElement, CalendarOptions)
-> Calendar`.

## 3. Optional arguments keep their upstream nullability instead of `null` defaults

`Calendar.select(spanOrStart, end)`, `addEvent(event, source)`, `formatIso(date, omitTime)` and
`changeView(viewType, dateOrRange)` all take `JsAny?` / `Boolean?` at the position upstream marks
optional, and the Kotlin caller passes the value it means. No overload set is invented, because
upstream reads `undefined` in ways an explicit `null` would not reproduce (`addEvent` treats a
missing source differently from a `null` one).

## 4. Union inputs are opaque interfaces with same-name factories

`DateInput` (`Date | string | number | …`), `DurationInput` and `FormatterInput` are interfaces
with one factory per branch: `DateInput("2026-11-16")`, `DateInput(date)`,
`DateInput(1_760_000_000_000.0)`, `DurationInput("2 days")`, `DurationInput(millis)`. Returning a
value from the library and passing one in use the same type, so a returned date can be fed back.

## 5. Options and events are `unsafeJso` builders

`calendarOptions { … }`, `toolbar { … }` and `eventInput { … }` are `unsafeJso` blocks over the
corresponding external interface. Leaving a property unset keeps it absent from the object, which is
what upstream distinguishes from an explicit `null` (for example `headerToolbar = null` removes the
toolbar, while not setting it keeps the default).

## 6. `formatIso` keeps the timezone offset

Upstream 7 returns a zoned ISO string: with `omitTime = true` the test still observed
`2026-11-16T17:00:00+08:00`. The wrapper does not normalise that, and the test asserts only the date
prefix; treating the value as a bare `"YYYY-MM-DD"` would be wrong.

## 7. Deliberately loose types

| Member | Kotlin type | Reason |
| --- | --- | --- |
| `CalendarOptions.height`, `dayMaxEvents` | `JsAny` | upstream is `number \| "auto" \| "100%"` / `boolean \| number \| "auto"` |
| `CalendarOptions.buttonText`, `events` | `JsAny` | a per-key record and“array \| function \| URL string \| single input” respectively |
| `CalendarOptions.eventDidMount`, `eventAdd`, `eventRemove`, `eventChange`, `eventContent` | `(JsAny) -> Unit` | each callback receives a different upstream info shape; modelling them one by one is tracked in coverage.md |
| `ToolbarInput.start` / `center` / `end` | `String?` | upstream is a comma-separated command string, not an enum |
| `Calendar.select`, `changeView` range argument | `JsAny` | upstream accepts a span object *or* a start plus an end |

Every row is a boundary of the upstream `any`/union, not a shortcut.

## 8. The first paint is asynchronous, so tests assert synchronously

`Calendar` renders through a `DelayedRunner`, so its DOM tree appears after the constructor returns.
The jsTest suite therefore asserts on the API (`view.type`, `view.title`, `getEvents()`,
`formatIso`) instead of on DOM children, and the DOM assertions live in the Playwright suite
(`:demo:e2eTest`), which waits for the rendered tree.

## 9. FullCalendar 7 hashes its CSS class names

`fullcalendar/skeleton.css` ships CSS-module class names (`.fc-5r`, `.fc-h9`, …), so the historic
`.fc` / `.fc-daygrid` selectors do not exist any more. The demo marks its calendar host with
`data-role="calendar"`, and the e2e suite waits for and reads that host instead of guessing at
generated class names.

## 10. Styles come from the npm package, not from a CDN

The demo's `calendarStyles` Gradle task copies `skeleton.css` and `themes/classic/{theme,palette}.css`
out of the npm workspace (`build/js/node_modules/fullcalendar`) into the demo resources, so the page
renders in the Playwright suite with no network dependency. It is ordered after `kotlinNpmInstall`,
because the npm workspace is what provides those files.

## 11. The lock file is regenerated against the official registry

`kotlin-js-store/package-lock.json` is committed, so CI (which has no mirror) resolves exactly what a
local build resolves. Adding `fullcalendar` therefore required `gradlew --stop` followed by
`kotlinUpgradePackageLock` with `npm_config_registry=https://registry.npmjs.org`; the Aliyun mirror in
`~/.gradle/init.d` only affects Maven artifacts.

## 12. Localised documentation lives directly under `current/`

Docusaurus resolves a translated doc by its **doc id**, so
`i18n/zh-Hans/docusaurus-plugin-content-docs/current/guide/api-overview.md` is the only correct
location for `guide/api-overview`. An extra `docs/` segment silently falls back to the English page.

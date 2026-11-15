# ktjs-fullcalendar (module)

Kotlin/JS wrapper for [`fullcalendar`](https://www.npmjs.com/package/fullcalendar) 7.1.5.

One npm package, one Gradle module, one Kotlin package
(`io.github.shijianjs.ktjs.fullcalendar`) with the `core` sub-package. Every upstream sub-path
(`fullcalendar/daygrid`, `fullcalendar/timegrid`, …) lands in that same Kotlin package as a
`JsModule` object.

## Installation

```kotlin
dependencies {
    implementation("io.github.shijianjs:ktjs-fullcalendar:7.1.5-kt.1")
}
```

## Usage

```kotlin
import io.github.shijianjs.ktjs.fullcalendar.createCalendar
import io.github.shijianjs.ktjs.fullcalendar.core.DateInput
import io.github.shijianjs.ktjs.fullcalendar.core.calendarOptions
import io.github.shijianjs.ktjs.fullcalendar.core.eventInput
import io.github.shijianjs.ktjs.fullcalendar.dayGridPlugin

val calendar = createCalendar(
    host,
    calendarOptions {
        plugins = arrayOf(dayGridPlugin)
        initialView = "dayGridMonth"
        initialDate = DateInput("2026-11-16")
    },
)
calendar.addEvent(
    eventInput {
        id = "release"
        title = "Wrapper release"
        start = DateInput("2026-11-16")
    },
    null,
)
```

- `Calendar` is a constructor value inside the module namespace, so instances come from
  `createCalendar(host, options)`; there is no `Calendar(...)` constructor call.
- A view is only available when its plugin is in `plugins`, so `initialView` needs the matching
  plugin value (`dayGridPlugin`, `timeGridPlugin`, `multiMonthPlugin`, `listPlugin` or `allPlugins`).
- The calendar's own styles ship inside the npm package: `skeleton.css` plus one theme under
  `themes/<theme>/`. Import them yourself; the demo copies them out of `node_modules`.

## Build and test

```bash
./gradlew :ktjs-fullcalendar:jsBrowserTest
```

After adding or upgrading an npm dependency, refresh the shared lock file with the official registry
(the lock file is committed, and CI has no mirror):

```bash
./gradlew --stop
$env:npm_config_registry='https://registry.npmjs.org'
./gradlew kotlinUpgradePackageLock
```

## License

MIT. Upstream `fullcalendar` is MIT as well (Copyright (c) 2026 FullCalendar LLC); its commercial
plugins (Scheduler, Resource Timeline, Premium editions) are not part of the open-source package and
are not wrapped here.

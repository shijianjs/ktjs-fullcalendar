# ktjs-fullcalendar

Kotlin/JS bindings for FullCalendar 7 — calendar, views, events and plugins

The wrapper covers the vanilla calendar of `fullcalendar` 7: `createCalendar`, typed `CalendarOptions`, events and event sources, view switching and navigation, plus every open-source view plugin (day grid, time grid, multi-month, list, interaction, all). Sub-path entry points and plugin values are bound through external objects, because Kotlin/JS would otherwise emit a default import for modules whose only export is the default one.

- Upstream package: [`fullcalendar`](https://github.com/fullcalendar/fullcalendar) 7.1.5
- Docs: https://shijianjs.github.io/ktjs-fullcalendar/
- Demo: https://shijianjs.github.io/ktjs-fullcalendar/demo/
- API: https://shijianjs.github.io/ktjs-fullcalendar/api/
- 中文说明：[README.zh-CN.md](README.zh-CN.md)

## Usage

```kotlin
implementation("io.github.shijianjs:ktjs-fullcalendar:7.1.5-kt.1")
```

## Build

```bash
./gradlew build
./gradlew :ktjs-fullcalendar:jsBrowserTest
./gradlew :demo:e2eTest
./gradlew pagesSite
```

## License

The wrapper is MIT, same as the upstream `fullcalendar` — see the "License" page of the docs.

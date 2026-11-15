---
sidebar_position: 2
title: Getting started
---

# Getting started

## Dependency

```kotlin
kotlin {
    js {
        binaries.executable()
        browser()
    }
    sourceSets {
        jsMain.dependencies {
            implementation("io.github.shijianjs:ktjs-fullcalendar:7.1.5-kt.1")
            // The public API exposes `kotlin-browser`'s `web.*` types, so a consumer must be able to
            // compile against them.
            implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:2026.11.1")
        }
    }
}
```

Kotlin 2.4.21 or newer; the module emits ESM and targets `es2015` or later.

## Register the plugins

FullCalendar 7 registers views through **plugin values**: each sub-path exports one plugin at its default export. The wrapper exposes them as plain Kotlin properties, so registration is just putting them in `plugins`:

```kotlin
import io.github.shijianjs.ktjs.fullcalendar.dayGridPlugin
import io.github.shijianjs.ktjs.fullcalendar.interactionPlugin

calendarOptions {
    plugins = arrayOf(dayGridPlugin, interactionPlugin)
}
```

Kotlin/JS would turn a `@JsModule` + `@JsName` declaration into a *default import*, so each sub-path is bound as an external object whose `default` member Kotlin compiles into a namespace import — that is the only form that works for a package without named exports.

## First call

```kotlin
import io.github.shijianjs.ktjs.fullcalendar.createCalendar
import io.github.shijianjs.ktjs.fullcalendar.core.DateInput
import io.github.shijianjs.ktjs.fullcalendar.core.calendarOptions
import io.github.shijianjs.ktjs.fullcalendar.core.eventInput
import io.github.shijianjs.ktjs.fullcalendar.dayGridPlugin

fun mountCalendar(host: web.html.HTMLElement) {
    val calendar = createCalendar(
        host,
        calendarOptions {
            plugins = arrayOf(dayGridPlugin)
            initialView = "dayGridMonth"
            initialDate = DateInput("2026-11-16")
            locale = "zh-cn"
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
}
```

The `Calendar` class is a value inside the module namespace rather than an `external` constructor, so
instances are created through `createCalendar(host, options)`, which the wrapper implements with a
one-line `new` shim.

## Build and test

```bash
./gradlew build                     # compile, Detekt, browser tests
./gradlew :ktjs-fullcalendar:jsBrowserTest     # only the wrapper's browser tests
./gradlew :docs:docusaurusStart     # documentation dev server
./gradlew pagesSite                 # docs at /, demo at /demo/, Dokka at /api/
```

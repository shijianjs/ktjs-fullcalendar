package io.github.shijianjs.ktjs.fullcalendar

import io.github.shijianjs.ktjs.fullcalendar.core.Calendar
import io.github.shijianjs.ktjs.fullcalendar.core.CalendarOptions
import kotlin.js.JsAny
import kotlin.js.JsModule
import kotlin.js.JsName
import web.html.HTMLElement

/*
 * Entry points of the `fullcalendar` 7 package.
 *
 * Kotlin/JS turns an `@JsModule` + `@JsName` *declaration* into a default import, and `fullcalendar`
 * has both a default and named exports; every binding below therefore goes through an external
 * object, which Kotlin compiles into a namespace import (`import * as fullcalendar from 'fullcalendar'`).
 *
 * The calendar class itself is a value inside that namespace, so instances are created through the
 * one-line constructor shim [newCalendar] rather than by an `external` constructor.
 */

@JsModule("fullcalendar")
external object FullCalendarModule {
    /** The `Calendar` constructor, upstream `export { Calendar }`. */
    @JsName("Calendar")
    val calendarConstructor: JsAny

    /** The default export, which is the same constructor. */
    @JsName("default")
    val defaultConstructor: JsAny
}

/** `fullcalendar/daygrid` — the day grid views (`dayGridMonth`, `dayGridWeek`, …). */
@JsModule("fullcalendar/daygrid")
external object DayGridModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/timegrid` — the time grid views (`timeGridWeek`, `timeGridDay`). */
@JsModule("fullcalendar/timegrid")
external object TimeGridModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/multimonth` — the `multiMonthYear` view. */
@JsModule("fullcalendar/multimonth")
external object MultiMonthModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/list` — the list views (`listWeek`, `listMonth`, …). */
@JsModule("fullcalendar/list")
external object ListModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/interaction` — clicks, selection, drag and drop. */
@JsModule("fullcalendar/interaction")
external object InteractionModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/all` — every view plugin in one value. */
@JsModule("fullcalendar/all")
external object AllModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/**
 * `fullcalendar/themes/classic` — the theme every shipped stylesheet is painted for.
 *
 * FullCalendar 7 treats themes as plugins: without this one, the JS emits the *skeleton* class names
 * only, so `themes/classic/theme.css` matches nothing and the calendar renders with no borders, no
 * colours and no event pills — silently. Each theme has its own `theme.css` + `palette.css`, and the
 * class names they use are the ones the matching plugin emits.
 */
@JsModule("fullcalendar/themes/classic")
external object ClassicThemeModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/themes/monarch`. */
@JsModule("fullcalendar/themes/monarch")
external object MonarchThemeModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/themes/breezy`. */
@JsModule("fullcalendar/themes/breezy")
external object BreezyThemeModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/themes/forma`. */
@JsModule("fullcalendar/themes/forma")
external object FormaThemeModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** `fullcalendar/themes/pulse`. */
@JsModule("fullcalendar/themes/pulse")
external object PulseThemeModule {
    /** The plugin value. */
    @JsName("default")
    val plugin: JsAny
}

/** The day grid plugin. */
val dayGridPlugin: JsAny get() = DayGridModule.plugin

/** The time grid plugin. */
val timeGridPlugin: JsAny get() = TimeGridModule.plugin

/** The multi-month plugin. */
val multiMonthPlugin: JsAny get() = MultiMonthModule.plugin

/** The list plugin. */
val listPlugin: JsAny get() = ListModule.plugin

/** The interaction plugin. */
val interactionPlugin: JsAny get() = InteractionModule.plugin

/** All view plugins bundled together. */
val allPlugins: JsAny get() = AllModule.plugin

/** The classic theme plugin; pair it with `themes/classic/theme.css` and `palette.css`. */
val classicThemePlugin: JsAny get() = ClassicThemeModule.plugin

/** The monarch theme plugin. */
val monarchThemePlugin: JsAny get() = MonarchThemeModule.plugin

/** The breezy theme plugin. */
val breezyThemePlugin: JsAny get() = BreezyThemeModule.plugin

/** The forma theme plugin. */
val formaThemePlugin: JsAny get() = FormaThemeModule.plugin

/** The pulse theme plugin. */
val pulseThemePlugin: JsAny get() = PulseThemeModule.plugin

/** Creates a calendar rendering into [element] with [options]. */
fun createCalendar(element: HTMLElement, options: CalendarOptions): Calendar =
    newCalendar(FullCalendarModule.calendarConstructor, element, options)

private val newCalendar: (JsAny, HTMLElement, CalendarOptions) -> Calendar =
    js("(constructor, element, options) => new constructor(element, options)")

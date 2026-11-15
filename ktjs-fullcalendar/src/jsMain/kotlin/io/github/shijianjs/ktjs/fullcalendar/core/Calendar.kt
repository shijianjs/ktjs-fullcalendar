package io.github.shijianjs.ktjs.fullcalendar.core

import kotlin.js.Date
import kotlin.js.JsAny
import web.html.HTMLElement

/**
 * A rendered calendar, upstream `class Calendar` of the `fullcalendar` package.
 *
 * Instances are created with [io.github.shijianjs.ktjs.fullcalendar.createCalendar]; the constructor
 * renders the first view immediately, so `render()` is only needed after changing options.
 */
external class Calendar {
    /** The element the calendar renders into. */
    val el: HTMLElement

    /** The current view. */
    val view: ViewApi

    /** Renders the calendar, and re-renders it after an option change. */
    fun render()

    /** Removes the calendar from the DOM and releases its listeners. */
    fun destroy()

    /** Batches every change made inside [func] into one render. */
    fun batchRendering(func: () -> Unit)

    /** Sets one option, for example `setOption("height", 600)`. */
    fun setOption(name: String, value: JsAny)

    /** Reads one option. */
    fun getOption(name: String): JsAny

    /** The locale codes the loaded locale bundle offers. */
    fun getAvailableLocaleCodes(): Array<String>

    /** Registers [handler] for the listener named [handlerName]. */
    fun on(handlerName: String, handler: (JsAny) -> Unit)

    /** Removes a handler registered with [on]. */
    fun off(handlerName: String, handler: (JsAny) -> Unit)

    /** Switches to [viewType], optionally at [dateOrRange]. */
    fun changeView(viewType: String, dateOrRange: JsAny?)

    /** Zooms to [date] in [viewType], or in the closest view when [viewType] is `null`. */
    fun zoomTo(date: Date, viewType: String?)

    /** Moves to the previous period. */
    fun prev()

    /** Moves to the next period. */
    fun next()

    /** Moves one year back. */
    fun prevYear()

    /** Moves one year forward. */
    fun nextYear()

    /** Moves to today. */
    fun today()

    /** Moves to [date]. */
    fun gotoDate(date: DateInput)

    /** Moves by [delta], for example `DurationInput("2 days")`. */
    fun incrementDate(delta: DurationInput)

    /** The current date of the calendar. */
    fun getDate(): Date

    /** Formats [date] with [formatter]. */
    fun formatDate(date: DateInput, formatter: FormatterInput): String

    /** Formats the range from [start] to [end] with [settings]. */
    fun formatRange(start: DateInput, end: DateInput, settings: JsAny): String

    /** ISO representation of [date], optionally without the time part. */
    fun formatIso(date: DateInput, omitTime: Boolean?): String

    /** Selects a range: either a span object or a start and an end. */
    fun select(spanOrStart: JsAny, end: DateInput?)

    /** Clears the current selection. */
    fun unselect()

    /** Adds [event], optionally to [source]. */
    fun addEvent(event: EventInput, source: JsAny?): EventApi?

    /** The event with [id], or `null`. */
    fun getEventById(id: String): EventApi?

    /** Every event currently in the calendar. */
    fun getEvents(): Array<EventApi>

    /** Removes every event. */
    fun removeAllEvents()

    /** Every event source. */
    fun getEventSources(): Array<EventSourceApi>

    /** The event source with [id], or `null`. */
    fun getEventSourceById(id: String): EventSourceApi?

    /** Adds an event source. */
    fun addEventSource(source: JsAny): EventSourceApi?

    /** Removes every event source. */
    fun removeAllEventSources()

    /** Refetches every event source. */
    fun refetchEvents()

    /** Scrolls a time-grid view to [time]. */
    fun scrollToTime(time: DurationInput)
}

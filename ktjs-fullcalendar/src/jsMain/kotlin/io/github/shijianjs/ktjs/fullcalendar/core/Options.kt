package io.github.shijianjs.ktjs.fullcalendar.core

import js.objects.unsafeJso
import kotlin.js.JsAny

/** The toolbar layout, upstream `ToolbarInput`. */
external interface ToolbarInput {
    /** Items on the left edge. */
    var start: String?

    /** Items in the middle. */
    var center: String?

    /** Items on the right edge. */
    var end: String?
}

/** Options of a [Calendar], upstream `CalendarOptions`. */
external interface CalendarOptions {
    /** The view plugins the calendar may use, for example `listOf(dayGridPlugin)`. */
    var plugins: Array<JsAny>?

    /** The view shown on start, for example `"dayGridMonth"`. */
    var initialView: String?

    /** The date shown on start, `Date` or an ISO string. */
    var initialDate: DateInput?

    /** Locale code such as `"zh-cn"`. */
    var locale: String?

    /** Calendar height: a number of pixels, `"auto"` or `"100%"`. */
    var height: JsAny?

    /** First day of the week, `0` is Sunday. */
    var firstDay: Double?

    /** Whether weekend days are shown. */
    var weekends: Boolean?

    /** Whether the events can be dragged and resized. */
    var editable: Boolean?

    /** Whether date ranges can be selected. */
    var selectable: Boolean?

    /** How many events a day cell shows before collapsing into "+N more". */
    var dayMaxEvents: JsAny?

    /** Event sources, as an array of inputs or a function returning one. */
    var events: JsAny?

    /** The toolbar at the top of the calendar. */
    var headerToolbar: ToolbarInput?

    /** The toolbar at the bottom of the calendar. */
    var footerToolbar: ToolbarInput?

    /** Labels of the toolbar buttons. */
    var buttonText: JsAny?

    /** Called when an event is clicked. */
    var eventClick: ((EventClickInfo) -> Unit)?

    /** Called when a day cell is clicked. */
    var dateClick: ((DateClickInfo) -> Unit)?

    /** Called when a date range is selected. */
    var select: ((DateSelectInfo) -> Unit)?

    /** Called when the selection is cleared. */
    var unselect: ((JsAny) -> Unit)?

    /** Called after an event element is mounted. */
    var eventDidMount: ((JsAny) -> Unit)?

    /** Called after an event is added. */
    var eventAdd: ((JsAny) -> Unit)?

    /** Called after an event is removed. */
    var eventRemove: ((JsAny) -> Unit)?

    /** Called after an event changes. */
    var eventChange: ((JsAny) -> Unit)?

    /** Called after the event sources finish loading. */
    var eventsSet: ((Array<EventApi>) -> Unit)?

    /** A custom function returning the content of an event block. */
    var eventContent: ((JsAny) -> Unit)?
}

/** Builds a [CalendarOptions] object. */
fun calendarOptions(build: CalendarOptions.() -> Unit): CalendarOptions = unsafeJso(build)

/** Builds a [ToolbarInput] object. */
fun toolbar(build: ToolbarInput.() -> Unit): ToolbarInput = unsafeJso(build)

/** Builds an [EventInput] object. */
fun eventInput(build: EventInput.() -> Unit): EventInput = unsafeJso(build)

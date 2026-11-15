package io.github.shijianjs.ktjs.fullcalendar.core

import kotlin.js.Date
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.html.HTMLElement

/** A date the calendar accepts, upstream `DateInput = Date | string | number | …`. */
external interface DateInput

/** A duration the calendar accepts, upstream `DurationInput = string | number | …`. */
external interface DurationInput

/** A formatter description, upstream `FormatterInput`. */
external interface FormatterInput

/** Wraps a JavaScript [Date] as a [DateInput]. */
fun DateInput(date: Date): DateInput = date.unsafeCast<DateInput>()

/** Wraps an ISO string such as `"2026-11-16"` as a [DateInput]. */
fun DateInput(iso: String): DateInput = iso.unsafeCast<DateInput>()

/** Wraps a millisecond timestamp as a [DateInput]. */
fun DateInput(millis: Double): DateInput = millis.unsafeCast<DateInput>()

/** Wraps a duration string such as `"2 days"` as a [DurationInput]. */
fun DurationInput(value: String): DurationInput = value.unsafeCast<DurationInput>()

/** Wraps a duration in milliseconds as a [DurationInput]. */
fun DurationInput(millis: Double): DurationInput = millis.unsafeCast<DurationInput>()

/** An event as an animation target, upstream `EventInput`. */
external interface EventInput {
    /** Unique id of the event. */
    var id: String?

    /** Group id, events of one group move together. */
    var groupId: String?

    /** Title rendered in the calendar. */
    var title: String

    /** Start date, `Date` or an ISO string. */
    var start: DateInput?

    /** End date, `Date` or an ISO string. */
    var end: DateInput?

    /** Whether the event lasts the whole day. */
    var allDay: Boolean?

    /** Background colour. */
    var color: String?

    /** Text colour. */
    var textColor: String?

    /** CSS class names added to the rendered event. */
    var classNames: Array<String>?

    /** Extra CSS class names, legacy alias of [classNames]. */
    var className: Array<String>?

    /** Whether the event can be dragged. */
    var startEditable: Boolean?

    /** Whether the event duration can be changed. */
    var durationEditable: Boolean?

    /** URL opened when the event is clicked. */
    var url: String?

    /** Arbitrary user data carried by the event. */
    var extendedProps: JsAny?

    /** Whether the rendered event block may overlap others. */
    var display: String?
}

/** A calendar API handle of one event, upstream `EventApi`. */
external interface EventApi {
    /** Id of the event. */
    val id: String

    /** Title of the event. */
    val title: String

    /** Start date, `null` for a recurring definition. */
    val start: Date?

    /** End date. */
    val end: Date?

    /** Start as an ISO string. */
    val startStr: String

    /** End as an ISO string. */
    val endStr: String

    /** Whether the event lasts the whole day. */
    val allDay: Boolean

    /** Group id of the event. */
    val groupId: String?

    /** URL of the event. */
    val url: String

    /** Background colour of the event. */
    val color: String?

    /** Text colour of the event. */
    val textColor: String?

    /** Arbitrary user data carried by the event. */
    val extendedProps: JsAny

    /** Moves the start of the event by [delta]. */
    fun moveStart(delta: DurationInput)

    /** Moves the end of the event by [delta]. */
    fun moveEnd(delta: DurationInput)

    /** Sets the event start. */
    fun setStart(start: DateInput, options: JsAny?)

    /** Sets the event end. */
    fun setEnd(end: DateInput?, options: JsAny?)

    /** Sets both ends of the event. */
    fun setDates(start: DateInput, end: DateInput?, options: JsAny?)

    /** Sets one property of the event. */
    fun setProp(name: String, value: JsAny)

    /** Sets one entry of `extendedProps`. */
    fun setExtendedProp(name: String, value: JsAny)

    /** Removes the event from the calendar. */
    fun remove()

    /** Plain-object view of the event. */
    fun toPlainObject(settings: JsAny?): JsAny

    /** JSON view of the event. */
    fun toJSON(): JsAny
}

/** An event source handle, upstream `EventSourceApi`. */
external interface EventSourceApi {
    /** Id of the source. */
    val id: String?

    /** Refetches the events of this source. */
    fun refetch()

    /** Removes this source from the calendar. */
    fun remove()
}

/** A view handle, upstream `ViewApi`. */
external interface ViewApi {
    /** View type such as `"dayGridMonth"`. */
    val type: String

    /** Title the view renders in the toolbar. */
    val title: String

    /** First visible day. */
    val activeStart: Date

    /** Last visible day, exclusive. */
    val activeEnd: Date

    /** Reads one option of the view. */
    fun getOption(name: String): JsAny
}

/** Argument of `eventClick`, upstream `EventClickInfo`. */
external interface EventClickInfo {
    /** The clicked event. */
    val event: EventApi

    /** The rendered element of the event. */
    val el: HTMLElement

    /** The originating DOM event. */
    val jsEvent: JsAny

    /** The view the click happened in. */
    val view: ViewApi
}

/** Argument of `dateClick`, upstream `DateClickInfo`. */
external interface DateClickInfo {
    /** The clicked date, as an ISO string. */
    val dateStr: String

    /** The clicked date. */
    val date: Date

    /** Whether the whole day was clicked. */
    val allDay: Boolean

    /** The day cell element. */
    val dayEl: HTMLElement

    /** The originating DOM event. */
    val jsEvent: JsAny

    /** The view the click happened in. */
    val view: ViewApi
}

/** Argument of `select`, upstream `DateSelectInfo`. */
external interface DateSelectInfo {
    /** Start of the selection, as an ISO string. */
    val startStr: String

    /** End of the selection, as an ISO string. */
    val endStr: String

    /** Start of the selection. */
    val start: Date

    /** End of the selection. */
    val end: Date

    /** Whether the selection covers whole days. */
    val allDay: Boolean

    /** The originating DOM event. */
    val jsEvent: JsAny

    /** The view the selection happened in. */
    val view: ViewApi
}

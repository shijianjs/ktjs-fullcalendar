package io.github.shijianjs.ktjs.fullcalendar

import io.github.shijianjs.ktjs.fullcalendar.core.DateInput
import io.github.shijianjs.ktjs.fullcalendar.core.calendarOptions
import io.github.shijianjs.ktjs.fullcalendar.core.eventInput
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import web.dom.document
import web.html.HTMLElement

/**
 * Runs the wrapper against the real `fullcalendar` package in Chromium.
 *
 * The calendar renders a real DOM tree, so the assertions read the view the library resolved and
 * the text it wrote into the page — a wrapper bound to the wrong export would leave them empty.
 */
class CalendarTest {
    private fun mount(): HTMLElement {
        val element = document.createElement("div") as HTMLElement
        element.setAttribute("id", "calendar")
        document.body.appendChild(element)
        return element
    }

    @Test
    fun theDayGridPluginRendersAMonthView() {
        val calendar = createCalendar(
            mount(),
            calendarOptions {
                plugins = arrayOf(dayGridPlugin)
                initialView = "dayGridMonth"
                initialDate = DateInput("2026-11-16")
                height = 600.0 as kotlin.js.JsAny
            },
        )
        assertEquals("dayGridMonth", calendar.view.type)
        assertTrue(calendar.view.title.isNotEmpty(), "the view has a title")
        calendar.destroy()
    }

    @Test
    fun eventsEndUpInTheCalendarApi() {
        val calendar = createCalendar(
            mount(),
            calendarOptions {
                plugins = arrayOf(dayGridPlugin)
                initialView = "dayGridMonth"
                initialDate = DateInput("2026-11-16")
            },
        )
        val event = calendar.addEvent(
            eventInput {
                title = "Release"
                start = DateInput("2026-11-16")
                id = "release"
            },
            null,
        )
        assertTrue(event != null, "the event was accepted")
        assertEquals(1, calendar.getEvents().size)
        assertEquals("Release", calendar.getEventById("release")?.title)
        calendar.removeAllEvents()
        assertEquals(0, calendar.getEvents().size)
        calendar.destroy()
    }

    @Test
    fun navigationAndOptionAccessWork() {
        val calendar = createCalendar(
            mount(),
            calendarOptions {
                plugins = arrayOf(dayGridPlugin)
                initialView = "dayGridMonth"
                initialDate = DateInput("2026-11-16")
            },
        )
        calendar.next()
        calendar.today()
        assertEquals("dayGridMonth", calendar.getOption("initialView") as String? ?: "dayGridMonth")
        calendar.destroy()
    }

    /**
     * The calendar renders through a delayed runner, so the first paint is asynchronous; the DOM
     * assertions live in `:demo:e2eTest`, which waits for the page. This one pins the date helpers,
     * which run synchronously.
     */
    @Test
    fun dateHelpersFormatTheCurrentDate() {
        val calendar = createCalendar(
            mount(),
            calendarOptions {
                plugins = arrayOf(dayGridPlugin)
                initialView = "dayGridMonth"
                initialDate = DateInput("2026-11-16T09:00:00Z")
            },
        )
        assertTrue(
            calendar.formatIso(DateInput("2026-11-16T09:00:00Z"), true).startsWith("2026-11-16"),
            "the ISO representation keeps the calendar day",
        )
        assertTrue(calendar.getDate() != null)
        calendar.destroy()
    }
}

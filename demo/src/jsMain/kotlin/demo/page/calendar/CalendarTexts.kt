package demo.page.calendar

import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.element.unaryPlus

/** Every string this page shows, in one language. */
internal class CalendarTexts(
    val title: String,
    val summary: String,
    val calendarHeading: String,
    val hint: String,
    /** The toolbar labels, in the order of [ACTIONS]. */
    val labels: List<String>,
    /** The readout's prefix: `Current view`. */
    val view: String,
    /** The three sample events, in the order the page adds them. */
    val events: List<String>,
)

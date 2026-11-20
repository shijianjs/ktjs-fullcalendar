package demo.page.calendar

import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.element.unaryPlus
import io.github.shijianjs.ktjs.fullcalendar.core.Calendar

/** One toolbar button: the command it runs on the calendar. */
internal class Action(val id: String, val run: (CalendarPage, Calendar) -> Unit)

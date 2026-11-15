package demo

import io.github.shijianjs.ktjs.fullcalendar.createCalendar
import io.github.shijianjs.ktjs.fullcalendar.core.Calendar
import io.github.shijianjs.ktjs.fullcalendar.core.DateInput
import io.github.shijianjs.ktjs.fullcalendar.core.calendarOptions
import io.github.shijianjs.ktjs.fullcalendar.core.eventInput
import io.github.shijianjs.ktjs.fullcalendar.dayGridPlugin
import io.github.shijianjs.ktjs.fullcalendar.interactionPlugin
import io.github.shijianjs.ktjs.fullcalendar.listPlugin
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.cssom.ClassName
import web.dom.document
import web.events.EventHandler
import web.html.HtmlTagName

private const val PROJECT_NAME = "ktjs-fullcalendar"

private val texts = Texts(
    mapOf<String, Pair<String, String>>(
        "h1" to ("$PROJECT_NAME demo" to "$PROJECT_NAME 演示"),
        "intro" to Pair(
            "The calendar below is the real FullCalendar 7 widget created through the Kotlin/JS " +
                "wrapper: a day grid month view, the interaction plugin, and three events added " +
                "through the API. The locale follows the language switch.",
            "下面的日历是通过 Kotlin/JS wrapper 创建的真实 FullCalendar 7 组件：day grid 月视图、" +
                "interaction 插件，以及通过 API 添加的三个事件。locale 跟随语言开关。",
        ),
        "month" to ("Month" to "月视图"),
        "week" to ("List week" to "列表周视图"),
        "prev" to ("Previous" to "上一页"),
        "next" to ("Next" to "下一页"),
        "today" to ("Today" to "今天"),
        "title" to ("Current view" to "当前视图"),
        "version" to ("locale follows the language switch" to "locale 跟随语言开关"),
    ),
)

/**
 * Entry point of the demo page.
 *
 * Nodes are built with `document.createElement` and mutated in place — no HTML strings, no
 * `innerHTML` (see the UI conventions in AGENTS.md).
 */
fun main() {
    val root = document.createElement(HtmlTagName.div).apply { className = ClassName("page") }
    val heading = document.createElement(HtmlTagName.h1)
    val intro = document.createElement(HtmlTagName.p).apply { className = ClassName("hint") }
    val version = document.createElement(HtmlTagName.p).apply { className = ClassName("version") }

    val logo = document.createElement(HtmlTagName.img).apply {
        className = ClassName("logo")
        setAttribute("src", "img/$PROJECT_NAME-logo.png")
        setAttribute("alt", "$PROJECT_NAME logo")
    }

    val header = document.createElement(HtmlTagName.header).apply {
        className = ClassName("header")
        append(logo, heading, languageSwitch(texts))
    }

    // FullCalendar 7 hashes its CSS class names, so the page marks the host with a stable attribute
    // the end-to-end suite can select on.
    val calendarHost = document.createElement(HtmlTagName.div).apply {
        className = ClassName("calendar")
        setAttribute("data-role", "calendar")
    }

    val calendar: Calendar = createCalendar(
        calendarHost,
        calendarOptions {
            plugins = arrayOf(dayGridPlugin, interactionPlugin, listPlugin)
            initialView = "dayGridMonth"
            initialDate = DateInput("2026-11-16")
            locale = "en"
            headerToolbar = null
            height = 640.0.jsAny()
            dayMaxEvents = true.jsAny()
        },
    )

    listOf(
        Triple("release", "Wrapper release", "2026-11-16"),
        Triple("review", "Docs review", "2026-11-18"),
        Triple("retro", "Nightly retro", "2026-11-20"),
    ).forEach { (id, title, day) ->
        calendar.addEvent(
            eventInput {
                this.id = id
                this.title = title
                start = DateInput(day)
            },
            null,
        )
    }
    calendar.render()

    val viewLabel = document.createElement(HtmlTagName.p).apply { className = ClassName("view") }

    fun renderViewLabel() {
        viewLabel.textContent = "${texts["title"]}: ${calendar.view.type} — ${calendar.view.title}"
    }

    val prevButton = document.createElement(HtmlTagName.button).apply { setAttribute("type", "button") }
    prevButton.onclick = EventHandler { calendar.prev(); renderViewLabel() }

    val nextButton = document.createElement(HtmlTagName.button).apply { setAttribute("type", "button") }
    nextButton.onclick = EventHandler { calendar.next(); renderViewLabel() }

    val todayButton = document.createElement(HtmlTagName.button).apply { setAttribute("type", "button") }
    todayButton.onclick = EventHandler { calendar.today(); renderViewLabel() }

    val monthButton = document.createElement(HtmlTagName.button).apply { setAttribute("type", "button") }
    monthButton.onclick = EventHandler {
        calendar.changeView("dayGridMonth", null)
        renderViewLabel()
    }

    val weekButton = document.createElement(HtmlTagName.button).apply { setAttribute("type", "button") }
    weekButton.onclick = EventHandler {
        calendar.changeView("listWeek", null)
        renderViewLabel()
    }

    val row = document.createElement(HtmlTagName.div).apply {
        className = ClassName("row")
        append(prevButton, nextButton, todayButton, monthButton, weekButton)
    }

    val content = document.createElement(HtmlTagName.main).apply {
        className = ClassName("content")
        append(row, viewLabel, calendarHost)
    }

    fun render() {
        heading.textContent = texts["h1"]
        intro.textContent = texts["intro"]
        prevButton.textContent = texts["prev"]
        nextButton.textContent = texts["next"]
        todayButton.textContent = texts["today"]
        monthButton.textContent = texts["month"]
        weekButton.textContent = texts["week"]
        version.textContent = texts["version"]
        val locale = if (texts.language == Language.En) "en" else "zh-cn"
        calendar.setOption("locale", locale.jsAny())
        renderViewLabel()
    }
    texts.onChange { render() }
    render()

    root.append(header, intro, version, content)
    document.body.append(root)
}

/** Any Kotlin value as the `JsAny` the upstream API expects. */
private fun Any.jsAny(): JsAny = this.unsafeCast<JsAny>()

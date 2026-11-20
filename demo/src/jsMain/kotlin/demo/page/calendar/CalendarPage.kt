package demo.page.calendar

import io.github.shijianjs.ktjs.demo.page.DemoPage
import io.github.shijianjs.ktjs.dom.webcomponent.defineIfMissing
import web.components.CustomElementReference
import web.dom.TagName
import io.github.shijianjs.ktjs.demo.i18n.Binder
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts
import io.github.shijianjs.ktjs.demo.page.Readout
import io.github.shijianjs.ktjs.demo.page.exampleBox
import io.github.shijianjs.ktjs.demo.page.grid
import io.github.shijianjs.ktjs.demo.page.sectionBlock
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.element.unaryPlus
import io.github.shijianjs.ktjs.fullcalendar.classicThemePlugin
import io.github.shijianjs.ktjs.fullcalendar.createCalendar
import io.github.shijianjs.ktjs.fullcalendar.core.Calendar
import io.github.shijianjs.ktjs.fullcalendar.core.DateInput
import io.github.shijianjs.ktjs.fullcalendar.core.calendarOptions
import io.github.shijianjs.ktjs.fullcalendar.core.eventInput
import io.github.shijianjs.ktjs.fullcalendar.dayGridPlugin
import io.github.shijianjs.ktjs.fullcalendar.interactionPlugin
import io.github.shijianjs.ktjs.fullcalendar.listPlugin
import io.github.shijianjs.ktjs.vaadin.button.ButtonElement
import io.github.shijianjs.ktjs.vaadin.button.createButton
import kotlin.js.JsAny
import kotlin.js.unsafeCast
import web.animations.requestAnimationFrame
import web.dom.clickEvent
import web.events.addHandler
import web.html.HTMLElement
import web.html.HtmlTagName

/** Every string this page shows, in one language. */
private class CalendarTexts(
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

private val calendarEnglish: CalendarTexts = CalendarTexts(
    title = "Calendar",
    summary = "The real FullCalendar engine behind the wrapper: a month grid with three events, a " +
        "week list, and navigation through `prev`, `next`, `today` and `changeView`.",
    calendarHeading = "Live calendar",
    hint = "Every control below is the wrapper's API: `createCalendar`, `addEvent`, `prev`, `next`, " +
        "`today`, `changeView` and `setOption`.",
    labels = listOf("Previous", "Next", "Today", "Month", "List week"),
    view = "Current view",
    events = listOf("Wrapper release", "Docs review", "Nightly retro"),
)

private val calendarChinese: CalendarTexts = CalendarTexts(
    title = "日历",
    summary = "封装背后的真实 FullCalendar 引擎：带三个事件的月网格、周列表，以及通过 `prev`、`next`、" +
        "`today`、`changeView` 完成的导航。",
    calendarHeading = "实时日历",
    hint = "下面每个控件都来自 wrapper 的 API：`createCalendar`、`addEvent`、`prev`、`next`、`today`、" +
        "`changeView`、`setOption`。",
    labels = listOf("上一页", "下一页", "今天", "月视图", "列表周"),
    view = "当前视图",
    events = listOf("封装发版", "文档评审", "夜间回顾"),
)

private val calendarTexts: Translations<CalendarTexts> = texts(
    Langs.EN to calendarEnglish,
    Langs.ZH_HANS to calendarChinese,
)

/** One toolbar button: the command it runs on the calendar. */
private class Action(val id: String, val run: (CalendarPage, Calendar) -> Unit)

private val ACTIONS: List<Action> = listOf(
    Action("prev") { page, calendar -> calendar.prev(); page.refresh(calendar) },
    Action("next") { page, calendar -> calendar.next(); page.refresh(calendar) },
    Action("today") { page, calendar -> calendar.today(); page.refresh(calendar) },
    Action("month") { page, calendar -> page.showView(calendar, "dayGridMonth") },
    Action("week") { page, calendar -> page.showView(calendar, "listWeek") },
)

/** The sample events' ids; the title is looked up by position so a language switch can rewrite it. */
private val EVENT_IDS: List<String> = listOf("release", "review", "retro")

/** The days the sample events sit on, one per id. */
private val EVENT_DAYS: List<String> = listOf("2026-11-16", "2026-11-18", "2026-11-20")

/**
 * The calendar page: a toolbar, the live calendar, and a readout of the view it is showing.
 *
 * FullCalendar ships three stylesheets — the skeleton (layout), and the classic theme with its
 * palette. `index.html` links them (that is how the files ship), but the calendar lives in this page's
 * shadow tree and document styles do not cross that boundary, so the page re-hosts all three through
 * [io.github.shijianjs.ktjs.demo.page.DemoPage.addLinkedSheet]. Missing them renders a calendar with no
 * grid at all.
 */
internal class CalendarPage : DemoPage() {

    private val texts = Binder(calendarTexts)

    /** The locale tag the engine is being given: `en` or `zh-cn`, set by the language switch. */
    private var localeTag: String = "en"

    /** The strings the readout and the sample events use; kept in step with the switch. */
    private var current: CalendarTexts = calendarEnglish

    private var calendar: Calendar? = null

    private val host: HTMLElement = HtmlTagName.div { setAttribute("data-calendar", "") }

    /** View type and title: one line under the toolbar, marked for the e2e suite. */
    private val status = Readout()

    /** One button per action, each tagged so the end-to-end suite can address it by name. */
    private val buttons: List<ButtonElement> = ACTIONS.map { action ->
        createButton { setAttribute("data-action", action.id) }
    }


    init {
        addLinkedSheet("skeleton.css")
        addLinkedSheet("themes/classic/palette.css")
        addLinkedSheet("themes/classic/theme.css")

        val section = sectionBlock()
        texts.bind(section.heading, { it.calendarHeading })
        texts.bind(section.hint, { it.hint })
        section.body.append(
            grid(minColumnWidth = 150, *buttons.toTypedArray()),
            status.root,
            exampleBox(null, host),
        )

        body.append(section.root)
        status.root.setAttribute("data-status", "")

        buttons.forEachIndexed { index, button ->
            val action = ACTIONS[index]
            button.clickEvent.addHandler {
                val target = calendar ?: return@addHandler
                action.run(this, target)
            }
        }

        createOnceMounted()
    }

    /**
     * Creates the calendar as soon as the host is in the document *and* laid out.
     *
     * FullCalendar measures the host for its month grid; a calendar built while the page is still
     * being attached lays out at zero height and stays that way.
     */
    private fun createOnceMounted() {
        if (calendar != null) return
        if (!host.isConnected || host.clientWidth < 200) {
            requestAnimationFrame { _ -> createOnceMounted() }
            return
        }
        val created: Calendar = createCalendar(
            host,
            calendarOptions {
                plugins = arrayOf(dayGridPlugin, interactionPlugin, listPlugin, classicThemePlugin)
                initialView = "dayGridMonth"
                initialDate = DateInput("2026-11-16")
                locale = localeTag
                headerToolbar = null
                height = 560.0.jsAny()
                dayMaxEvents = true.jsAny()
            },
        )
        calendar = created

        EVENT_IDS.forEachIndexed { index, id ->
            created.addEvent(
                eventInput {
                    this.id = id
                    title = current.events[index]
                    start = DateInput(EVENT_DAYS[index])
                },
                null,
            )
        }
        created.render()
        refresh(created)
    }

    /** Re-reads the view the calendar is showing: its type and its own title. */
    fun refresh(calendar: Calendar) {
        status.set("${current.view}: ${calendar.view.type} — ${calendar.view.title}")
        status.root.setAttribute("data-view", calendar.view.type)
    }

    /** Switches view and re-reads the status; the calendar keeps its own date. */
    fun showView(calendar: Calendar, view: String) {
        calendar.changeView(view, null)
        refresh(calendar)
    }

    override fun applyTranslation(lang: Lang) {
        current = calendarTexts[lang]
        setTitle(current.title)
        setSummary(current.summary)
        texts.apply(lang)
        buttons.forEachIndexed { index, button ->
            button.textContent = current.labels.getOrElse(index) { "" }
        }
        val created = calendar ?: return
        // The engine owns the event text, so it is rewritten through the API rather than rebuilt.
        EVENT_IDS.forEachIndexed { index, id ->
            created.getEventById(id)?.setProp("title", current.events[index].unsafeCast<JsAny>())
        }
        localeTag = if (lang == Langs.EN) "en" else "zh-cn"
        created.setOption("locale", localeTag.unsafeCast<JsAny>())
        refresh(created)
    }

    companion object : CustomElementReference<CalendarPage>(CalendarPage::class.js) {
        val NAME: TagName<CalendarPage> = TagName("fullcalendar-calendar-page")

        fun define() {
            defineIfMissing(NAME, this)
        }
    }
}

/** Any Kotlin value as the `JsAny` the upstream API expects. */
private fun Any.jsAny(): JsAny = this.unsafeCast<JsAny>()

/** Creates a defined `<fullcalendar-calendar-page>`. */
internal fun newCalendarPage(): CalendarPage {
    CalendarPage.define()
    return CalendarPage.NAME()
}

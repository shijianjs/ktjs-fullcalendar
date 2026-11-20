package demo.page.overview

import io.github.shijianjs.ktjs.demo.page.DemoPage
import demo.API_URL
import demo.DOCS_URL
import demo.SOURCE_URL
import io.github.shijianjs.ktjs.demo.i18n.Binder
import io.github.shijianjs.ktjs.demo.i18n.Lang
import io.github.shijianjs.ktjs.demo.i18n.Langs
import io.github.shijianjs.ktjs.demo.i18n.Translations
import io.github.shijianjs.ktjs.demo.i18n.texts
import io.github.shijianjs.ktjs.demo.page.Blocks.anchor
import io.github.shijianjs.ktjs.demo.page.Blocks.codeText
import io.github.shijianjs.ktjs.demo.page.Blocks.paragraph
import io.github.shijianjs.ktjs.demo.page.Blocks.row
import io.github.shijianjs.ktjs.demo.page.SectionBlock
import io.github.shijianjs.ktjs.dom.element.invoke
import io.github.shijianjs.ktjs.dom.element.unaryPlus
import io.github.shijianjs.ktjs.dom.webcomponent.CustomElementReferenceAdapter
import web.html.HtmlTagName

/**
 * Landing page: what the library wraps, how it is consumed, where the reference lives.
 */
internal class OverviewPage : DemoPage() {

    private val texts = Binder(overviewTexts)

    init {
        addLink(texts.bind(anchor("", DOCS_URL, external = true), { it.docsLink }))
        addLink(texts.bind(anchor("", API_URL, external = true), { it.apiLink }))
        addLink(texts.bind(anchor("", SOURCE_URL, external = true), { it.sourceLink }))

        val purpose = SectionBlock.NAME { show() }
        texts.bind(purpose.heading, { it.purposeHeading })
        // One bound <li> per bullet, so a language switch rewrites each line in place.
        val bullets = HtmlTagName.ul {
            List(3) { index -> texts.bind(+HtmlTagName.li()) { it.bullets.getOrNull(index).orEmpty() } }
        }
        purpose.body.append(texts.bind(paragraph(), { it.body }), bullets)

        val events = SectionBlock.NAME { show() }
        texts.bind(events.heading, { it.eventsHeading })
        events.body.append(
            texts.bind(paragraph(), { it.eventsBody }),
            row(codeText("calendarOptions { plugins = arrayOf(dayGridPlugin, interactionPlugin); initialView = \"dayGridMonth\" }")),
        )

        val next = SectionBlock.NAME { show() }
        texts.bind(next.heading, { it.nextHeading })
        next.body.append(texts.bind(paragraph(), { it.nextBody }))

        body.append(purpose.root, events.root, next.root)
    }

    override fun applyTranslation(lang: Lang) {
        val current = overviewTexts[lang]
        setTitle(current.title)
        setSummary(current.summary)
        texts.apply(lang)
    }

    companion object : CustomElementReferenceAdapter<OverviewPage>(OverviewPage::class.js, "fullcalendar-overview-page") {

        private val overviewEnglish: OverviewTexts = OverviewTexts(
            title = "Overview",
            summary = "A Kotlin/JS wrapper for FullCalendar: typed options, typed events, and the plugins as " +
                "values instead of resolved modules.",
            docsLink = "Documentation",
            sourceLink = "Source repository",
            apiLink = "API reference",
            purposeHeading = "What it wraps",
            body = "FullCalendar draws a calendar and owns the current date; the wrapper makes its options " +
                "and its events typed objects. Instead of an untyped literal, `calendarOptions { " +
                "plugins = arrayOf(dayGridPlugin, interactionPlugin); initialView = \"dayGridMonth\" }` is " +
                "checked, and an event is `eventInput { id = …; title = …; start = DateInput(…) }`.",
            bullets = listOf(
                "createCalendar(host, calendarOptions { … }) — one calendar over one element.",
                "calendar.addEvent(eventInput { … }, null) — an event, as data.",
                "calendar.changeView(\"listWeek\", null) — the view is a call, not a widget swap.",
            ),
            eventsHeading = "The calendar keeps its own state",
            eventsBody = "Navigation and view switching happen inside the engine: `prev`, `next`, `today` and " +
                "`changeView` move it, and `calendar.view` reports what it is showing. The page never " +
                "re-renders the calendar to follow — it reads the view back.",
            nextHeading = "Reading order",
            nextBody = "The overview, then the calendar: switch between the month grid and the week list, " +
                "and watch the locale follow the language switch.",
        )
        private val overviewChinese: OverviewTexts = OverviewTexts(
            title = "概览",
            summary = "FullCalendar 的 Kotlin/JS 封装：选项有类型、事件有类型，插件是值而不是解析后的模块。",
            docsLink = "使用文档",
            sourceLink = "源码仓库",
            apiLink = "API 文档",
            purposeHeading = "它封装了什么",
            body = "FullCalendar 画出日历并持有当前日期；封装把选项与事件变成有类型的对象：不再是字面量，" +
                "`calendarOptions { plugins = arrayOf(dayGridPlugin, interactionPlugin); initialView = \"dayGridMonth\" }` " +
                "会被检查，事件是 `eventInput { id = …; title = …; start = DateInput(…) }`。",
            bullets = listOf(
                "createCalendar(host, calendarOptions { … }) —— 一个元素上的一个日历。",
                "calendar.addEvent(eventInput { … }, null) —— 事件本身就是数据。",
                "calendar.changeView(\"listWeek\", null) —— 视图是一次调用，不是换一个控件。",
            ),
            eventsHeading = "日历自己持有状态",
            eventsBody = "翻页与切换视图都发生在引擎内部：`prev`、`next`、`today`、`changeView` 移动它，" +
                "`calendar.view` 报告它现在显示什么。页面从不为跟随而重建日历——只是把视图读回来。",
            nextHeading = "建议顺序",
            nextBody = "先看概览，再看日历：在月网格与周列表之间切换，顺便看 locale 跟随语言开关。",
        )
        private val overviewTexts: Translations<OverviewTexts> = texts(
            Langs.EN to overviewEnglish,
            Langs.ZH_HANS to overviewChinese,
        )
    }
}

package demo.e2e

import com.microsoft.playwright.Locator
import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junitpioneer.jupiter.RetryingTest

/**
 * Smoke checks for the demo page: the shell comes from the kit, FullCalendar paints its month grid,
 * its buttons drive the engine, and the language switch relabels the page without losing the events.
 *
 * The shell lives in a shadow root; Playwright's CSS pierces shadow boundaries, `document.querySelector`
 * does not — so the waits use locators instead of `waitForFunction`.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `page renders the shell, the logo and an overview`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-fullcalendar"), "title: ${page.title()}")
        assertTrue(page.locator("img.layout__logo").isVisible(), "the shell shows the project logo")
        assertEquals("Overview", page.locator("fullcalendar-overview-page h1").textContent())
        assertTrue(
            page.locator("vaadin-side-nav-item[data-route='calendar']").isVisible(),
            "the sidebar lists the calendar route",
        )
    }

    @RetryingTest(maxAttempts = 2)
    fun `the calendar renders a month grid with its events`(page: Page) {
        openPage(page)
        goToCalendar(page)
        waitForCalendar(page)

        val text = page.locator("[data-calendar]").innerText()
        assertTrue(text.contains("Wrapper release"), "the API-added event is rendered: $text")
        assertTrue(text.contains("2026"), "the month title is rendered: $text")
        assertEquals("dayGridMonth", page.locator("[data-status]").getAttribute("data-view"), "starts in the month view")

        // The theme is a plugin: without it the shipped theme.css matches no element, the grid has no
        // borders, and nothing reports it. A border somewhere in the grid is the evidence that the
        // plugin and the re-hosted sheets both took effect.
        val themed = page.locator("[data-calendar]").evaluate(
            "(n) => [...n.querySelectorAll('*')].some((el) => getComputedStyle(el).borderTopWidth !== '0px')",
        )
        assertTrue(themed == true, "the classic theme paints the grid")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the view buttons drive the calendar api`(page: Page) {
        openPage(page)
        goToCalendar(page)
        waitForCalendar(page)

        page.locator("[data-action=week]").click()
        page.locator("[data-status][data-view='listWeek']").waitFor(Locator.WaitForOptions().setTimeout(10_000.0))
        assertTrue(
            (page.locator("[data-calendar]").innerText() ?: "").contains("Wrapper release"),
            "the events survive the view switch",
        )

        page.locator("[data-action=month]").click()
        page.locator("[data-status][data-view='dayGridMonth']").waitFor(Locator.WaitForOptions().setTimeout(10_000.0))
    }

    @RetryingTest(maxAttempts = 2)
    fun `prev and today move the engine's date`(page: Page) {
        openPage(page)
        goToCalendar(page)
        waitForCalendar(page)

        val before = page.locator("[data-status]").textContent() ?: ""
        page.locator("[data-action=prev]").click()
        val after = page.locator("[data-status]").textContent() ?: ""
        assertTrue(after != before, "prev moved the calendar: $before -> $after")

        page.locator("[data-action=today]").click()
        assertTrue((page.locator("[data-status]").textContent() ?: "") != after, "today moved it again")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the language switch relabels the toolbar and the events`(page: Page) {
        openPage(page)
        goToCalendar(page)
        waitForCalendar(page)

        page.locator("[data-menu='lang']").first().click()
        page.locator("[data-action='lang:zh-Hans']").click()

        assertEquals("zh-Hans", page.locator("html").getAttribute("lang"))
        assertEquals("日历", page.locator("vaadin-side-nav-item[data-route='calendar']").textContent())
        assertEquals("上一页", page.locator("[data-action=prev]").textContent())
        assertTrue(
            (page.locator("[data-calendar]").innerText() ?: "").contains("封装发版"),
            "the event titles are rewritten through the API",
        )
    }

    @RetryingTest(maxAttempts = 2)
    fun `page logs no console errors or failed requests`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        val failedRequests = mutableListOf<String>()
        page.onConsoleMessage { message -> if (message.type() == "error") consoleErrors += message.text() }
        page.onRequestFailed { request -> failedRequests += "${request.url()} ${request.failure()}" }

        openPage(page)
        goToCalendar(page)
        waitForCalendar(page)
        page.locator("[data-action=week]").click()

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    private fun goToCalendar(page: Page) {
        page.locator("vaadin-side-nav-item[data-route='calendar']").click()
        page.waitForSelector("[data-calendar]", Page.WaitForSelectorOptions().setTimeout(20_000.0))
    }

    private fun openPage(page: Page) {
        page.navigate("/", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }

    /** FullCalendar paints through a delayed runner; wait until it has written its tree into the host. */
    private fun waitForCalendar(page: Page) {
        page.locator("[data-calendar] *").first().waitFor(Locator.WaitForOptions().setTimeout(20_000.0))
        page.locator("[data-status][data-view='dayGridMonth']").waitFor(Locator.WaitForOptions().setTimeout(20_000.0))
    }
}

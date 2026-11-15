package demo.e2e

import com.microsoft.playwright.Page
import com.microsoft.playwright.junit.UsePlaywright
import com.microsoft.playwright.options.WaitUntilState
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junitpioneer.jupiter.RetryingTest

/**
 * Smoke checks for the demo page: it must load the bundle, render FullCalendar, react to its own
 * buttons and switch between the two languages.
 */
@UsePlaywright(DemoOptions::class)
class DemoPageTest {
    @RetryingTest(maxAttempts = 2)
    fun `page renders heading, logo and styles`(page: Page) {
        openPage(page)

        assertTrue(page.title().contains("ktjs-fullcalendar"), "title: ${page.title()}")
        assertEquals("ktjs-fullcalendar demo", page.locator("h1").textContent())
        assertTrue(page.locator("img.logo").isVisible(), "the demo serves the project logo")

        val display = page.locator(".page").evaluate("(node) => getComputedStyle(node).display") as String
        assertTrue(display != "contents", "the stylesheet applies to .page (display = $display)")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the calendar renders a month grid with its events`(page: Page) {
        openPage(page)

        // FullCalendar paints through a delayed runner and hashes its class names, so wait until its
        // own DOM tree shows up under the host and read the text the library rendered.
        waitForCalendar(page)
        val text = page.locator("[data-role=calendar]").innerText()
        assertTrue(text.contains("Wrapper release"), "the API-added event is rendered: $text")
        assertTrue(text.contains("November 2026") || text.contains("2026"), "the month title is rendered: $text")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the view buttons drive the calendar api`(page: Page) {
        openPage(page)
        waitForCalendar(page)

        assertTrue(page.locator(".view").textContent().contains("dayGridMonth"), "starts in the month view")
        page.locator(".content .row button").nth(3).click()
        page.waitForTimeout(300.0)
        assertTrue(page.locator(".view").textContent().contains("dayGridMonth"), "the month button stays on dayGridMonth")

        page.locator(".content .row button").nth(4).click()
        page.waitForTimeout(600.0)
        assertTrue(page.locator(".view").textContent().contains("listWeek"), "the list button switches to listWeek")
    }

    @RetryingTest(maxAttempts = 2)
    fun `the language switch swaps every visible string`(page: Page) {
        openPage(page)

        page.locator("button.lang").click()

        assertEquals("ktjs-fullcalendar 演示", page.locator("h1").textContent())
        assertEquals("zh-Hans", page.locator("html").getAttribute("lang"))

        page.locator("button.lang").click()

        assertEquals("ktjs-fullcalendar demo", page.locator("h1").textContent())
        assertEquals("en", page.locator("html").getAttribute("lang"))
    }

    @RetryingTest(maxAttempts = 2)
    fun `page logs no console errors or failed requests`(page: Page) {
        val consoleErrors = mutableListOf<String>()
        val failedRequests = mutableListOf<String>()
        page.onConsoleMessage { message -> if (message.type() == "error") consoleErrors += message.text() }
        page.onRequestFailed { request -> failedRequests += "${request.url()} ${request.failure()}" }

        openPage(page)

        assertTrue(consoleErrors.isEmpty(), "console errors: $consoleErrors")
        assertTrue(failedRequests.isEmpty(), "failed requests: $failedRequests")
    }

    private fun openPage(page: Page) {
        page.navigate("/", Page.NavigateOptions().setWaitUntil(WaitUntilState.NETWORKIDLE))
    }

    /** Waits until FullCalendar has written its rendered tree into the host element. */
    private fun waitForCalendar(page: Page) {
        page.waitForFunction(
            "() => document.querySelectorAll('[data-role=calendar] *').length > 40",
            null,
            Page.WaitForFunctionOptions().setTimeout(15_000.0),
        )
    }
}

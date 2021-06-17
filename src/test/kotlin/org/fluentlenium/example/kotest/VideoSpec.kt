package org.fluentlenium.example.kotest

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.Spec
import io.kotest.extensions.testcontainers.perTest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.fluentlenium.adapter.kotest.FluentFreeSpec
import org.fluentlenium.configuration.ConfigurationProperties
import org.fluentlenium.configuration.FluentConfiguration
import org.fluentlenium.core.hook.wait.Wait
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import java.io.File

class VideoSpec : FluentFreeSpec() {

    private val SEARCH_TEXT = "FluentLenium"

    private val chrome = KBrowserWebDriverContainer()
        .withCapabilities(ChromeOptions())
        .withRecordingMode(
            BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL,
            File("build/videos"),
            VncRecordingContainer.VncRecordingFormat.MP4
        )

    override fun listeners(): List<TestListener> =
        listOf(chrome.perTest())

    override fun newWebDriver(): WebDriver =
        chrome.webDriver

    override fun beforeSpec(spec: Spec) {
        File("build/videos").mkdirs() shouldBe true
    }

    init {
        "Title of duck duck go" {
            goTo("https://duckduckgo.com")

            el("#search_form_input_homepage").fill().with(SEARCH_TEXT)
            el("#search_button_homepage").submit()
            await().untilWindow(SEARCH_TEXT)

            window().title() shouldContain SEARCH_TEXT
        }
    }
}

internal class KBrowserWebDriverContainer : BrowserWebDriverContainer<KBrowserWebDriverContainer>()
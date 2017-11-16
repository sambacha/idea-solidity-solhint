package idrabenia.solhint.client

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType.WARNING
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import idrabenia.solhint.client.process.EmptyProcess
import idrabenia.solhint.client.process.ServerProcess
import idrabenia.solhint.settings.data.SettingsManager.nodePath


object Environment {
    val isSolhintAvailable = isSolhintInstalled()

    init {
        validateDependencies()
    }

    fun validateDependencies() =
        if (!isNodeJsInstalled()) {
            notifyThatNodeNotInstalled()
        } else if (!isSolhintAvailable) {
            notifyThatSolhintNotInstalled()
        } else {
            // noop
        }

    fun solhintServer(baseDir: String) =
        if (isSolhintAvailable) {
            ServerProcess(nodePath(), baseDir)
        } else {
            EmptyProcess()
        }

    fun isNodeJsInstalled() =
        canRunProcess("${nodePath()} -v")

    fun isSolhintInstalled() =
        canRunProcess("solhint")

    fun notifyThatNodeNotInstalled() =
        error(
            "Node.js is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Node.js"
        )

    fun notifyThatSolhintNotInstalled() =
        error(
            "Solhint is not installed",
            "For correct run of Solidity Solhint Plugin you need to install Solhint. Just run 'npm install -g solhint'"
        )

    fun error(title: String, message: String) =
        if (ApplicationManager.getApplication() != null) {
            Notifications.Bus.notify(Notification("idrabenia.solidity-solhint", title, message, WARNING))
        } else {
            null
        }

    fun canRunProcess(cmd: String) =
        try {
            Runtime.getRuntime().exec(cmd).waitFor() == 0
        } catch (e: Exception) {
            false
        }
}
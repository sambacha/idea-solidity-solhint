package idrabenia.solhint.client.process

import idrabenia.solhint.utils.IOUtils
import java.io.File
import java.io.InputStream


class ServerProcess(val baseDir: String) : AbstractSolhintProcess {
    override val process = start()

    fun start(): Process? =
        ProcessBuilder()
            .redirectInput(ProcessBuilder.Redirect.from(serverCodeFile()))
            .directory(File(baseDir))
            .command("node")
            .start()
            .killOnShutdown()

    override fun stop() {
        process!!.destroyForcibly()
    }

    override fun isAlive() =
        process != null && process.isAlive

    private fun serverCodeFile(): File =
        File
            .createTempFile("solhint-server", ".js")
            .writeFrom(javaClass.getResourceAsStream("solhint-server.js"))

    private fun File.writeFrom(inputStream: InputStream): File {
        IOUtils.copy(inputStream, this.outputStream())
        return this
    }

    private fun Process.killOnShutdown(): Process {
        Runtime.getRuntime().addShutdownHook(Thread({ this.destroyForcibly() }))
        return this
    }
}
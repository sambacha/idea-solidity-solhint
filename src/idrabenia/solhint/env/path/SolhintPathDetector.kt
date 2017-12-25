package idrabenia.solhint.env.path

import java.io.File


object SolhintPathDetector : BasePathDetector() {

    private val solhintName = "solhint"

    fun detectSolhintPath(nodePath: String) =
        solhintForNode(nodePath) ?: detectPath(solhintName) ?: ""

    fun detectAllSolhintPaths() =
        detectAllInPaths(solhintName)

    fun solhintForNode(nodePath: String) =
        detectWithFilter(solhintName, { it.startsWith(File(nodePath).parent) })

}
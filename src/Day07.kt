// https://adventofcode.com/2022/day/7
fun main() {
    fun firstPart(inputFilePath: String): Long {
        val rootDir = FileTreeParser(readInput(inputFilePath)).parse()
        val dirSizes = TreeClimber(rootDir).climb().values
        return dirSizes.filter { it <= 100000 }.sum()
    }

    check(firstPart("Day07_test") == 95437L)
    println(firstPart("Day07"))

    fun secondPart(inputFilePath: String): Long {
        val rootDir = FileTreeParser(readInput(inputFilePath)).parse()
        val dirSizes = TreeClimber(rootDir).climb().values.sorted()
        return dirSizes.first { it >= 30000000 - (70000000 - dirSizes.last()) }
    }

    check(secondPart("Day07_test") == 24933642L)
    println(secondPart("Day07"))
}

private class TreeClimber(private val rootDir: DataDir) {
    private val dirSizesFound = hashMapOf<String, Long>()

    fun climb(): Map<String, Long> {
        climb(rootDir)
        return dirSizesFound
    }

    private fun climb(dir: DataDir): Long {
        val dirSize = dir.content.values.sumOf {
            when (it) {
                is DataFile -> it.fileSize
                is DataDir -> climb(it)
            }
        }
        dirSizesFound[dir.path] = dirSize
        return dirSize
    }
}

private class FileTreeParser(private val fileLines: List<String>) {
    private val rootDir = DataDir("/", parentDir = null)
    private var currentDir: DataDir = rootDir

    fun parse(): DataDir {
        val lineIterator = fileLines.iterator()
        while (lineIterator.hasNext()) {
            val textLine = lineIterator.next()
            when (val firstToken = textLine.split(" ").first()) {
                "$" -> parseCommandLine(textLine).executeChangeDirCommand()
                else -> {
                    val (fileSize, fileName) = textLine.split(" ")
                    currentDir.content[fileName] = if (firstToken == "dir") {
                        DataDir("${currentDir.path}/$fileName", parentDir = currentDir)
                    } else {
                        DataFile("${currentDir.path}/$fileName", fileSize.toLong())
                    }
                }
            }
        }
        return rootDir
    }

    private fun parseCommandLine(commandLine: String) = when {
        commandLine.startsWith("$ cd") -> when (val dirName = commandLine.split(" ").last()) {
            "/" -> Command.GoToRootDir
            ".." -> Command.GoToParentDir(currentDir.parentDir!!)
            else -> Command.ChangeDir(dirName, parentDir = currentDir)
        }

        commandLine.startsWith("$ ls") -> Command.ListFilesCurrentDir
        else -> throw java.io.IOException("Unknown Command")
    }

    private fun Command.executeChangeDirCommand() {
        when (this) {
            is Command.ChangeDir -> currentDir = currentDir.content[this.dirName] as DataDir
            is Command.GoToParentDir -> currentDir = currentDir.parentDir ?: currentDir
            Command.GoToRootDir -> currentDir = rootDir
            Command.ListFilesCurrentDir -> { /* ignore listing files, we only want to change dir */
            }
        }
    }
}

private sealed class Command {
    data class ChangeDir(val dirName: String, val parentDir: DataDir) : Command()
    data class GoToParentDir(val dir: DataDir) : Command()
    object GoToRootDir : Command()
    object ListFilesCurrentDir : Command()
}

private sealed interface File {
    val path: String
    val fileSize: Long
}

private data class DataDir(
    override val path: String, val parentDir: DataDir?, val content: HashMap<String, File> = hashMapOf(),
) : File {
    override val fileSize = 0L
}

private data class DataFile(
    override val path: String, override val fileSize: Long
) : File

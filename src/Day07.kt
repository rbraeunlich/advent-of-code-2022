import java.util.Collections

fun main() {
    fun part1(input: List<String>): Long {
        val fileSystem = RootDirectory()
        val allDirectories = mutableListOf<FileDirectory>()
        var currentDirectory: FileSystemNode = fileSystem
        input.forEach { line ->
            if (line == "$ cd /" || line == "$ ls") {
                // do nothing
            } else if (line.startsWith("dir")) {
                val directoryName = line.replace("dir ", "")
                val fileDirectory = FileDirectory(parent = currentDirectory, name = directoryName)
                currentDirectory.contents.add(fileDirectory)
                allDirectories.add(fileDirectory)
            } else if (line.matches("(\\d+) (.)+".toRegex())) {
                val fileSize = line.split(" ")[0].toLong()
                val fileName = line.split(" ")[1]
                currentDirectory.contents.add(File(size = fileSize, name = fileName, parent = currentDirectory))
            } else if (line == "$ cd ..") {
                currentDirectory = currentDirectory.parent

            } else {
                // must be directory change
                val newDirectory = line.replace("$ ", "").split(" ")[1]
                currentDirectory = currentDirectory.contents.first { it.name == newDirectory }
            }
        }
        return allDirectories
            .filter { it.size() < 100000 }
            .sumOf { it.size() }
    }

    fun part2(input: List<String>): Long {
        val fileSystem = RootDirectory()
        val allDirectories = mutableListOf<FileDirectory>()
        var currentDirectory: FileSystemNode = fileSystem
        input.forEach { line ->
            if (line == "$ cd /" || line == "$ ls") {
                // do nothing
            } else if (line.startsWith("dir")) {
                val directoryName = line.replace("dir ", "")
                val fileDirectory = FileDirectory(parent = currentDirectory, name = directoryName)
                currentDirectory.contents.add(fileDirectory)
                allDirectories.add(fileDirectory)
            } else if (line.matches("(\\d+) (.)+".toRegex())) {
                val fileSize = line.split(" ")[0].toLong()
                val fileName = line.split(" ")[1]
                currentDirectory.contents.add(File(size = fileSize, name = fileName, parent = currentDirectory))
            } else if (line == "$ cd ..") {
                currentDirectory = currentDirectory.parent

            } else {
                // must be directory change
                val newDirectory = line.replace("$ ", "").split(" ")[1]
                currentDirectory = currentDirectory.contents.first { it.name == newDirectory }
            }
        }
        val usedSpace = fileSystem.size()
        val unusedSpace = 70000000 - usedSpace
        val neededSpace = 30000000 - unusedSpace
        return allDirectories
            .filter { it.size() > neededSpace }
            .minOf { it.size() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

sealed interface FileSystemNode {
    fun size(): Long
    val parent: FileSystemNode
    val contents: MutableList<FileSystemNode>
    val name: String
}

data class RootDirectory(override val contents: MutableList<FileSystemNode> = mutableListOf()) : FileSystemNode {
    override fun size(): Long = contents.sumOf { it.size() }
    override val parent: FileSystemNode
        get() = this
    override val name: String
        get() = "/"
}

data class FileDirectory(
    override val contents: MutableList<FileSystemNode> = mutableListOf(),
    override val parent: FileSystemNode,
    override val name: String
) : FileSystemNode {
    override fun size(): Long = contents.sumOf { it.size() }
    override fun toString(): String {
        return "FileDirectory(contents=$contents, parent=${parent.name}, name='$name')"
    }
}

data class File(val size: Long, override val name: String, override val parent: FileSystemNode) : FileSystemNode {
    override fun size(): Long = size
    override val contents: MutableList<FileSystemNode> = Collections.unmodifiableList(mutableListOf())

    override fun toString(): String {
        return "File(size=$size, name='$name', parent=${parent.name}, contents=$contents)"
    }

}
// Node class representing a node in the data structure
data class Node<T>(val value: T, val children: MutableList<Node<T>> = mutableListOf())

// Extension function to add a child node
fun <T> Node<T>.addChild(child: Node<T>) {
    children.add(child)
}

// Extension function to perform depth-first search
fun <T> Node<T>.dfs(action: (Node<T>) -> Unit) {
    action(this)
    children.forEach { it.dfs(action) }
}

// Extension function to perform breadth-first search
fun <T> Node<T>.bfs(action: (Node<T>) -> Unit) {
    val queue = mutableListOf(this)
    while (queue.isNotEmpty()) {
        val node = queue.removeAt(0)
        action(node)
        queue.addAll(node.children)
    }
}

// Extension function to transform the data structure
fun <T, R> Node<T>.map(transform: (T) -> R): Node<R> {
    return Node(transform(value), children.map { it.map(transform) }.toMutableList())
}

// Extension function to filter the data structure
fun <T> Node<T>.filter(predicate: (T) -> Boolean): Node<T>? {
    return if (predicate(value)) {
        Node(value, children.mapNotNull { it.filter(predicate) }.toMutableList())
    } else {
        null
    }
}

// DSL function to build a data structure
fun <T> buildDataStructure(rootValue: T, builder: Node<T>.() -> Unit): Node<T> {
    val root = Node(rootValue)
    root.builder()
    return root
}

// Example usage
val dataStructure = buildDataStructure("A") {
    addChild(Node("B") {
        addChild(Node("D"))
        addChild(Node("E"))
    })
    addChild(Node("C") {
        addChild(Node("F"))
        addChild(Node("G"))
    })
}

println("Depth-First Search:")
dataStructure.dfs { node ->
    println(node.value)
}

println("\nBreadth-First Search:")
dataStructure.bfs { node ->
    println(node.value)
}

val transformedStructure = dataStructure.map { it.toLowerCase() }
println("\nTransformed Structure:")
transformedStructure.dfs { node ->
    println(node.value)
}

val filteredStructure = dataStructure.filter { it != "B" }
println("\nFiltered Structure:")
filteredStructure?.dfs { node ->
    println(node.value)
}

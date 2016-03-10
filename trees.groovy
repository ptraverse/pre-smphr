////https://gist.github.com/NAzT/267384
//
//class DFS {
//    def visitedNodeCounter = 0
//
//    def visitor = {
//        visitedNodeCounter++
//    }
//
//    def visit(node) {
//        node.acceptVisitor visitor
//        node.children.each { visit(it) }
//    }
//}
//
//class Node {
//    def children = []
//
//    def addChild(node) {
//        children << node
//    }
//
//    def acceptVisitor(visitor) {
//        visitor()
//    }
//}
//
//root = new Node()
//
//100.times {
//    root.addChild new Node()
//}
//
//root.children.each { child ->
//    50.times { child.addChild new Node() }
//}
//
//dfs = new DFS()
//dfs.visit(root)
//
//result = dfs.visitedNodeCounter
//
//println result


//http://stackoverflow.com/questions/13965056/groovy-node-depthfirst-returning-a-list-of-nodes-and-strings
def rawXml = """<xml>
    <metadata>
        <article>
            <body>
                <sec>
                    <title>A Title</title>
                    <p>
                        This contains
                        <italic>italics</italic>
                        and
                        <xref ref-type="bibr">xref's</xref>
                        .
                    </p>
                </sec>
                <sec>
                    <title>Second Title</title>
                </sec>
            </body>
        </article>
    </metadata>
</xml>"""

def processNode(String nodeText) {
    return nodeText
}

def processNode(Object node) {
    if(node.children().size() == 1) {
        return node.text()
    }
}

def xmlParser = new XmlParser()
def xml = xmlParser.parseText(rawXml)
// '**' is short hand for depthFirst()
// '*' is short hand for breadthFirst()
def xmlText = xml.metadata.article.body[0].'**'.findResults { node ->
    processNode(node)
}

println xmlText.join("|")
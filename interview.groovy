//Given a class:
//
// class Node {
//     Node parent;
// }
//
// which represents trees with only parent pointers, as is common in databases,
// write a member function setParent which sets the parent only if doing so would not create a cycle.
//
// Example
// Given:
// a = Node(parent:null)
// b = Node(parent:a)
// c = Node(parent:b)
// d = Node(parent:b)
//
// Then:
//
// a.setParent(c) should fail because it would create a cycle
// c.setParent(a) should succeed
//
// Define a set of test cases for your code, thinking through possible edge cases.

class Node {
    Node parent

    def getParent() {
        return this.parent
    }

    def getRoot() {
        if (this.parent == null) { //root
            return this
        } else {
            return this.parent.getRoot()
        }
    }

    def setParent(parentNode) {
        if (parentNode == this) { //cycle
            throw new Exception('')
        }

        if (parentNode == null) { //root
            this.parent = null
        } else {
            def visited = []
            println parentNode
            while (parentNode.getRoot() != null) {
                println parentNode.getRoot()
                visited.push(parentNode.getParent())
                println visited
                if (visited.contains(this)) {
                    throw new Exception('')
                }
            }
            this.parent = parentNode
        }

        return this
    }

}

def a = new Node(parent:null)
def b = new Node(parent:a)
def c = new Node(parent:b)
def d = new Node(parent:b)

//should fail because it would create a cycle
a.setParent(c)

//should succeed
c.setParent(a)
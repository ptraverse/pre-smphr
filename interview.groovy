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

    def setParent(parentNode) {
        //check if parentNode.parent is self -> loop
        if (parentNode == this) {
            throw new Exception('cycleException of selfloop')
        }
        // then travel up the chain of parents until you either find null (root) or self (cycle)
        // if you get to null then good if you get to self then bad
        def visited = [parentNode]
        while (!visited.contains(null) && !visited.contains(this)) {
            visited << visited.last().parent
        }
        if (visited.contains(null)) {
            //we got to the root, OK
            this.parent = parentNode
        } else if (visited.contains(this)) {
            //we found a cycle, not OK
            throw new Exception('cycleException of length ' + visited.size)
        }

        this
    }

}

def a = new Node(parent:null)
def b = new Node(parent:a)
def c = new Node(parent:b)
def d = new Node(parent:b)

//should fail because it would create a cycle
try {
    a.setParent(c)
    println 'success 1'
} catch (all) {
    println 'Fail: ' + all
}

//should succeed
try {
    c.setParent(a)
    println 'success 2'
} catch (all) {
    println 'Fail: ' + all
}

//More Test Cases
try {
    b.setParent(b) //self-loop
    println 'success 3'
} catch (all) {
    println 'Fail: ' + all
}
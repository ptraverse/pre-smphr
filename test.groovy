//println 'Hello world!'
//
//def x = 1
//println x
//
//x = new java.util.Date()
//println x
//
//x = -3.1499392
//println x
//
//x = false
//println x
//
//x = "Groovy!"
//
////Creating an empty list
//def technologies = []
//
///*** Adding a elements to the list ***/
//
//// As with Java
//technologies.add("Grails")
//
//// Left shift adds, and returns the list
//technologies << "Groovy"
//
//// Add multiple elements
//technologies.addAll(["Gradle","Griffon"])
//
//println technologies
//
///*** Removing elements from the list ***/
//
//// As with Java
//technologies.remove("Griffon")
//
//// Subtraction works also
//technologies = technologies - 'Grails'
//
//println technologies
//
///*** Iterating Lists ***/
//
//// Iterate over elements of a list
//technologies.each { println "Technology: $it"}
//technologies.eachWithIndex { it, i -> println "$i: $it"}
//

//2D Arrays

def x = 'superman'
def y = 'ubermensch'
def matrix = []
println matrix
x.each { i ->
    matrix.push([i])
}
println matrix

x.each {
    println it
}
fastaObj = new Fasta();
def result = fastaObj.NWScore x, y
println result
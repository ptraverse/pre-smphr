def uniprots = ['P68871', 'P01958']
def fastas = []
uniprots.each {
    def fastaObject = new Fasta(it)
    assert fastaObject.identifier == it
    fastaObject.setDataFromUniprot(it)
    fastaObject.parseData()
//    println fastaObject.sequence
    fastas.push(fastaObject)
}

//fastaCalc = new Fasta()
//def score = fastaCalc.NWScore fastas[0].sequence, fastas[1].sequence
//println score

def fastaObject = new Fasta()
def x = 'AGTACGCA'
def y = 'TATGC'
println fastaObject.Hirschberg(x, y)


def fastas = []

// SWISS-PROT
def sps = ['P68871', 'P01958']
sps.each {
    def fastaObject = new Fasta(it)
    assert fastaObject.identifier == it
    fastaObject.setDataSp(it)
    fastaObject.parseData()
    assert fastaObject.data > ''
    fastas.push(fastaObject)
}

// Genbank
def gbs = ['BN000065']
gbs.each {
    def fastaObject = new Fasta(it)
    assert fastaObject.identifier == it
    fastaObject.setDataGb(it)
    fastaObject.parseData()
    assert fastaObject.data > ''
}

fastaCalc = new Fasta()
def score = fastaCalc.NWScore fastas[0].sequence, fastas[1].sequence
assert score > 0


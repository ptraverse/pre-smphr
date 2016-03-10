def uniprots = ['P68871','P68874']
uniprots.each {
    def fastaObject = new Fasta(it)
    assert fastaObject.identifier == it
    println fastaObject.setDataFromUniprot(it)
}

def ebis = ['BN000065','A10909']
ebis.each {
    def fastaObject = new Fasta(it)
    assert fastaObject.identifier == it
    println fastaObject.setDataFromEbi(it)
}
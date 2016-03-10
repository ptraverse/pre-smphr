/*
 * Fetches the FASTA file for a given gene identifier
 */
def fetchFasta(id) {
    def url = 'https://www.ebi.ac.uk/ena/data/view/' + id + '&display=fasta&download=fasta&filename=' + id + '.fasta'
    def data = new URL(url).getText()
    println data
    data
}

def ids = ['BN000065', 'A10909']
ids.each {
    def fasta = fetchFasta it
    println fasta
    System.console().readLine 'Press Enter to Continue to Next'
}


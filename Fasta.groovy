import groovy.transform.Canonical

@Canonical
class Fasta {

    String identifier
    String data
    String description
    String sequence

    /**
     * Constructor
     * TODO properly define the type of sequence identifier
     * TODO https://en.wikipedia.org/wiki/FASTA_format#Sequence_identifiers
     *
     * @param {String} identifier
     */
    Fasta(identifier) {
        this.identifier = identifier
    }


    /**
     * Fetches the FASTA file from uniprot.org for a given sequence identifier
     *
     * @param {String} identifier
     *
     * @return this
     */
    def String setDataFromUniprot(identifier) {
        def url = 'http://www.uniprot.org/uniprot/' + identifier + '.fasta'
        def data = new URL(url).getText()
        this.data = data

        this
    }


    /**
    * Fetches the FASTA file from EBI for a given sequence identifier
     *
     * @param {String} identifier
     *
     * @return this
    */
    def String setDataFromEbi(identifier) {
        def url = 'https://www.ebi.ac.uk/ena/data/view/' + identifier + '&display=fasta&download=fasta&filename=' + identifier + '.fasta'
        def data = new URL(url).getText()
        this.data = data

        this
    }

    /**
     * Main Method
     * @param args
     */
    static void main(String[] args) {

    }

}
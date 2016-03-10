import groovy.transform.Canonical

@Canonical
class Fasta {

    String identifier
    String data
    String description
    String sequence = ''

    /**
     * Constructor
     * TODO figure out which data source to use based on sequence identifier
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
    def setDataFromUniprot(identifier) {
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
    def setDataFromEbi(identifier) {
        def url = 'https://www.ebi.ac.uk/ena/data/view/' + identifier + '&display=fasta&download=fasta&filename=' + identifier + '.fasta'
        def data = new URL(url).getText()
        this.data = data

        this
    }

    /**
     * Helper for constructor to parse data and split off first descriptor line and remove /n from sequence
     *
     * @return this
     */
    def parseData() {
        this.data.eachLine { line, number ->
            if (number == 0) {
                this.description = line
            } else {
                this.sequence += line
            }
        }

        this
    }

    /**
     * NWScore which calculates the matrix of Needleman Wunsch Algorithm between 2 strings
     * and returns the last line of that matrix
     * https://en.wikipedia.org/wiki/Hirschberg%27s_algorithm
     *
     * -- PseudoCode --
     function NWScore(X,Y)
        Score(0,0) = 0
        for j=1 to length(Y)
            Score(0,j) = Score(0,j-1) + Ins(Yj)
        for i=1 to length(X)
            Score(i,0) = Score(i-1,0) + Del(Xi)
            for j=1 to length(Y)
                scoreSub = Score(i-1,j-1) + Sub(Xi, Yj)
                scoreDel = Score(i-1,j) + Del(Xi)
                scoreIns = Score(i,j-1) + Ins(Yj)
                Score(i,j) = max(scoreSub, scoreDel, scoreIns)
            end
        end
        for j=0 to length(Y)
            LastLine(j) = Score(length(X),j)
        return LastLine
     *
     * @param {String} x
     * @param {String} y
     *
     * @return list
     */
    def NWScore(x, y) {
        def score = []
        def lastLine = []
        def i = 0 //TODO There must be a better way to get the iterator on the each in Groovy but this works for now
        x.each { xLetter ->
            score[i] = []
            def j = 0
            y.each { yLetter ->
                score[i][j] = 0
                j++
            }
            i++
        }

        def j = 1
        while (j < y.length()) {
            score[0][j] = score[0][j-1] + NWIns(y, j)
            j++
        }

        i = 1
        while (i < x.length()) {
            score[i][0] = score[i-1][0] + NWDel(x, i)
            j = 1
            while (j < y.length()) {
                def scoreSub = score[i - 1][j - 1] + NWSub(x, i, y, j)
                def scoreDel = score[i - 1][j] + NWDel(x, i)
                def scoreIns = score[i][j - 1] + NWIns(y, j)
                def scores = [scoreSub, scoreDel, scoreIns]
                score[i][j] = scores.max { it.value }.value
                j++
            }
            i++
        }

        j = 0
        while (j < y.length()) {
            println score[x.length() - 1][j]
            lastLine[j] = score[x.length() - 1][j]
            j++
        }

        lastLine
    }

    /**
     * Get a score for substitution
     * TODO - Could optimize this based on degree of similarity of AA's?
     * @param x
     * @param i
     * @param y
     * @param j
     *
     * @return int
     */
    def NWSub(x, i, y, j) {
        if (x.charAt(i) == y.charAt(j)) {
            return 2
        } else {
            return -1
        }
    }

    /**
     * Get a score for deletion
     *
     * @param x
     * @param i
     *
     * @return int
     */
    def NWDel(x, i) {
        return -2
    }

    /**
     * Get a score for insertion
     *
     * @param x
     * @param i
     *
     * @return int
     */
    def NWIns(x, i) {
        return -2
    }

    /**
     * Generates the optimal sequence alignment between 2 strings
     *
     * @param x
     * @param y
     *
     * return {String}
     */
    def Hirschberg(x, y) {
        //TODO
    }
}
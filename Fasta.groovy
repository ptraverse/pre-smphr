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
     * TODO e.g ">ENA|BN000065|BN000065.1 TPA: Homo sapiens SMP1 gene, RHD gene and RHCE gene"
     *
     * @param {String} identifier
     */
    Fasta(identifier) {
        this.identifier = identifier
    }

    /**
     * Genbank
     * gb|accession|locus
     * http://adina-howe.readthedocs.org/en/latest/ncbi/
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataGb(accession) {
        def url = 'http://eutils.ncbi.nlm.nih.gov/entrez/eutils/efetch.fcgi?db=nuccore&id=' + accession + '&rettype=fasta&retmode=text'
        def data = new URL(url).getText()
        this.data = data

        this
    }

    /**
     * EMB => Same as ENA
     * emb|accession|locus
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataEmb(accession) {
        this.setDataEna(accession)
    }

    /**
     * DBJ - DNA DB of Japan
     * dbj|accession|locus
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataDbj(accession) {
        throw new Exception("not yet implemented")
    }

    /**
     * Georgetown Protein Information Resource
     * pir||entry
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataPir(pir, entry) {
        throw new Exception("not yet implemented")
    }

    /**
     * Protein Research Foundation
     * prf||name
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataPrf(name) {
        throw new Exception("not yet implemented")
    }


    /**
     * SWISS-PROT
     * sp|accession|entry name
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataSp(accession) {
        def url = 'http://www.uniprot.org/uniprot/' + identifier + '.fasta'
        def data = new URL(url).getText()
        this.data = data

        this
    }


    /**
     * Brookhaven Protein Data Bank
     * pdb|entry|chain
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataPdb(entry, chain) {
        throw new Exception("not yet implemented")
    }

    /**
     * Patents
     * pat|country|number
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataPat(country, number) {
        throw new Exception("not yet implemented")
    }

    /**
     * GenInfo Backbone Id
     * bbs|number
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataBbs(number) {
        throw new Exception("not yet implemented")
    }

    /**
     * General database identifier
     * gnl|database|identifier
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataGnl(database, indentifier) {
        throw new Exception("not yet implemented")
    }

    /**
     * NCBI Reference Sequence
     * ref|accession|locus
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataRef(accession, locus) {
        throw new Exception("not yet implemented")
    }

    /**
     * Local Sequence identifier
     * lcl|identifier
     *
     * @param {String} accession
     *
     * @return this
     */
    def setDataLcl(identifier) {
        throw new Exception("not yet implemented")
    }


    /**
     * Fetches the FASTA file from EBI for a given sequence identifier
     * ENA European Nucleotide Archive
     * ena|accession|entry name
     *
     * @param {String} identifier
     *
     * @return this
    */
    def setDataEna(identifier) {
        def url = 'https://www.ebi.ac.uk/ena/data/view/' + identifier + '&display=fasta&download=fasta&filename=' + identifier + '.fasta'
        def data = new URL(url).getText()
        this.data = data

        this
    }

    /**
     * Helper for constructor to parse data and split off first descriptor line and remove /n from sequence
     *
     * TODO - Remove this
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
        def i = 0 //TODO Change this to use eachWithIndex
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
     function Hirschberg(X,Y)
        Z = ""
        W = ""
        if length(X) == 0
            for i=1 to length(Y)
                Z = Z + '-'
                W = W + Yi
            end
        else if length(Y) == 0
            for i=1 to length(X)
                 Z = Z + Xi
                 W = W + '-'
            end
        else if length(X) == 1 or length(Y) == 1
            (Z,W) = NeedlemanWunsch(X,Y)
        else
             xlen = length(X)
             xmid = length(X)/2
             ylen = length(Y)

             ScoreL = NWScore(X1:xmid, Y)
             ScoreR = NWScore(rev(Xxmid+1:xlen), rev(Y))
             ymid = PartitionY(ScoreL, ScoreR)

            (Z,W) = Hirschberg(X1:xmid, y1:ymid) + Hirschberg(Xxmid+1:xlen, Yymid+1:ylen)
        end
        return (Z,W)
     *
     * @param x
     * @param y
     * TODO Fniish the Neeldman Wunsch
     *
     * return {String}
     */
    def Hirschberg(x, y) {
        def Z
        def W
        if (x.length() == 0) {
            y.eachWithIndex { val, index ->
                Z += '-'
                W += W + val
            }
        } else if (y.length() == 0) {
            x.eachWithIndex { val, index ->
                Z = Z + val
                W = W + '-'
            }
        } else if (x.length() == 1 || y.length() == 1) {
//            return NeedlemanWunsch(x, y)
        } else {
            def xMid = x.length()/2

            def scoreL = NWScore(x[0..xMid-1], y)
            def scoreR = NWScore(x[xMid..x.length()].reverse(), y.reverse())
            def yMid = partitionY(scoreL, scoreR)

//          return Hirschberg(x[0..xMid-1],y[0..yMid-1]) + Hirschberg(x[xMid..x.length()], y[yMid..y.length()])
        }
//      return [Z, W]
    }

    def partitionY(scoreL, scoreR) {
        return max( [argMax(scoreL), argMax(scoreR.reverse())] )
    }

    def argMax(items) {
        def maxVal = ''
        def maxIndex = 0
        items.eachWithIndex { val, index ->
            if (val > maxVal) { maxVal = val }
            if (index > maxIndex) { maxIndex = index }
        }

        maxIndex
    }
}
package org.example;

public class RMSortingAlgo {

    public interface eComparator <L> {
        int compare(L a, L b);
    }

    //--------------------------------
    //MERGE SORTING
    //--------------------------------
    public static <L> void mergeSort(L[] a, eComparator<L> comp) {

        //if arrays of size 0 or 1 its sorted
        if (a == null || a.length <= 1)
            return;

        //midpoint
        int mid = a.length / 2;

        //Split array into 2 halves

        L[] left = (L[]) new Object[mid];
        L[] right = (L[]) new Object[a.length - mid];

        // Copy elements into the left half
        for (int i = 0; i < mid; i++) left[i] = a[i];
        //Copy elements into the right half
        for (int i = mid; i < a.length; i++) right [i - mid] = a[i];

        //Recursively sort both halves
        mergeSort(left, comp);
        mergeSort(right, comp);

        //merge sorted halves back into the array
        merge(a, left, right, comp);

    }

    private static <L> void merge(L[] result, L[] left, L[] right, eComparator <L> comp) {
        //index for left i, right j, result k
        int i = 0, j = 0, k = 0;

        //Compare elements from both arrays and copy smaller one
        while (i < left.length && j < right.length) {
            if (comp.compare(left[i], right[j]) <= 0){
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        //Copy any remaining elements from left array
        while (i < left.length) result[k++] = left[i++];
        //Copy any remaining elements from right
        while (j < right.length) result[k++] = right[j++];

    }

    //----------------------
    //COMPARATORS
    //----------------------

    // objects which are why they are classes

    /**
     * sorts by name alphabetically ascending
     */
    public static class PoliticianNameComp implements eComparator<Politician> {
        @Override
        public int compare(Politician a, Politician b) {
            return a.getName().compareTo(b.getName());
        }
    }

    /**
     * sorts by party first, if there is a tie in the party, the candidates are then further sorted by
     * name alphabetically ascending
     */
    public static class PoliticianPartyTheNameComp implements eComparator<Politician> {
        @Override
        public int compare(Politician a, Politician b) {
            int partyComp = a.getParty().compareTo(b.getParty());
            if (partyComp != 0) return partyComp;
            return a.getName().compareTo(b.getName());
        }
    }

    /**
     * Sorts elections alphabetically by type (A–Z)
     */
    public static class ElectionTypeComp implements eComparator<Election> {
        @Override
        public int compare(Election a, Election b) {
            return a.getType().compareTo(b.getType());
        }
    }

    /**
     * Sorts elections by date/year ascending (oldest first)
     */
    public static class ElectionYearAscComp implements eComparator<Election> {
        @Override
        public int compare(Election a, Election b) {
            return a.getDate().compareTo(b.getDate());
        }
    }

    /**
     * Sorts elections by date/year descending (newest first)
     */
    public static class ElectionYearDescComp implements eComparator<Election> {
        @Override
        public int compare(Election a, Election b) {
            return b.getDate().compareTo(a.getDate());
        }
    }

    public static class CandidateVotesDescComp implements eComparator<Candidate> {
        @Override
        public int compare(Candidate a, Candidate b) {
            return Integer.compare(b.getVotes(), a.getVotes());
        }
    }


    //------------------------------------
    //Sorting MLinkedList
    //------------------------------------
    public static <L> void sortList(MLinkedList<L> list, eComparator<L> comp) {

        if (list == null) return;

        int n = list.size();

        if (n <= 1) return;

        L[] a = (L[]) new Object[n];

        for (int i = 0; i < n; i++) {
            a[i] = list.get(i);
        }

        mergeSort(a, comp);

        list.clear();
        for (int i = 0; i < n; i++) {
            list.addElement(a[i]);
        }
    }
}

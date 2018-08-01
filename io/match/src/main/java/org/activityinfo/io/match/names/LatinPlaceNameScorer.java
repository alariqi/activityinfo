/*
 * ActivityInfo
 * Copyright (C) 2009-2013 UNICEF
 * Copyright (C) 2014-2018 BeDataDriven Groep B.V.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.activityinfo.io.match.names;

import com.google.common.annotations.VisibleForTesting;

/**
 * Scores the similarity between two names names written in a Latin
 * script.
 *
 */
public class LatinPlaceNameScorer {


    public static final int MINIMUM_STRING_LENGTH_FOR_FUZZY_MATCHING = 1;

    private static final int NONE = -1;

    // We store the current names names that we're matching against
    // in a pair of flyweight classes to void creating bazillions
    // of little objects in the course of matching

    private final LatinPlaceName x = new LatinPlaceName();
    private final LatinPlaceName y = new LatinPlaceName();

    private final LatinWordDistance distanceFunction = new LatinWordDistance();

    public LatinPlaceNameScorer() {
    }

    public double score(String importedValue, String referencedValue) {

        // quick check...
        if(importedValue.equals(referencedValue)) {
            return 1.0;
        }

        // if we don't have an exact match, first normalize the
        // the strings into lists of lowercase parts free of
        // diacriticals or other messiness

        x.set(importedValue);
        y.set(referencedValue);


        if(x.isEmpty() || y.isEmpty()) {
            return 0.0;
        }

        // Now we have two sets of ordered components, for example:
        //  [T1, T2, T3]  and  [S1, S2]

        // we want to know how likely it is that S refers to the
        // same entity as T.

        // So we not only have to deal with fuzzy matching, but we have to deal with it
        // on multiple levels:

        // (1) Proportion of parts matching
        // (2) Combinations of parts
        // (3) Similarity between non-matching parts

        // So we start off by running through all the combinations. If we have
        //  [A, B, C]  and  [X, Y]

        // We have to expand this into a set of mergings
        // [A, B, C]         [X, Y]
        // [AB,   C]   x     [XY]
        // [A,   BC]

        // We can think of each break between parts as a bit, and since
        // we're only concerned with names names composed of a small
        // number of parts, we'll use a bit set to iterate through all
        // the combinations

        // first try with no merging

        return score();

    }

    @VisibleForTesting
    void init(String xs, String ys) {
        this.x.set(xs);
        this.y.set(ys);
    }

    /**
     * Given two names names like X = "COMMUNE DE KAYES" and Y = "KAYES COMMUNE",
     * score the similarity between each part X[i] and Y[j] and find the best
     * assignment from i -> j.
     *
     *           | COMMUNE | DE   | KAYES  |
     * KAYES     |       0 |    0 |     1  |
     * COMMUNE   |       1 |    0 |     1  |
     *
     */
    @VisibleForTesting
    double score() {
        // swap x and y if necessary so that
        // left.numParts <= right.numParts

        LatinPlaceName left, right;
        if(x.partCount() <= y.partCount()) {
            left = this.x;
            right = this.y;
        } else {
            left = this.y;
            right = this.x;
        }

        // find the similarity between each pair of parts in left and right
        int leftParts = left.partCount();
        int rightParts = right.partCount();

        // keep track of which parts we have matched on the right so that we
        // don't count it twice
        boolean[] columnsMatched = new boolean[rightParts];

        // Our numerator is the (minimum) length of matching part pairs, weighted by
        // by their similarity score.

        // The denominator is the sum of the minimum length of matching part pairs, together
        // with the sum of all unmatched pairs.

        double numerator = 0;
        double denominator = 0;

        // For each left part, find the best matching right part, if there is one.

        for (int i = 0; i < leftParts; i++) {

            int leftLength = left.charCount(i);

            // Find the best remaining column to match
            int bestRight = -1;
            double bestScore = 0;
            for (int j = 0; j < rightParts; j++) {
                if(!columnsMatched[j]) {
                    double score = similarity(left, i, right, j);
                    if(score > 0 && score > bestScore) {
                        bestRight = j;
                        bestScore = score;
                    }
                }
            }

            // Did we match?
            if(bestRight != -1) {
                columnsMatched[bestRight] = true;

                // we use the minimum length of the word as the weight
                // to avoid inflating short words that match longer words because
                // of lots of vowels
                int minLength = Math.min(leftLength, right.charCount(bestRight));
                numerator += bestScore * (double)minLength;
                denominator += minLength;

            } else {
                denominator += leftLength;
            }
        }

        // Add unmatched "right" parts to the denominator
        for (int j = 0; j < rightParts; j++) {
            if(!columnsMatched[j]) {
                denominator += right.charCount(j);
            }
        }

        return numerator / denominator;
    }

    private double similarity(LatinPlaceName left, int leftPartIndex, LatinPlaceName right, int rightPartIndex) {
        int leftChars = left.charCount(leftPartIndex);
        int rightChars = right.charCount(rightPartIndex);

        // first try an exact comparison
        if(leftChars == rightChars) {
            boolean matchesExactly = true;
            for(int i=0;i!=leftChars;++i) {
                if(left.charAt(leftPartIndex, i) != right.charAt(rightPartIndex, i)) {
                    matchesExactly = false;
                    break;
                }
            }
            if(matchesExactly) {
                return 1.0;
            }
        }

        boolean numericLeft = left.isPartNumeric(leftPartIndex);
        boolean numericRight = right.isPartNumeric(rightPartIndex);

        if(numericLeft && numericRight) {
            if(left.parsePartAsInteger(leftPartIndex) == right.parsePartAsInteger(rightPartIndex)) {
                return 1.0;
            } else {
                return 0.0;
            }
        } else if(numericLeft) {
            return tryCompareNumericWithAlpha(left, leftPartIndex, right, rightPartIndex);

        } else if(numericRight) {
            return tryCompareNumericWithAlpha(right, rightPartIndex, left, leftPartIndex);
        }

        // now try an approximate match based on the phonetic shape

        return distanceFunction.similarity(
                left.chars, left.partStart(leftPartIndex), left.partStart(leftPartIndex + 1),
                right.chars, right.partStart(rightPartIndex), right.partStart(rightPartIndex + 1));
    }

    private double tryCompareNumericWithAlpha(LatinPlaceName nameWithNumericPart, int numericPartIndex,
                                              LatinPlaceName nameWithAlphaPart, int alphaPartIndex) {

        long x = nameWithNumericPart.parsePartAsInteger(numericPartIndex);
        long y = nameWithAlphaPart.tryParsePartAsRomanNumeral(alphaPartIndex);

        if(x == y) {
            return 1.0;
        } else {
            return 0.0;
        }
    }
}

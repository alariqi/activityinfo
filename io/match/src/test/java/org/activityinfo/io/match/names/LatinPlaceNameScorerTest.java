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

import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;


public class LatinPlaceNameScorerTest {

    public static final double MINIMUM_SCORE = 0.25;

    @Test
    public void matchCodes() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        assertThat(scorer.score("MDG21", "MDG11"), closeTo(0.43, 0.01));
    }

    @Test
    public void bestPermutations() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        scorer.init("COMMUNE DE KAYES", "COMMUNE KAYES");

        double bestScore = scorer.score();

        // the score ~ number of characters matched
        double numerator = "COMMUNEKAYES".length();
        double denominator = "COMMUNEDEKAYES".length();

        assertThat(bestScore, closeTo(numerator / denominator, 0.01));
    }

    @Test
    public void denominatorIncludesUnmatchedParts() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        double score = scorer.score("FINKOLO SIKASSO", "SIKASSO COMMUNE");

        double numerator = "SIKASSO".length();
        double denominator = "SIKASSO".length() + "FINKOLO".length() + "COMMUNE".length();
        assertThat(score, closeTo(numerator / denominator, 0.01));
    }

    @Test
    public void denominatorIncludesExtraParts() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        double score = scorer.score("BENKADI-FOUNIA", "BENKADI");

        double numerator = "BENKADI".length();
        double denominator = "BENKADI".length() + "FOUNIA".length();

        assertThat(score, closeTo(numerator / denominator, 0.01));
    }

    @Test
    public void consonantClusters() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        double score = scorer.score("AMBOALIMENA", "ABOALIMENA");

        System.out.println(score);
    }
    
    @Test
    public void somalia() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();

        double score = scorer.score("Jubbada Hoose (Lower Juba)", "Lower Juba");
        System.out.println(score);
        assertThat(score, greaterThan(MINIMUM_SCORE));
    }

    @Test
    public void tanganika() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();

        // ika vs yika
        // Ignore missing consonants before /i/
        double score = scorer.score("Tanganika", "Tanganyika");

        System.out.println(score);

        assertThat(score, greaterThan(MINIMUM_SCORE));
    }

    @Test
    public void caseInsensitive() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();

        double score = scorer.score("q1", "Q1");
        System.out.println(score);
        assertThat(score, greaterThan(MINIMUM_SCORE));
    }

    @Test
    public void avoidIntegerOverflowWithLongStrings() {
        final LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        double score = scorer.score("Partner with long name 111111111x 222222222222 333333333 444444444444444", "Social and Medical Assistance to Beneficiaries - 3");

        System.out.println(score);
        assertThat(score, equalTo((double) 0));
    }
    
    @Test
    @Ignore("merging not implemented yet")
    public void joffreVille() {
        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        assertThat(scorer.score("Joffre Ville", "Joffreville"), equalTo(1.0));
        assertThat(scorer.score("Joffreville", "Joffre Ville"), equalTo(1.0));

    }

    @Test
    public void longFieldNames() {
        String field1 = "2.2 Name of organization/department receiving the referral";
        String field2 = "If the refugee is a child (0-17year) who is the primary caretaker at present? ";

        final LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();
        System.out.println(scorer.score(field1, field2));
    }

    @Test
    public void nonLatinNames() {
        // When matching non-latin names, the algorithm should degrade gracefuly to score exact matches as 1.0
        // and everything else as 0.0

        LatinPlaceNameScorer scorer = new LatinPlaceNameScorer();

        assertThat(scorer.score("رقم تسجيل العائلة", "رقم تسجيل العائلة"), equalTo(1.0));
        assertThat(scorer.score("رقم تسجيل العائلة", "فلسطيني قادم من سوريا"), equalTo(0.0));
        assertThat(scorer.score("نعم", "Yes"), equalTo(0.0));
        assertThat(scorer.score("Yes / نعم", "Yes / نعم"), equalTo(1.0));
    }

//
//    @Test
//    public void profileNames() throws IOException {
//        final LatinPlaceName name = new LatinPlaceName();
//        final StringBuilder nucleus = new StringBuilder();
//
//        final PrintWriter nuclei = new PrintWriter("/home/alex/data/names/nuclei.txt");
//        final PrintWriter normalized = new PrintWriter("/home/alex/data/names/normalized.txt");
//
//
//        Files.readLines(new File("/home/alex/data/names.txt"), Charsets.UTF_8, new LineProcessor<Object>() {
//            @Override
//            public boolean processLine(String s) throws IOException {
//                name.set(s);
//
//                normalized.println(name.toString());
//
//                for(int i=0;i!=name.partCount();++i) {
//                    int start = name.partStart(i);
//                    int end = name.partStart(i+1);
//                    for(int j=start;j<end;j++) {
//
//                        if(isVowelChar(name.chars[j])) {
//                            nucleus.setLength(0);
//                            nucleus.append(name.chars[j]);
//                            while(j < end && isVowelChar(name.chars[j])) {
//                                nucleus.append(name.chars[j]);
//                                j++;
//                            }
//                            nuclei.println(nucleus.toString());
//                        }
//                    }
//                }
//                return true;
//
//            }
//
//            @Override
//            public Object getResult() {
//                return null;
//            }
//        });
//    }

}

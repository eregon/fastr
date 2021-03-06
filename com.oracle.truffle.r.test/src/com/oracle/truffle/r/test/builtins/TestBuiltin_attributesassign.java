/*
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 *
 * Copyright (c) 2014, Purdue University
 * Copyright (c) 2014, 2018, Oracle and/or its affiliates
 *
 * All rights reserved.
 */
package com.oracle.truffle.r.test.builtins;

import org.junit.Test;

import com.oracle.truffle.r.test.TestBase;

// Checkstyle: stop line length check
public class TestBuiltin_attributesassign extends TestBase {

    @Test
    public void testattributesassign1() {
        // FIXME According to docs NULL object is allowed (coerced to empty list)
        assertEval(Ignored.ImplementationError, "argv <- list(NULL, NULL);`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testattributesassign2() {
        assertEval(Output.IgnoreWhitespace,
                        "argv <- list(structure(list(c0 = structure(integer(0), .Label = character(0), class = 'factor')), .Names = 'c0', row.names = character(0), class = 'data.frame'), structure(list(c0 = structure(integer(0), .Label = character(0), class = 'factor')), .Names = 'c0', row.names = character(0), class = 'data.frame'));`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testattributesassign3() {
        assertEval("argv <- list(NA, value = NULL);`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testattributesassign4() {
        assertEval("argv <- list(1:6, value = NULL);`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testattributesassign5() {
        assertEval("argv <- list(c(2.63548374681491, 2.5910646070265, 2.66370092538965, 2.70586371228392, 2.78247262416629, 2.79379038469082, 2.72835378202123, 2.67394199863409, 2.66370092538965, 2.6222140229663, 2.59217675739587, 2.62013605497376, 2.60745502321467, 2.55870857053317, 2.60959440922522, 2.66558099101795, 2.74741180788642, 2.73878055848437, 2.67394199863409, 2.6232492903979, 2.59769518592551, 2.60852603357719, 2.53402610605613, 2.55630250076729, 2.52762990087134, 2.49136169383427, 2.55509444857832, 2.60638136511061, 2.70329137811866, 2.69108149212297, 2.63848925695464, 2.55990662503611, 2.54157924394658, 2.55870857053317, 2.50242711998443, 2.53147891704225, 2.52633927738984, 2.48429983934679, 2.54032947479087, 2.60638136511061, 2.66931688056611, 2.66745295288995, 2.62531245096167, 2.55022835305509, 2.54157924394658, 2.55144999797288, 2.47856649559384, 2.4983105537896, 2.48572142648158, 2.43296929087441, 2.48572142648158, 2.55022835305509, 2.60745502321467, 2.6159500516564, 2.57287160220048, 2.50242711998443, 2.49554433754645, 2.50105926221775, 2.44247976906445, 2.45331834004704, 2.44404479591808, 2.3747483460101, 2.43775056282039, 2.49415459401844, 2.54032947479087, 2.56110138364906, 2.4983105537896, 2.43136376415899, 2.42975228000241, 2.42651126136458, 2.36735592102602, 2.38381536598043, 2.35983548233989, 2.30749603791321, 2.35983548233989, 2.41329976408125, 2.46686762035411, 2.48000694295715, 2.42160392686983, 2.36921585741014, 2.35602585719312, 2.37106786227174, 2.27415784926368, 2.3096301674259, 2.30319605742049, 2.25527250510331, 2.32428245529769, 2.3747483460101, 2.4345689040342, 2.42160392686983, 2.38560627359831, 2.35983548233989, 2.37106786227174, 2.37291200297011, 2.29225607135648, 2.29225607135648, 2.28780172993023, 2.23552844690755, 2.28103336724773, 2.32014628611105, 2.38381536598043, 2.36172783601759, 2.3384564936046, 2.26245108973043, 2.25767857486918, 2.28555730900777, 2.25527250510331, 2.23299611039215, 2.22010808804005, 2.16435285578444, 2.20951501454263, 2.26481782300954, 2.29885307640971, 2.29885307640971, 2.25042000230889, 2.23552844690755, 2.21218760440396, 2.25042000230889, 2.17609125905568, 2.16136800223497, 2.14612803567824, 2.05690485133647, 2.12385164096709, 2.19865708695442, 2.23044892137827, 2.23044892137827, 2.17318626841227, 2.09691001300806, 2.13033376849501, 2.14921911265538, 2.10037054511756, 2.06069784035361, 2.07188200730613, 2.01703333929878, 2.07554696139253, 2.13353890837022, 2.17026171539496, 2.17026171539496, 2.13033376849501, 2.08278537031645, 2.11058971029925, 2.12057393120585, 2.07188200730613, 2.04921802267018), value = structure(list(tsp = c(1949, 1960.91666666667, 12), class = 'ts'), .Names = c('tsp', 'class')));`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testattributesassign6() {
        assertEval("argv <- list(structure(c(1, 1, 1, 1, 2, 3), .Dim = c(3L, 2L), .Dimnames = list(NULL, c('I', 'a')), foo = 'bar', class = 'matrix'), value = structure(list(class = 'matrix', foo = 'bar', dimnames = list(NULL, c('I', 'a')), dim = c(3L, 2L)), .Names = c('class', 'foo', 'dimnames', 'dim')));`attributes<-`(argv[[1]],argv[[2]]);");
    }

    @Test
    public void testArgsCasts() {
        assertEval("x <- 42;  attributes(x) <- 44");
        assertEval("x <- 42;  attributes(x) <- NULL");
        assertEval("x <- 42;  attributes(x) <- list()");
    }
}

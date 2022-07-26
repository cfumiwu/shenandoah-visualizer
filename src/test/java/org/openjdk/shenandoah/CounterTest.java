/*
 * ====
 *     Copyright (c) 2020, Red Hat, Inc. All rights reserved.
 *     DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 *     This code is free software; you can redistribute it and/or modify it
 *     under the terms of the GNU General Public License version 2 only, as
 *     published by the Free Software Foundation.  Oracle designates this
 *     particular file as subject to the "Classpath" exception as provided
 *     by Oracle in the LICENSE file that accompanied this code.
 *
 *     This code is distributed in the hope that it will be useful, but WITHOUT
 *     ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *     FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 *     version 2 for more details (a copy is included in the LICENSE file that
 *     accompanied this code).
 *
 *     You should have received a copy of the GNU General Public License version
 *     2 along with this work; if not, write to the Free Software Foundation,
 *     Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 *     Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 *     or visit www.oracle.com if you need additional information or have any
 *     questions.
 * ====
 *
 * Copyright (c) 2020, Red Hat, Inc. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package org.openjdk.shenandoah;

import org.HdrHistogram.Histogram;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.openjdk.shenandoah.RegionAffiliation.OLD;
import static org.openjdk.shenandoah.RegionAffiliation.YOUNG;
import static org.openjdk.shenandoah.RegionState.*;

public class CounterTest {
    List<RegionStat> stats_1 = new ArrayList<>();
    List<RegionStat> stats_2 = new ArrayList<>();
    List<RegionStat> stats_3 = new ArrayList<>();
    List<RegionStat> stats_4 = new ArrayList<>();
    List<RegionStat> stats_5 = new ArrayList<>();
    List<RegionStat> stats_6 = new ArrayList<>();
    List<RegionStat> stats_7 = new ArrayList<>();
    List<RegionStat> stats_8 = new ArrayList<>();
    List<RegionStat> stats_9 = new ArrayList<>();
    List<RegionStat> stats_10 = new ArrayList<>();
    List<RegionStat> stats_11 = new ArrayList<>();
    List<RegionStat> stats_12 = new ArrayList<>();
    List<RegionStat> stats_13 = new ArrayList<>();
    Snapshot snapshot_1;
    Snapshot snapshot_2;
    Snapshot snapshot_3;
    Snapshot snapshot_4;
    Snapshot snapshot_5;
    Snapshot snapshot_6;
    Snapshot snapshot_7;
    Snapshot snapshot_8;
    Snapshot snapshot_9;
    Snapshot snapshot_10;
    Snapshot snapshot_11;
    Snapshot snapshot_12;
    Snapshot snapshot_13;
    @Before
    public void setup() {
        for (int i = 0; i < 10; i++) {
            stats_1.add(new RegionStat(EMPTY_UNCOMMITTED, 0));
            stats_2.add(new RegionStat(EMPTY_COMMITTED, 3));
            stats_3.add(new RegionStat(TRASH, 6));
            stats_4.add(new RegionStat(HUMONGOUS, 9));
            stats_5.add(new RegionStat(PINNED_HUMONGOUS, 12));
            stats_6.add(new RegionStat(CSET, 15));
            stats_7.add(new RegionStat(PINNED, 0));
            stats_8.add(new RegionStat(PINNED_CSET, 0));
            stats_9.add(new RegionStat(1.0f, 1.0f, 0.3f, 0.3f, 0.5f, 0.3f, YOUNG, REGULAR));
            stats_10.add(new RegionStat(1.0f, 1.0f, 0.2f, 0.3f, 0.5f, 0.3f, YOUNG, REGULAR));
            stats_11.add(new RegionStat(1.0f, 1.0f, 0.7f, 0.8f, 0.9f, 0.5f, OLD, REGULAR));
            stats_12.add(new RegionStat(1.0f, 1.0f, 0.2f, 0.3f, 0.5f, 0.7f, YOUNG, REGULAR));
            stats_13.add(new RegionStat(1.0f, 1.0f, 0.2f, 0.9f, 0.5f, 0.7f, OLD, REGULAR));
        }
        snapshot_1= new Snapshot(0, 1024, 1, stats_1, 0, new Histogram(2));
        snapshot_2 = new Snapshot(0, 1024, 1, stats_2, 0, new Histogram(2));
        snapshot_3 = new Snapshot(0, 1024, 1, stats_3, 0, new Histogram(2));
        snapshot_4 = new Snapshot(0, 1024, 1, stats_4, 0, new Histogram(2));
        snapshot_5 = new Snapshot(0, 1024, 1, stats_5, 0, new Histogram(2));
        snapshot_6 = new Snapshot(0, 1024, 1, stats_6, 0, new Histogram(2));
        snapshot_7 = new Snapshot(0, 1024, 1, stats_7, 0, new Histogram(2));
        snapshot_8 = new Snapshot(0, 1024, 1, stats_8, 0, new Histogram(2));
        snapshot_9 = new Snapshot(0, 1024, 1, stats_9, 0, new Histogram(2));
        snapshot_10 = new Snapshot(0, 1024, 1, stats_10, 0, new Histogram(2));
        snapshot_11 = new Snapshot(0, 1024, 1, stats_11, 0, new Histogram(2));
        snapshot_12 = new Snapshot(0, 1024, 1, stats_12, 0, new Histogram(2));
        snapshot_13 = new Snapshot(0, 1024, 1, stats_13, 0, new Histogram(2));
        snapshot_1.stateCounter();
        snapshot_1.ageCounter();
        snapshot_2.stateCounter();
        snapshot_2.ageCounter();
        snapshot_3.stateCounter();
        snapshot_3.ageCounter();
        snapshot_4.stateCounter();
        snapshot_4.ageCounter();
        snapshot_5.stateCounter();
        snapshot_5.ageCounter();
        snapshot_6.stateCounter();
        snapshot_6.ageCounter();
        snapshot_7.stateCounter();
        snapshot_7.ageCounter();
        snapshot_8.stateCounter();
        snapshot_8.ageCounter();
        snapshot_9.stateCounter();
        snapshot_9.ageCounter();
        snapshot_10.stateCounter();
        snapshot_10.ageCounter();
        snapshot_11.stateCounter();
        snapshot_11.ageCounter();
        snapshot_12.stateCounter();
        snapshot_12.ageCounter();
        snapshot_13.stateCounter();
        snapshot_13.ageCounter();
    }

    @Test
    public void emptyUncommittedCounter_test() {
        Assert.assertEquals(snapshot_1.getEmptyUncommittedCount(), 10);
    }
    @Test
    public void emptyCommittedCounter_test() {
        Assert.assertEquals(snapshot_2.getEmptyCommittedCount(), 10);
    }
    @Test
    public void trashCounter_test() {
        Assert.assertEquals(snapshot_3.getTrashCount(), 10);
    }
    @Test
    public void humongousCounter_test() {
        Assert.assertEquals(snapshot_4.getHumongousCount(), 10);
    }
    @Test
    public void pinnedHumongousCounter_test() { Assert.assertEquals(snapshot_5.getPinnedHumongousCount(), 10); }
    @Test
    public void cSetCounter_test() { Assert.assertEquals(snapshot_6.getCSetCount(), 10); }
    @Test
    public void pinnedCounter_test() { Assert.assertEquals(snapshot_7.getPinnedCount(), 10); }
    @Test
    public void pinnedCSetCounter_test() { Assert.assertEquals(snapshot_8.getPinnedCSetCount(), 10);}
    @Test
    public void age0Counter_test() {
        Assert.assertEquals(snapshot_1.getAge0Count(), 10);
    }
    @Test
    public void age3Counter_test() {
        Assert.assertEquals(snapshot_2.getAge3Count(), 10);
    }
    @Test
    public void age6Counter_test() {
        Assert.assertEquals(snapshot_3.getAge6Count(), 10);
    }
    @Test
    public void age9Counter_test() {
        Assert.assertEquals(snapshot_4.getAge9Count(), 10);
    }
    @Test
    public void age12Counter_test() {
        Assert.assertEquals(snapshot_5.getAge12Count(), 10);
    }
    @Test
    public void age15Counter_test() {
        Assert.assertEquals(snapshot_6.getAge15Count(), 10);
    }
    @Test
    public void tlabCounter_test() {
        Assert.assertEquals(snapshot_9.getTlabCount(), 10);
    }
    @Test
    public void gclabCounter_test() {
        Assert.assertEquals(snapshot_10.getGclabCount(), 10);
    }
    @Test
    public void plabCounter_test() {
        Assert.assertEquals(snapshot_11.getPlabCount(), 10);
    }
    @Test
    public void sharedCounter_test() {
        Assert.assertEquals(snapshot_12.getSharedCount(), 10);
        Assert.assertEquals(snapshot_13.getSharedCount(), 10);
    }
}

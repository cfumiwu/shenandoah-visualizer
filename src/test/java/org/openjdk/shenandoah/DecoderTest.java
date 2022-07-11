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

import java.util.Collections;



public class DecoderTest {
    Snapshot snapshot_version1_idle;
    Snapshot snapshot_version1_marking;
    Snapshot snapshot_version1_evacuating;
    Snapshot snapshot_version1_update_refs;

    Snapshot snapshot_new_idle;
    Snapshot snapshot_new_marking;
    Snapshot snapshot_new_evacuating;
    Snapshot snapshot_new_update_refs;

    //setting up different value for both old and new versions
    @Before
    public void setUp() {
        snapshot_version1_idle = new Snapshot(0, 1024, 1, Collections.emptyList(), 0, new Histogram(2));
        snapshot_version1_marking = new Snapshot(0, 1024, 1, Collections.emptyList(), 1, new Histogram(2));
        snapshot_version1_evacuating = new Snapshot(0, 1024, 1, Collections.emptyList(), 2, new Histogram(2));
        snapshot_version1_update_refs = new Snapshot(0, 1024, 1, Collections.emptyList(), 4, new Histogram(2));

        snapshot_new_idle = new Snapshot(0, 1024, 2, Collections.emptyList(), 0, new Histogram(2));
        snapshot_new_marking = new Snapshot(0, 1024, 2, Collections.emptyList(), 1, new Histogram(2));
        snapshot_new_evacuating = new Snapshot(0, 1024, 2, Collections.emptyList(), 2, new Histogram(2));
        snapshot_new_update_refs = new Snapshot(0, 1024, 2, Collections.emptyList(), 3, new Histogram(2));

    }

    //Testing different phase for old version
    @Test
    public void test_version1_phase_idle() {
        Assert.assertEquals(Phase.IDLE, snapshot_version1_idle.phase());
    }
    @Test
    public void test_version1_phase_marking() {
        Assert.assertEquals(Phase.MARKING, snapshot_version1_marking.phase());
    }
    @Test
    public void test_version1_phase_evacuating() {
        Assert.assertEquals(Phase.EVACUATING, snapshot_version1_evacuating.phase());
    }
    @Test
    public void test_version1_phase_update_refs() {
        Assert.assertEquals(Phase.UPDATE_REFS, snapshot_version1_update_refs.phase());
    }

    //Testing different phase for new version
    @Test
    public void test_new_phase_idle() {
        Assert.assertEquals(Phase.IDLE, snapshot_new_idle.phase());
    }
    @Test
    public void test_new_phase_marking() {
        Assert.assertEquals(Phase.MARKING, snapshot_new_marking.phase());
    }
    @Test
    public void test_new_phase_evacuating() {
        Assert.assertEquals(Phase.EVACUATING, snapshot_new_evacuating.phase());
    }
    @Test
    public void test_new_phase_update_refs() {
        Assert.assertEquals(Phase.UPDATE_REFS, snapshot_new_update_refs.phase());
    }

}

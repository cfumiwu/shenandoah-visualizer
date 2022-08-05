/*
 * Copyright (c) 2022, Amazon.com, Inc. All rights reserved.
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

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;

public class RegionPopUp extends JFrame {
    private int regionNumber;
    private float usedLvl;
    private float liveLvl;
    private float tlabLvl;
    private float gclabLvl;
    private float plabLvl;
    private float sharedLvl;
    private RegionState state;
    private long age;
    private RegionAffiliation affiliation;
    private float spotlightUsedLvl;
    private float spotlightLiveLvl;
    private float spotlightTlabLvl;
    private float spotlightGclabLvl;
    private float spotlightPlabLvl;
    private float spotlightSharedLvl;
    private RegionState spotlightState;
    private long spotlightAge;
    private RegionAffiliation spotlightAffiliation;
    private int startIndex = 0;
    private int frameHeight;
    private int squareSize = 15;
    private int spotlightSquareSize = 28;
    private int numberOfShowRegions = 25;
    private int initialY = 1;
    private boolean noAutomaticScroll = false;
    private boolean stepbackStop = false;

    Snapshot snapshot;
    RegionStat spotlightRegionData;

    List<Snapshot> snapshots = new LinkedList<Snapshot>();

    public RegionPopUp(Snapshot snapshot, int regionNumber) {
        this.snapshot = snapshot;
        this.regionNumber = regionNumber;

        JPanel detailedStatePanel = new JPanel() {
            public void paint(Graphics g) {
                detailedStatePaint(g);
            }
        };

        JPanel timelinePanel = new JPanel() {
            public void paint (Graphics g) {
                timelinePaint(g);
            }
        };
        JPanel controlPanel = new JPanel();
        JPanel spotlightPanel = new JPanel() {
            public void paint (Graphics g) {
                spotlightPaint(g);
            }
        };
        JButton stepbackButton = new JButton("-1");
        JButton stepforwardButton = new JButton("+1");
        JButton realtimeButton = new JButton("Realtime");
        JButton pauseButton = new JButton("Pause");

        stepbackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noAutomaticScroll = true;
                if (!stepbackStop) {
                    startIndex--;
                }
                timelinePanel.repaint();
            }
        });

        stepforwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((startIndex + 1) < (snapshots.size() - numberOfShowRegions)) {
                    noAutomaticScroll = true;
                    stepbackStop = false;
                    startIndex++;
                } else {
                    noAutomaticScroll = false;
                }
                timelinePanel.repaint();
            }
        });

        realtimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noAutomaticScroll = false;
                stepbackStop = false;
                timelinePanel.repaint();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                noAutomaticScroll = true;
            }
        });

        stepbackButton.setBounds(10, 5, 25, 20);
        stepforwardButton.setBounds(35, 5, 25, 20);
        realtimeButton.setBounds(60, 5, 70, 20);
        pauseButton.setBounds(40, 25, 60,20);

        controlPanel.setLayout(null);
        controlPanel.add(stepbackButton);
        controlPanel.add(stepforwardButton);
        controlPanel.add(realtimeButton);
        controlPanel.add(pauseButton);

//        System.out.println(regionNumber);
        setSnapshot(snapshot);

        this.setLayout(new GridBagLayout());

        Insets pad = new Insets(7, 7, 7, 7);

        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 4.5;
            c.weighty = 4;
            c.insets = pad;
            this.add(detailedStatePanel, c);
        }

        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 2.5;
            c.weighty = 7;
            c.insets = pad;
            c.gridheight = 2;
            this.add(timelinePanel, c);
        }
        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 1;
            c.weightx = 3;
            c.weighty = 3;
            c.insets = pad;
            this.add(controlPanel, c);
        }
        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 2;
            c.gridy = 0;
            c.weightx = 4;
            c.weighty = 5;
            c.insets = pad;
            this.add(spotlightPanel, c);
        }

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                frameHeight = e.getComponent().getHeight();
                numberOfShowRegions = (frameHeight / squareSize) - 3;
            }
        });


    }
    public synchronized void timelinePaint(Graphics g) {
        int y = initialY;
        for (int i = snapshots.size() - 1; i >= 0; i--) {
            int index = i + startIndex;
            if (i < numberOfShowRegions && index < snapshots.size() && index >= 0) {
                RegionStat r = snapshots.get(index).get(regionNumber);
                if (y == initialY) {
                    r.render(g, 1, y, spotlightSquareSize, spotlightSquareSize);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString(Long.toString(snapshots.get(index).time()) + " ms", 35, y + spotlightSquareSize);
                    setSpotlightRegionStat(r);
                    if (index == 0) {
                        stepbackStop = true;
                    }
                    y += spotlightSquareSize;
                } else {
                    r.render(g, 7, y, squareSize, squareSize);
                    y += squareSize;
                }
                if (i % 10 == 0) {
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawString(Long.toString(snapshots.get(index).time()) + " ms", 30, y);
                }
            }
            if (startIndex < snapshots.size() - numberOfShowRegions && !noAutomaticScroll){
                startIndex++;
            }

        }

    }
    public synchronized void detailedStatePaint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Region index: " + regionNumber, 10, 30);
        g.drawString("Used Level: " + usedLvl + " %", 10, 50);
        g.drawString("Live Level: " + liveLvl + " %", 10, 70);
        g.drawString("TLAB Level: " + tlabLvl + " %", 10, 90);
        g.drawString("GCLAB Level: " + gclabLvl + " %", 10, 110);
        g.drawString("PLAB Level: " + plabLvl + " %", 10, 130);
        g.drawString("Shared Level: " + sharedLvl + " %", 10, 150);
        g.drawString("State: " + state, 10, 170);
        g.drawString("Age: " + age, 10, 190);
        g.drawString("Affiliation: " + affiliation, 10, 210);

    }
    public synchronized void spotlightPaint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Spotlight Region Data", 0, 30);
        g.drawString("Used Level: " + spotlightUsedLvl + " %", 0, 50);
        g.drawString("Live Level: " + spotlightLiveLvl + " %", 0, 70);
        g.drawString("TLAB Level: " + spotlightTlabLvl + " %", 0, 90);
        g.drawString("GCLAB Level: " + spotlightGclabLvl + " %", 0, 110);
        g.drawString("PLAB Level: " + spotlightPlabLvl + " %", 0, 130);
        g.drawString("Shared Level: " + spotlightSharedLvl + " %", 0, 150);
        g.drawString("State: " + spotlightState, 0, 170);
        g.drawString("Age: " + spotlightAge, 0, 190);
        g.drawString("Affiliation: " + spotlightAffiliation, 0, 210);
    }
    public final void setSnapshot(Snapshot snapshot) {
        this.snapshot = snapshot;
        RegionStat regionData = snapshot.get(regionNumber);
        usedLvl = regionData.used() * 100f;
        liveLvl = regionData.live() * 100f;
        tlabLvl = regionData.tlabAllocs() * 100f;
        gclabLvl = regionData.gclabAllocs() * 100f;
        plabLvl = regionData.plabAllocs() * 100f;
        sharedLvl = regionData.sharedAllocs() * 100f;
        state = regionData.state();
        age = regionData.age();
        affiliation = regionData.affiliation();

    }
    public final void setSnapshots(LinkedList<Snapshot> snapshots) {
        this.snapshots = snapshots;
    }
    public final void setSpotlightRegionStat(RegionStat r) {
        this.spotlightRegionData = r;
        spotlightUsedLvl = spotlightRegionData.used() * 100f;
        spotlightLiveLvl = spotlightRegionData.live() * 100f;
        spotlightTlabLvl = spotlightRegionData.tlabAllocs() * 100f;
        spotlightGclabLvl = spotlightRegionData.gclabAllocs() * 100f;
        spotlightPlabLvl = spotlightRegionData.plabAllocs() * 100f;
        spotlightSharedLvl = spotlightRegionData.sharedAllocs() * 100f;
        spotlightState = spotlightRegionData.state();
        spotlightAge = spotlightRegionData.age();
        spotlightAffiliation = spotlightRegionData.affiliation();
    }
}


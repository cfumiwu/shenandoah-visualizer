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
        JButton realtimeButton = new JButton("Syncing");
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


        this.setLayout(new GridBagLayout());

        Insets pad = new Insets(7, 7, 7, 7);

        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 4;
            c.weighty = 5;
            c.insets = pad;
            this.add(spotlightPanel, c);
        }

        {
            GridBagConstraints c = new GridBagConstraints();
            c.fill = GridBagConstraints.BOTH;
            c.gridx = 1;
            c.gridy = 0;
            c.weightx = 3;
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
            c.weighty = 2;
            c.insets = pad;
            this.add(controlPanel, c);
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

    public synchronized void spotlightPaint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Spotlight Region Data", 20, 30);
        g.drawString("Region index: " + regionNumber, 20, 50);
        g.drawString("Used Level: " + spotlightUsedLvl + " %", 20, 70);
        g.drawString("Live Level: " + spotlightLiveLvl + " %", 20, 90);
        g.drawString("TLAB Level: " + spotlightTlabLvl + " %", 20, 110);
        g.drawString("GCLAB Level: " + spotlightGclabLvl + " %", 20, 130);
        g.drawString("PLAB Level: " + spotlightPlabLvl + " %", 20, 150);
        g.drawString("Shared Level: " + spotlightSharedLvl + " %", 20, 170);
        g.drawString("State: " + spotlightState, 20, 190);
        g.drawString("Age: " + spotlightAge, 20, 210);
        g.drawString("Affiliation: " + spotlightAffiliation, 20, 230);
    }
    public final void setStepback(int n) {
        startIndex -= n;
        for (int i = 0; i < n; i++) {
            if (snapshots.size() > 0) {
                snapshots.remove(snapshots.size() - 1);
            }
        }
    }
    public final void setStepForward(int n) {
        startIndex += n;
    }
    public final void setNoAutomaticScroll(boolean noAutomaticScroll) {
        this.noAutomaticScroll = noAutomaticScroll;

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


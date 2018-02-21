//****************************************************************************
// Copyright (c) 1997-2017 F-Secure Corporation. All rights reserved.
//****************************************************************************

package com.forecast.utils;

import com.forecast.utils.FGridBagConstraints.Fill;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static com.forecast.utils.FGridBagConstraints.Anchor.GB_CENTER;
import static com.forecast.utils.FGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.FGridBagConstraints.GB_RELATIVE;
import static java.lang.System.arraycopy;


/**
 * Fixed version of GridBagLayout. Fixes BugId 4254022 (including support for more than 512 components. Also, provides an easy and efficient
 * way to initialize a constraint object reusing a single shared constraint object.
 */
public final class FGridBagLayout implements LayoutManager2 {

    private static final int MINSIZE = 1;
    private static final int PREFERREDSIZE = 2;

    private static final ThreadLocal sharedConstraintsContainer = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new FGridBagConstraints();
        }
    };

    /**
     * Gets the shared constraints initialized with values like when a constructor java.awt.GridBagConstraints() called.
     */
    public static FGridBagConstraints getDefaultSharedConstraints() {
        return getSharedConstraints(GB_RELATIVE, GB_RELATIVE, 1, 1, 0, 0, GB_CENTER, GB_NONE, 0, 0, 0, 0, 0, 0);
    }

    public static FGridBagConstraints getSharedConstraints
            (
                    int gridx,
                    int gridy,
                    int gridwidth,
                    int gridheight,
                    double weightx,
                    double weighty,
                    FGridBagConstraints.Anchor anchor,
                    Fill fill,
                    int top, int left, int bottom, int right
            ) {
        return getSharedConstraints(gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, top, left, bottom, right, 0, 0);
    }

    /**
     * Gets the shared constraints initialized with values like when a constructor java.awt.GridBagConstraints ( int gridx, int gridy, int
     * gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady ) called with one
     * exception: four ints (top, left, bottom, right) are used instead of an java.awt.Insets object.
     */
    public static FGridBagConstraints getSharedConstraints
    (
            int gridx,       // -1 <Default values>
            int gridy,       // -1
            int gridwidth,   // 1
            int gridheight,  // 1
            double weightx,  // 0.0
            double weighty,  // 0.0
            FGridBagConstraints.Anchor anchor,   // CENTER
            Fill fill,       // NONE
            int top, int left, int bottom, int right, // 0,0,0,0
            int ipadx, int ipady // 0, 0
    ) {
        FGridBagConstraints sharedConstraintsInstance = (FGridBagConstraints) sharedConstraintsContainer.get();

        sharedConstraintsInstance.gridx = gridx;
        sharedConstraintsInstance.gridy = gridy;
        sharedConstraintsInstance.gridwidth = gridwidth;
        sharedConstraintsInstance.gridheight = gridheight;
        sharedConstraintsInstance.fill = fill;
        sharedConstraintsInstance.ipadx = ipadx;
        sharedConstraintsInstance.ipady = ipady;

        sharedConstraintsInstance.top = top;
        sharedConstraintsInstance.left = left;
        sharedConstraintsInstance.bottom = bottom;
        sharedConstraintsInstance.right = right;

        sharedConstraintsInstance.anchor = anchor;
        sharedConstraintsInstance.weightx = weightx;
        sharedConstraintsInstance.weighty = weighty;

        return sharedConstraintsInstance;
    }

    public static Component createVerticalStrut() {
        return new JPanel();
    }

    public static FGridBagConstraints getVerticalStrutConstraints(int gridx, int gridy) {
        return getSharedConstraints(gridx, gridy, 1, 1, 0.0, 1.0, GB_CENTER, GB_NONE, 0, 0, 0, 0, 0, 0);
    }

    /**
     * This hashtable maintains the association between a component and its gridbag constraints. Keys in comptable are the components and the
     * values are the instances of FGridBagConstraints.
     */
    private final Map<Component, FGridBagConstraints> comptable = new HashMap<>();

    private int currentGridSize;

    /**
     * Creates a grid bag layout manager with the initial grid size of 3.
     */
    public FGridBagLayout() {
        this(10);
    }

    /**
     * Creates a grid bag layout manager with the given initial grid size.
     */
    public FGridBagLayout(int initialGridSize) {
        currentGridSize = initialGridSize;
    }

    /**
     * Sets the constraints for the specified component in this layout.
     */
    public void setConstraints(Component comp, FGridBagConstraints constraints) {
        comptable.put(comp, constraints.clone());
    }

    /**
     * Gets the constraints for the specified component.  A copy of the actual <code>FGridBagConstraints</code> object is returned.
     *
     * @param comp the component to be queried.
     * @return the constraint for the specified component in this grid bag layout; a copy of the actual constraint object is returned.
     */
    public FGridBagConstraints getConstraints(Component comp) {
        return comptable.get(comp).clone();
    }

    /**
     * Retrieves the constraints for the specified component. The return value is not a copy, but is the actual
     * <code>FGridBagConstraints</code> object used by the layout mechanism.
     *
     * @param comp the component to be queried
     * @return the constraints for the specified component.
     */
    private FGridBagConstraints lookupConstraints(Component comp) {
        return comptable.get(comp);
    }

    /**
     * Removes the constraints for the specified component in this layout
     *
     * @param comp the component to be modified.
     */
    private void removeConstraints(Component comp) {
        comptable.remove(comp);
    }

    /**
     * Adds the specified component with the specified name to the layout.
     *
     * @param name the name of the component.
     * @param comp the component to be added.
     */
    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Adds the specified component to the layout, using the specified constraint object.
     *
     * @param comp        the component to be added.
     * @param constraints an object that determines how the component is added to the layout.
     */
    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof FGridBagConstraints) {
            setConstraints(comp, (FGridBagConstraints) constraints);
        } else if (constraints != null) {
            throw new IllegalArgumentException("cannot add to layout: constraint must be a FGridBagConstraint");
        }
    }

    /**
     * Removes the specified component from this layout. <p> Most applications do not call this method directly.
     *
     * @param comp the component to be removed.
     */
    @Override
    public void removeLayoutComponent(Component comp) {
        removeConstraints(comp);
    }

    /**
     * Determines the preferred size of the <code>target</code> container using this grid bag layout. <p> Most applications do not call this
     * method directly.
     *
     * @param parent the container in which to do the layout.
     */
    @Override
    public Dimension preferredLayoutSize(Container parent) {
        return getMinSize(parent, getLayoutInfo(parent, PREFERREDSIZE));
    }

    /**
     * Determines the minimum size of the <code>target</code> container using this grid bag layout. <p> Most applications do not call this
     * method directly.
     *
     * @param parent the container in which to do the layout.
     */
    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return getMinSize(parent, getLayoutInfo(parent, MINSIZE));
    }

    /**
     * Returns the maximum dimensions for this layout given the components in the specified target container.
     *
     * @param target the component which needs to be laid out
     */
    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Returns the alignment along the x axis.  This specifies how the component would like to be aligned relative to other components.  The
     * value should be a number between 0 and 1 where 0 represents alignment along the origin, 1 is aligned the furthest away from the origin,
     * 0.5 is centered, etc.
     */
    @Override
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    /**
     * Returns the alignment along the y axis.  This specifies how the component would like to be aligned relative to other components.  The
     * value should be a number between 0 and 1 where 0 represents alignment along the origin, 1 is aligned the furthest away from the origin,
     * 0.5 is centered, etc.
     */
    @Override
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    /**
     * Invalidates the layout, indicating that if the layout manager has cached information it should be discarded.
     */
    @Override
    public void invalidateLayout(Container target) {
    }

    /**
     * Lays out the specified container using this grid bag layout. This method reshapes components in the specified container in order to
     * satisfy the constraints of this <code>GridBagLayout</code> object. <p> Most applications do not call this method directly.
     *
     * @param parent the container in which to do the layout.
     */
    @Override
    public void layoutContainer(Container parent) {
        int parentWidth = parent.getWidth();
        int parentHeight = parent.getHeight();
        Component comp;
        int compindex;
        FGridBagConstraints constraints;
        Insets insets = parent.getInsets();
        Component[] components = parent.getComponents();
        Dimension d;
        Rectangle r = new Rectangle();
        int i;
        int diffw;
        int diffh;
        double weight;
        FGridBagLayoutInfo info;

        /*
         * If the parent has no slaves anymore, then don't do anything
         * at all:  just leave the parent's size as-is.
         */
        if (components.length == 0) {
            return;
        }

        /*
         * Pass #1: scan all the slaves to figure out the total amount
         * of space needed.
         */

        info = getLayoutInfo(parent, PREFERREDSIZE);
        d = getMinSize(parent, info);

        if (parentWidth < d.width || parentHeight < d.height)
        // if (parent.width < d.width || parent.height < d.height)
        {
            info = getLayoutInfo(parent, MINSIZE);
            d = getMinSize(parent, info);
        }

        r.width = d.width;
        r.height = d.height;

        /*
         * If the current dimensions of the window don't match the desired
         * dimensions, then adjust the minWidth and minHeight arrays
         * according to the weights.
         */

        diffw = parentWidth - r.width;
        if (diffw != 0) {
            weight = 0.0;

            for (i = 0; i < info.width; i++) {
                weight += info.weightX[i];
            }

            if (weight > 0.0) {
                for (i = 0; i < info.width; i++) {
                    int dx = (int) ((((double) diffw) * info.weightX[i]) / weight);
                    info.minWidth[i] += dx;
                    r.width += dx;
                    if (info.minWidth[i] < 0) {
                        r.width -= info.minWidth[i];
                        info.minWidth[i] = 0;
                    }
                }
            }
            diffw = parentWidth - r.width;
        } else {
            diffw = 0;
        }

        diffh = parentHeight - r.height;
        if (diffh != 0) {
            weight = 0.0;

            for (i = 0; i < info.height; i++) {
                weight += info.weightY[i];
            }

            if (weight > 0.0) {
                for (i = 0; i < info.height; i++) {
                    int dy = (int) ((((double) diffh) * info.weightY[i]) / weight);
                    info.minHeight[i] += dy;
                    r.height += dy;
                    if (info.minHeight[i] < 0) {
                        r.height -= info.minHeight[i];
                        info.minHeight[i] = 0;
                    }
                }
            }

            diffh = parentHeight - r.height;

        } else {
            diffh = 0;
        }

        /*
         * Now do the actual layout of the slaves using the layout information
         * that has been collected.
         */

        info.startx = diffw / 2 + insets.left;
        info.starty = diffh / 2 + insets.top;

        for (compindex = 0; compindex < components.length; compindex++) {
            comp = components[compindex];

            if (!comp.isVisible()) {
                continue;
            }

            constraints = lookupConstraints(comp);

            r.x = info.startx;
            for (i = 0; i < constraints.tempX; i++) {
                r.x += info.minWidth[i];
            }

            r.y = info.starty;
            for (i = 0; i < constraints.tempY; i++) {
                r.y += info.minHeight[i];
            }

            r.width = 0;
            for (i = constraints.tempX; i < (constraints.tempX + constraints.tempWidth); i++) {
                r.width += info.minWidth[i];
            }

            r.height = 0;
            for (i = constraints.tempY; i < (constraints.tempY + constraints.tempHeight); i++) {
                r.height += info.minHeight[i];
            }

            adjustForGravity(constraints, r);

            /*
             * If the window is too small to be interesting then
             * unmap it.  Otherwise configure it and then make sure
             * it's mapped.
             */

            if ((r.width <= 0) || (r.height <= 0)) {
                comp.setBounds(0, 0, 0, 0);
            } else if (comp.getX() != r.x || comp.getY() != r.y || comp.getWidth() != r.width || comp.getHeight() != r.height) {
                comp.setBounds(r.x, r.y, r.width, r.height);
            }
        }
    }

    /*
     * Fill in an instance of the above structure for the current set
     * of managed children.  This requires three passes through the
     * set of children:
     *
     * 1) Figure out the dimensions of the layout grid
     * 2) Determine which cells the components occupy
     * 3) Distribute the weights and min sizes among the rows/columns.
     *
     * This also caches the minsizes for all the children when they are
     * first encountered (so subsequent loops don't need to ask again).
     */

    private FGridBagLayoutInfo getLayoutInfo(Container parent, int sizeflag) {
        // layoutInfo.width layoutInfo.height
        FGridBagLayoutInfo layoutInfo = new FGridBagLayoutInfo();
        Component comp;
        FGridBagConstraints constraints;
        Dimension d;
        Component[] components = parent.getComponents();

        int compindex, i, k, px, py, pixelsDiff, nextSize;
        int curX, curY, curWidth, curHeight, curRow, curCol;
        double weightDiff, weight;
        int[] xMax;
        int[] yMax;

        /*
         * Pass #1
         *
         * Figure out the dimensions of the layout grid (use a value of 1 for
         * zero or negative widths and heights).
         */

        layoutInfo.setWidth(0);
        layoutInfo.setHeight(0);
        curRow = curCol = -1;
        xMax = new int[512];
        yMax = new int[512];

        for (compindex = 0; compindex < components.length; compindex++) {
            comp = components[compindex];
            if (!comp.isVisible()) {
                continue;
            }
            constraints = lookupConstraints(comp);

            if (constraints == null) {
//        ErrorLog.log("FGridBagLayout: cannot find constraints for " + comp);
            }

            curX = constraints.gridx;
            curY = constraints.gridy;
            curWidth = constraints.gridwidth;

            if (curWidth <= 0) {
                curWidth = 1;
            }

            curHeight = constraints.gridheight;

            if (curHeight <= 0) {
                curHeight = 1;
            }

            /* If x or y is negative, then use relative positioning: */
            if (curX < 0 && curY < 0) {
                if (curRow >= 0) {
                    curY = curRow;
                } else if (curCol >= 0) {
                    curX = curCol;
                } else {
                    curY = 0;
                }
            }

            if (curX < 0) {
                px = 0;

                for (i = curY; i < (curY + curHeight) && i < xMax.length; i++) {
                    px = Math.max(px, xMax[i]);
                }

                curX = px - curX - 1;

                if (curX < 0) {
                    curX = 0;
                }
            } else if (curY < 0) {
                py = 0;
                for (i = curX; i < (curX + curWidth) && i < yMax.length; i++) {
                    py = Math.max(py, yMax[i]);
                }

                curY = py - curY - 1;

                if (curY < 0) {
                    curY = 0;
                }
            }

            /* Adjust the grid width and height */

            px = curX + curWidth;
            py = curY + curHeight;

            if (layoutInfo.width < px) {
                layoutInfo.setWidth(px);
            }
            if (layoutInfo.height < py) {
                layoutInfo.setHeight(py);
            }

            /* Adjust the xMax and yMax arrays */
            if (yMax.length < px) {
                int[] newYMax = new int[px + 512];
                arraycopy(yMax, 0, newYMax, 0, yMax.length);
                yMax = newYMax;
            }
            if (xMax.length < py) {
                int[] newXMax = new int[py + 512];
                arraycopy(xMax, 0, newXMax, 0, xMax.length);
                xMax = newXMax;
            }
            for (i = curX; i < px; i++) {
                yMax[i] = py;
            }

            for (i = curY; i < py; i++) {
                xMax[i] = px;
            }

            /* Cache the current slave's size. */
            d = (sizeflag == PREFERREDSIZE) ? comp.getPreferredSize() : comp.getMinimumSize();

            constraints.minWidth = d.width;
            constraints.minHeight = d.height;

            /* Zero width and height must mean that this is the last item (or
             * else something is wrong). */
            if (constraints.gridheight == 0 && constraints.gridwidth == 0) {
                curRow = curCol = -1;
            }

            if (constraints.gridheight == 0 && curRow < 0) {
                /* Zero width starts a new row */
                curCol = curX + curWidth;
            } else if (constraints.gridwidth == 0 && curCol < 0) {
                /* Zero height starts a new column */
                curRow = curY + curHeight;
            }
        }

        /*
         * Pass #2
         *
         * Negative values for gridX are filled in with the current x value.
         * Negative values for gridY are filled in with the current y value.
         * Negative or zero values for gridWidth and gridHeight end the current
         *  row or column, respectively.
         */

        curRow = curCol = -1;
        xMax = new int[512];
        yMax = new int[512];

        for (compindex = 0; compindex < components.length; compindex++) {
            comp = components[compindex];
            if (!comp.isVisible()) {
                continue;
            }
            constraints = lookupConstraints(comp);

            curX = constraints.gridx;
            curY = constraints.gridy;
            curWidth = constraints.gridwidth;
            curHeight = constraints.gridheight;

            /* If x or y is negative, then use relative positioning: */
            if (curX < 0 && curY < 0) {
                if (curRow >= 0) {
                    curY = curRow;
                } else if (curCol >= 0) {
                    curX = curCol;
                } else {
                    curY = 0;
                }
            }

            if (curX < 0) {
                if (curHeight <= 0) {
                    curHeight += (layoutInfo.height - curY);
                    if (curHeight < 1) {
                        curHeight = 1;
                    }
                }

                px = 0;
                for (i = curY; i < (curY + curHeight) && i < xMax.length; i++) {
                    px = Math.max(px, xMax[i]);
                }

                curX = px - curX - 1;
                if (curX < 0) {
                    curX = 0;
                }
            } else if (curY < 0) {
                if (curWidth <= 0) {
                    curWidth += (layoutInfo.width - curX);
                    if (curWidth < 1) {
                        curWidth = 1;
                    }
                }

                py = 0;

                for (i = curX; i < (curX + curWidth) && i < yMax.length; i++) {
                    py = Math.max(py, yMax[i]);
                }

                curY = py - curY - 1;
                if (curY < 0) {
                    curY = 0;
                }
            }

            if (curWidth <= 0) {
                curWidth += (layoutInfo.width - curX);
                if (curWidth < 1) {
                    curWidth = 1;
                }
            }

            if (curHeight <= 0) {
                curHeight += (layoutInfo.height - curY);
                if (curHeight < 1) {
                    curHeight = 1;
                }
            }

            px = curX + curWidth;
            py = curY + curHeight;

            if (yMax.length < px) {
                int[] newYMax = new int[px + 512];
                arraycopy(yMax, 0, newYMax, 0, yMax.length);
                yMax = newYMax;
            }

            if (xMax.length < py) {
                int[] newXMax = new int[py + 512];
                arraycopy(xMax, 0, newXMax, 0, xMax.length);
                xMax = newXMax;
            }

            for (i = curX; i < (curX + curWidth); i++) {
                yMax[i] = py;
            }

            for (i = curY; i < (curY + curHeight); i++) {
                xMax[i] = px;
            }

            /* Make negative sizes start a new row/column */
            if (constraints.gridheight == 0 && constraints.gridwidth == 0) {
                curRow = curCol = -1;
            }
            if (constraints.gridheight == 0 && curRow < 0) {
                curCol = curX + curWidth;
            } else if (constraints.gridwidth == 0 && curCol < 0) {
                curRow = curY + curHeight;
            }

            /* Assign the new values to the gridbag slave */
            constraints.tempX = curX;
            constraints.tempY = curY;
            constraints.tempWidth = curWidth;
            constraints.tempHeight = curHeight;
        }

        /*
         * Pass #3
         *
         * Distribute the minimun widths and weights:
         */

        nextSize = Integer.MAX_VALUE;

        for (i = 1; i != Integer.MAX_VALUE; i = nextSize, nextSize = Integer.MAX_VALUE) {
            for (compindex = 0; compindex < components.length; compindex++) {
                comp = components[compindex];

                if (!comp.isVisible()) {
                    continue;
                }
                constraints = lookupConstraints(comp);

                if (constraints.tempWidth == i) {
                    px = constraints.tempX + constraints.tempWidth; /* right column */

                    /*
                     * Figure out if we should use this slave\'s weight.  If the weight
                     * is less than the total weight spanned by the width of the cell,
                     * then discard the weight.  Otherwise split the difference
                     * according to the existing weights.
                     */

                    weightDiff = constraints.weightx;

                    for (k = constraints.tempX; k < px; k++) {
                        weightDiff -= layoutInfo.weightX[k];
                    }

                    if (weightDiff > 0.0) {
                        weight = 0.0;

                        for (k = constraints.tempX; k < px; k++) {
                            weight += layoutInfo.weightX[k];
                        }

                        for (k = constraints.tempX; weight > 0.0 && k < px; k++) {
                            double wt = layoutInfo.weightX[k];
                            double dx = (wt * weightDiff) / weight;
                            layoutInfo.weightX[k] += dx;
                            weightDiff -= dx;
                            weight -= wt;
                        }

                        /* Assign the remainder to the rightmost cell */
                        layoutInfo.weightX[px - 1] += weightDiff;
                    }

                    /*
                     * Calculate the minWidth array values.
                     * First, figure out how wide the current slave needs to be.
                     * Then, see if it will fit within the current minWidth values.
                     * If it will not fit, add the difference according to the
                     * weightX array.
                     */

                    pixelsDiff =
                            constraints.minWidth + constraints.ipadx +
                                    constraints.left + constraints.right;

                    for (k = constraints.tempX; k < px; k++) {
                        pixelsDiff -= layoutInfo.minWidth[k];
                    }

                    if (pixelsDiff > 0) {
                        weight = 0.0;

                        for (k = constraints.tempX; k < px; k++) {
                            weight += layoutInfo.weightX[k];
                        }

                        for (k = constraints.tempX; weight > 0.0 && k < px; k++) {
                            double wt = layoutInfo.weightX[k];
                            int dx = (int) ((wt * ((double) pixelsDiff)) / weight);
                            layoutInfo.minWidth[k] += dx;
                            pixelsDiff -= dx;
                            weight -= wt;
                        }

                        /* Any leftovers go into the rightmost cell */
                        layoutInfo.minWidth[px - 1] += pixelsDiff;
                    }
                } else if (constraints.tempWidth > i && constraints.tempWidth < nextSize) {
                    nextSize = constraints.tempWidth;
                }

                if (constraints.tempHeight == i) {
                    py = constraints.tempY + constraints.tempHeight; /* bottom row */

                    /*
                     * Figure out if we should use this slave's weight.  If the weight
                     * is less than the total weight spanned by the height of the cell,
                     * then discard the weight.  Otherwise split it the difference
                     * according to the existing weights.
                     */

                    weightDiff = constraints.weighty;

                    for (k = constraints.tempY; k < py; k++) {
                        weightDiff -= layoutInfo.weightY[k];
                    }

                    if (weightDiff > 0.0) {
                        weight = 0.0;

                        for (k = constraints.tempY; k < py; k++) {
                            weight += layoutInfo.weightY[k];
                        }

                        for (k = constraints.tempY; weight > 0.0 && k < py; k++) {
                            double wt = layoutInfo.weightY[k];
                            double dy = (wt * weightDiff) / weight;
                            layoutInfo.weightY[k] += dy;
                            weightDiff -= dy;
                            weight -= wt;
                        }

                        /* Assign the remainder to the bottom cell */
                        layoutInfo.weightY[py - 1] += weightDiff;
                    }

                    /*
                     * Calculate the minHeight array values.
                     * First, figure out how tall the current slave needs to be.
                     * Then, see if it will fit within the current minHeight values.
                     * If it will not fit, add the difference according to the
                     * weightY array.
                     */

                    pixelsDiff = constraints.minHeight + constraints.ipady +
                            constraints.top + constraints.bottom;

                    for (k = constraints.tempY; k < py; k++) {
                        pixelsDiff -= layoutInfo.minHeight[k];
                    }

                    if (pixelsDiff > 0) {
                        weight = 0.0;

                        for (k = constraints.tempY; k < py; k++) {
                            weight += layoutInfo.weightY[k];
                        }

                        for (k = constraints.tempY; weight > 0.0 && k < py; k++) {
                            double wt = layoutInfo.weightY[k];
                            int dy = (int) ((wt * ((double) pixelsDiff)) / weight);
                            layoutInfo.minHeight[k] += dy;
                            pixelsDiff -= dy;
                            weight -= wt;
                        }

                        /* Any leftovers go into the bottom cell */
                        layoutInfo.minHeight[py - 1] += pixelsDiff;
                    }
                } else if (constraints.tempHeight > i && constraints.tempHeight < nextSize) {
                    nextSize = constraints.tempHeight;
                }
            }
        }
        return layoutInfo;
    }

    /*
     * Adjusts the x, y, width, and height fields to the correct
     * values depending on the constraint geometry and pads.
     */
    private void adjustForGravity(FGridBagConstraints constraints, Rectangle r) {
        int diffx;
        int diffy;

        r.x += constraints.left;
        r.width -= (constraints.left + constraints.right);
        r.y += constraints.top;
        r.height -= (constraints.top + constraints.bottom);

        diffx = 0;
        if (
                (
                        constraints.fill != Fill.GB_HORIZONTAL &&
                                constraints.fill != Fill.GB_BOTH
                ) &&
                        (
                                r.width > (constraints.minWidth + constraints.ipadx)
                        )
                ) {
            diffx = r.width - (constraints.minWidth + constraints.ipadx);
            r.width = constraints.minWidth + constraints.ipadx;
        }

        diffy = 0;
        if (
                (
                        constraints.fill != Fill.GB_VERTICAL &&
                                constraints.fill != Fill.GB_BOTH
                ) &&
                        (
                                r.height > (constraints.minHeight + constraints.ipady)
                        )
                ) {
            diffy = r.height - (constraints.minHeight + constraints.ipady);
            r.height = constraints.minHeight + constraints.ipady;
        }

        switch (constraints.anchor) {
            case GB_CENTER:
                r.x += diffx / 2;
                r.y += diffy / 2;
                break;
            case GB_NORTH:
                r.x += diffx / 2;
                break;
            case GB_NORTHEAST:
                r.x += diffx;
                break;
            case GB_EAST:
                r.x += diffx;
                r.y += diffy / 2;
                break;
            case GB_SOUTHEAST:
                r.x += diffx;
                r.y += diffy;
                break;
            case GB_SOUTH:
                r.x += diffx / 2;
                r.y += diffy;
                break;
            case GB_SOUTHWEST:
                r.y += diffy;
                break;
            case GB_WEST:
                r.y += diffy / 2;
                break;
            case GB_NORTHWEST:
                break;
            default:
                throw new IllegalArgumentException("illegal anchor value");
        }
    }

    /*
     * Figure out the minimum size of the
     * master based on the information from GetLayoutInfo()
     */
    private Dimension getMinSize(Container parent, FGridBagLayoutInfo info) {
        Dimension d = new Dimension();
        int i;
        int t;
        Insets insets = parent.getInsets();

        t = 0;

        for (i = 0; i < info.width; i++) {
            t += info.minWidth[i];
        }

        d.width = t + insets.left + insets.right;

        t = 0;

        for (i = 0; i < info.height; i++) {
            t += info.minHeight[i];
        }

        d.height = t + insets.top + insets.bottom;

        return d;
    }

    private final class FGridBagLayoutInfo {

        int width;
        int height; /* number of cells horizontally, vertically */
        int startx;
        int starty; /* starting point for layout */
        int[] minWidth; /* largest minWidth in each column */
        int[] minHeight; /* largest minHeight in each row */
        double[] weightX; /* largest weight in each column */
        double[] weightY; /* largest weight in each row */

        FGridBagLayoutInfo() {
            minWidth = new int[currentGridSize];
            minHeight = new int[currentGridSize];
            weightX = new double[currentGridSize];
            weightY = new double[currentGridSize];
        }

        private void setWidth(int width) {
            this.width = width;
            if (currentGridSize < width) {
                currentGridSize += currentGridSize;
                if (currentGridSize < width) {
                    currentGridSize = width;
                }

                int[] newMinWidth = new int[currentGridSize];
                arraycopy(minWidth, 0, newMinWidth, 0, minWidth.length);
                minWidth = newMinWidth;

                double[] newWeightX = new double[currentGridSize];
                arraycopy(weightX, 0, newWeightX, 0, weightX.length);
                weightX = newWeightX;
            }
        }

        private void setHeight(int height) {
            this.height = height;
            if (currentGridSize < height) {
                currentGridSize += currentGridSize;
                if (currentGridSize < height) {
                    currentGridSize = height;
                }

                int[] newMinHeight = new int[currentGridSize];
                arraycopy(minHeight, 0, newMinHeight, 0, minHeight.length);
                minHeight = newMinHeight;

                double[] newWeightY = new double[currentGridSize];
                arraycopy(weightY, 0, newWeightY, 0, weightY.length);
                weightY = newWeightY;
            }
        }
    }
}
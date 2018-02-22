package com.forecast.utils;

import static java.lang.Double.doubleToLongBits;


public final class MyGridBagConstraints implements Cloneable {

  public static final int GB_RELATIVE = -1;
  public static final int GB_REMAINDER = 0;

  public enum Anchor {
    GB_CENTER,
    GB_NORTH,
    GB_NORTHEAST,
    GB_EAST,
    GB_SOUTHEAST,
    GB_SOUTH,
    GB_SOUTHWEST,
    GB_WEST,
    GB_NORTHWEST
  }

  public enum Fill {
    GB_NONE,
    GB_BOTH,
    GB_HORIZONTAL,
    GB_VERTICAL
  }

  public int gridx;
  public int gridy;
  public int gridwidth;
  public int gridheight;
  public double weightx;
  public double weighty;
  public Anchor anchor;
  public Fill fill;
  public int top;
  public int left;
  public int bottom;
  public int right;
  public int ipadx;
  public int ipady;

  int tempX;
  int tempY;
  int tempWidth;
  int tempHeight;
  int minWidth;
  int minHeight;

  MyGridBagConstraints() {
    gridx = GB_RELATIVE;
    gridy = GB_RELATIVE;
    gridwidth = 1;
    gridheight = 1;

    weightx = 0;
    weighty = 0;
    anchor = Anchor.GB_CENTER;
    fill = Fill.GB_NONE;

    top = 0;
    left = 0;
    bottom = 0;
    right = 0;

    ipadx = 0;
    ipady = 0;
  }

  @Override
  public MyGridBagConstraints clone() {
    try {
      return (MyGridBagConstraints) super.clone();
    } catch (CloneNotSupportedException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj instanceof MyGridBagConstraints) {
      MyGridBagConstraints that = (MyGridBagConstraints) obj;
      return
          that.gridx == this.gridx &&
          that.gridy == this.gridy &&
          that.gridwidth == this.gridwidth &&
          that.gridheight == this.gridheight &&
          doubleToLongBits(that.weightx) == doubleToLongBits(this.weightx) &&
          doubleToLongBits(that.weighty) == doubleToLongBits(this.weighty) &&
          that.anchor == this.anchor &&
          that.fill == this.fill &&
          that.top == this.top &&
          that.left == this.left &&
          that.bottom == this.bottom &&
          that.right == this.right &&
          that.ipadx == this.ipadx &&
          that.ipady == this.ipady;
    }
    return false;
  }
}
package com.deveclopers.myleague.utils;

import java.util.ArrayList;

public class BinaryList extends ArrayList<Integer> {
  @Override
  public boolean add(Integer i) {
    if (i == 0 || i == 1) {
      return super.add(i);
    } else {
      throw new IllegalArgumentException("Only 0s and 1s are allowed.");
    }
  }
}

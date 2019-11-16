package com.sonusourav.merazoverflow.model;

import java.util.Comparator;

public class sortByActivity implements Comparator<Question> {
  public int compare(Question a, Question b) {
    return (int) (a.getLastActivityDate() - b.getLastActivityDate());
  }
}

package com.sonusourav.merazoverflow.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Question implements Serializable {

  @SerializedName("tags")
  private String[] tags;
  @SerializedName("is_answered")
  private boolean isAnswered;
  @SerializedName("view_count")
  private int viewCount;
  @SerializedName("closed_date")
  private long timestamp;
  @SerializedName("accepted_answer_id")
  private long acceptedAnswerid;
  @SerializedName("answer_count")
  private int answerCount;
  @SerializedName("score")
  private int score;
  @SerializedName("last_activity_date")
  private long lastActivityDate;
  @SerializedName("creation_date")
  private long creationDate;
  @SerializedName("last_edit_date")
  private long editDate;
  @SerializedName("question_id")
  private long questionId;
  @SerializedName("link")
  private String link;
  @SerializedName("close_reason")
  private String closeReason;
  @SerializedName("title")
  private String title;
  @SerializedName("owner")
  private Owner owner;

  public Question(String[] tags, Owner owner, boolean isAnswered, int viewCount, long timestamp,
      long acceptedAnswerId, int answerCount, int score, long lastActivityDate, long creationDate,
      long editDate, long questionId, String link, String closeReason, String title) {
    this.tags = tags;
    this.owner = owner;
    this.isAnswered = isAnswered;
    this.viewCount = viewCount;
    this.timestamp = timestamp;
    this.acceptedAnswerid = acceptedAnswerId;
    this.answerCount = answerCount;
    this.score = score;
    this.lastActivityDate = lastActivityDate;
    this.creationDate = creationDate;
    this.editDate = editDate;
    this.questionId = questionId;
    this.link = link;
    this.closeReason = closeReason;
    this.title = title;
  }

  public Question() {
  }

  public String[] getTags() {
    return this.tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }

  public boolean isIsAnswered() {
    return this.isAnswered;
  }

  public boolean getIsAnswered() {
    return this.isAnswered;
  }

  public void setIsAnswered(boolean isAnswered) {
    this.isAnswered = isAnswered;
  }

  public int getViewCount() {
    return this.viewCount;
  }

  public void setViewCount(int viewCount) {
    this.viewCount = viewCount;
  }

  public long getTimestamp() {
    return this.timestamp;
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  public long getAcceptedAnswerid() {
    return this.acceptedAnswerid;
  }

  public void setAcceptedAnswerid(long acceptedAnswerid) {
    this.acceptedAnswerid = acceptedAnswerid;
  }

  public int getAnswerCount() {
    return this.answerCount;
  }

  public void setAnswerCount(int answerCount) {
    this.answerCount = answerCount;
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public long getLastActivityDate() {
    return this.lastActivityDate;
  }

  public void setLastActivityDate(long lastActivityDate) {
    this.lastActivityDate = lastActivityDate;
  }

  public long getCreationDate() {
    return this.creationDate;
  }

  public void setCreationDate(long creationDate) {
    this.creationDate = creationDate;
  }

  public long getEditDate() {
    return this.editDate;
  }

  public void setEditDate(long editDate) {
    this.editDate = editDate;
  }

  public long getQuestionId() {
    return this.questionId;
  }

  public void setQuestionId(long questionId) {
    this.questionId = questionId;
  }

  public String getLink() {
    return this.link;
  }

  public void setLink(String link) {
    this.link = link;
  }

  public String getCloseReason() {
    return this.closeReason;
  }

  public void setCloseReason(String closeReason) {
    this.closeReason = closeReason;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Owner getOwner() {
    return owner;
  }

  public void setOwner(Owner owner) {
    this.owner = owner;
  }
}

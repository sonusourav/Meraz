package com.sonusourav.merazoverflow.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Owner implements Serializable {

  @SerializedName("display_name")
  private String name;
  @SerializedName("reputation")
  private int reputation;
  @SerializedName("user_id")
  private long userId;
  @SerializedName("user_type")
  private String userType;
  @SerializedName("accept_rate")
  private int acceptRate;
  @SerializedName("profile_image")
  private String ImageUrl;
  @SerializedName("link")
  private String userIdlink;
}

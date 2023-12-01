package com.shuchi.springboot.demo.mycoolapp.rest;

public class PostAuthorization {
private String token;
private String issuedAt;
public String getToken() {
    return token;
}
public void setToken(String token) {
    this.token = token;
}
public String getIssuedAt() {
    return issuedAt;
}
public void setIssuedAt(String issuedAt) {
    this.issuedAt = issuedAt;
}

}

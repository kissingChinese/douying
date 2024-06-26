package com.example.entity.xunfei;

import lombok.Data;

import java.io.Serializable;

@Data
public class RoleContent implements Serializable {
    String role; //角色
    String content; //内容
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}

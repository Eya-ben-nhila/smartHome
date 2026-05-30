package com.smarthome.dto.admin;

import com.smarthome.entity.Role;
import com.smarthome.entity.User;

public class AdminUserResponse {
    private String id;
    private String fullName;
    private String email;
    private Role role;
    private boolean enabled;
    private String profileImageUrl;

    public static AdminUserResponse from(User user) {
        AdminUserResponse response = new AdminUserResponse();
        response.id = user.getId();
        response.fullName = user.getFullName();
        response.email = user.getEmail();
        response.role = user.getRole();
        response.enabled = user.isActive();
        response.profileImageUrl = user.getProfileImageUrl();
        return response;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}

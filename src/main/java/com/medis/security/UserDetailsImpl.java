package com.medis.security;

import com.medis.model.user.UserModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String uuid;

    private String nama;

    private String username;

    private String photoUrl;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String uuid, String nama, String email, String password, String photoUrl, List<GrantedAuthority> authorities) {
        this.uuid = uuid;
        this.nama = nama;
        this.username = email;
        this.password = password;
        this.authorities = authorities;
        this.photoUrl = photoUrl;
    }

    public static UserDetailsImpl build(UserModel userModel) {
        List<String> roles = new ArrayList<>();
        roles.add(userModel.getRole());
        List<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                userModel.getUuid(),
                userModel.getNama(),
                userModel.getEmail(),
                userModel.getPassword(),
                userModel.getPhotoUrl(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getUuid(){
        return uuid;
    }

    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getNama() {
        return nama;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(uuid, user.uuid);
    }

}


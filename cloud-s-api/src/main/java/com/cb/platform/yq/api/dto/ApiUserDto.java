package com.cb.platform.yq.api.dto;

import com.cb.platform.yq.api.entity.ApiUserDo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;

import java.util.Collection;

/**
 * 用户资料 继承了用户资料   和  认证接口
 */
public class ApiUserDto extends ApiUserDo implements UserDetails{


    public ApiUserDto(){

    }
    public ApiUserDto(ApiUserDo apiUserDo){
        super(apiUserDo.getId(),apiUserDo.getKeyId(),apiUserDo.getAppId(),apiUserDo.getCreateTime(),apiUserDo.getUpdateTime(),apiUserDo.getUpdateUser());
    }
    /**
     * 返回授予用户的权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder().encode(this.getKeyId());
    }

    @Override
    public String getUsername() {
        return super.getId();
    }

    /**
     * 返回账户是否已经过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 返回账户是否锁住
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示用户的凭据（密码）是否已过期。
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 指示用户是否已启用或禁用。
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}

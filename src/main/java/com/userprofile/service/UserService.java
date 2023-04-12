package com.userprofile.service;

import java.util.List;

import com.userprofile.entity.UserInfo;

public interface UserService  {

	UserInfo register(UserInfo user);
    UserInfo getUser(String id);
    List<UserInfo> getAllUser();
    

   

}

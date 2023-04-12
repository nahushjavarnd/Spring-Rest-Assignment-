package com.userprofile.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.userprofile.entity.UserInfo;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, String>  {
	  Optional<UserInfo> findByName(String username);
	

}

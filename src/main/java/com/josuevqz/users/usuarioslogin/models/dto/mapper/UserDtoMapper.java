package com.josuevqz.users.usuarioslogin.models.dto.mapper;

import com.josuevqz.users.usuarioslogin.models.User;
import com.josuevqz.users.usuarioslogin.models.dto.UserDTO;

public class UserDtoMapper {


    private User user;
    private UserDtoMapper() {
    }

    public  static  UserDtoMapper builder(){
        return  new UserDtoMapper();

    }

    public UserDtoMapper setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDTO build(){
        if (user == null){
           throw  new RuntimeException("Debe pasar el entity user!");
        }
        return  new UserDTO(this.user.getId(), user.getUsername(),user.getEmail());

    }


}

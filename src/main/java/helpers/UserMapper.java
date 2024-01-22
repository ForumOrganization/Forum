package helpers;

import models.User;
import models.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import services.UserService;

@Component
public class UserMapper {
    private final UserService userService;

    @Autowired
    public UserMapper(UserService userService) {
        this.userService = userService;
    }

    public User fromDto(int id, UserDto dto) {
        User user = fromDto(dto);
        user.setId(id);
        User repostioryUser = userService.getById(id);
        user.setFirstName(repostioryUser.getFirstName());
        user.setLastName(repostioryUser.getLastName());
        user.setEmail(repostioryUser.getEmail());
        user.setPassword(repostioryUser.getPassword());

        return user;
    }

    public User fromDto(UserDto dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());

        return user;
    }
}
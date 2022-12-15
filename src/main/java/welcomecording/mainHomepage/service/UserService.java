package welcomecording.mainHomepage.service;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import welcomecording.mainHomepage.model.Role;
import welcomecording.mainHomepage.model.User;
import welcomecording.mainHomepage.repository.UserRepository;


// 구현 할 기능 : checkNickname, putNicknamae, updateMyInfo, UpdateIsNewUser, changeProfile
// checkNickname은
@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void join(User user) {
        user.setRole(Role.USER);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }



}

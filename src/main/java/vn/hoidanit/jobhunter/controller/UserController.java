package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // fetch user by id
    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id)
            throws IdInvalidException {
        User searchUser = this.userService.fetchUserById(id);
        if (searchUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(searchUser));
        // return ResponseEntity.ok(searchUser); cach viet ngan gon hon dung ham ok()
    }

    // fetch all user
    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
    }

    // create new user
    @PostMapping("/users")
    @ApiMessage("Create successfully a new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postmanUser)
            throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(postmanUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException(
                    "Email " + postmanUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác!");
        }
        String hashPassword = this.passwordEncoder.encode(postmanUser.getPassword());
        postmanUser.setPassword(hashPassword);
        User tempUser = this.userService.handleCreateUser(postmanUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(tempUser));
    }

    // delete user
    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete successfully a user")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User với id = " + id + " không tồn tại!");
        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body("delete user with id= " + id + " sucessfully!!");
    }

    // update user
    @PutMapping("/users")
    @ApiMessage("update successfully a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user)
            throws IdInvalidException {
        User upUser = this.userService.handleUpdateUser(user);
        if (upUser == null) {
            throw new IdInvalidException("User với id= " + user.getId() + " không tồn tại!");
        }
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUpdateUserDTO(upUser));
    }
}

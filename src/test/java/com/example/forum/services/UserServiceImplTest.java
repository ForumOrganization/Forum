package com.example.forum.services;

import com.example.forum.exceptions.DuplicateEntityException;
import com.example.forum.exceptions.EntityAlreadyDeleteException;
import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Post;
import com.example.forum.models.User;
import com.example.forum.models.enums.Role;
import com.example.forum.models.enums.Status;
import com.example.forum.repositories.contracts.UserRepository;
import com.example.forum.utils.UserFilterOptions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.example.forum.helpers.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository mockRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void get_Should_CallRepository() {
        UserFilterOptions mockUserFilterOptions = createMockUserFilterOptions();

        userService.getAll(mockUserFilterOptions);

        Mockito.verify(mockRepository, Mockito.times(1))
                .getAll(mockUserFilterOptions);
    }

    @Test
    public void getUserById_Should_ReturnUser_When_MatchExists() {
        User user = createMockUser();

        Mockito.when(mockRepository.getById(user.getId())).thenReturn(user);

        userService.getById(user.getId());

        Mockito.verify(mockRepository, Mockito.times(1)).getById(user.getId());
    }

    @Test
    void getUserByUsername_Should_ReturnUser_When_MatchExists() {
        String username = "testUser";
        User user = new User();

        Mockito.when(mockRepository.getByUsername(username)).thenReturn(user);

        userService.getByUsername(username);

        Mockito.verify(mockRepository, Mockito.times(1)).getByUsername(username);
    }

    @Test
    void getUserByEmail_Should_ReturnUser_When_MatchExists() {
        String email = "test@example.com";
        User user = new User();

        Mockito.when(mockRepository.getByEmail(email)).thenReturn(user);

        userService.getByEmail(email);

        Mockito.verify(mockRepository, Mockito.times(1)).getByEmail(email);
    }

    @Test
    void getUserByFirstName_Should_ReturnUser_When_MatchExists() {
        String firstName = "John";

        Mockito.when(mockRepository.getByFirstName(firstName)).thenReturn(Collections.emptyList());

        userService.getByFirstName(firstName);

        Mockito.verify(mockRepository, Mockito.times(1)).getByFirstName(firstName);
    }

    @Test
    void getUserByCommentId_Should_ReturnUser_When_MatchExists() {
        int commentId = 1;
        User user = createMockUser();

        Mockito.when(mockRepository.getUserByComment(commentId)).thenReturn(user);

        userService.getUserByComment(commentId);

        Mockito.verify(mockRepository, Mockito.times(1)).getUserByComment(commentId);
    }

    @Test
    void getPostsByUserId_Should_ReturnPosts_When_MatchExists() {
        int userId = 1;
        Post post = createMockPost();

        Mockito.when(mockRepository.getPosts(userId)).thenReturn(Collections.singletonList(post));

        userService.getPosts(userId);

        Mockito.verify(mockRepository, Mockito.times(1)).getPosts(userId);
    }

    @Test
    public void registerUser_Should_ThrowException_When_UserWithSameNameExists() {
        User user = createMockUser();
        User existingUserWithTheSameName = createMockUser();
        existingUserWithTheSameName.setId(2);

        Mockito.when(mockRepository.getByUsernameFindUser(user.getUsername()))
                .thenReturn(existingUserWithTheSameName);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.registerUser(user));
    }

    @Test
    public void registerUser_Should_ThrowException_When_UserWithSameEmailExists() {
        User user = createMockUser();
        User existingUserWithTheSameName = createMockUser();
        existingUserWithTheSameName.setId(2);

        Mockito.when(mockRepository.getByEmailFindUser(user.getEmail()))
                .thenReturn(existingUserWithTheSameName);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.registerUser(user));
    }

    @Test
    public void registerUser_Should_CallRepository_When_UserWithSameNameDoesNotExist() {
        User user = createMockUser();

        Mockito.when(mockRepository.getByUsernameFindUser(user.getUsername()))
                .thenReturn(null);

        userService.registerUser(user);

        Mockito.verify(mockRepository, Mockito.times(1))
                .registerUser(user);
    }

    @Test
    public void registerUser_Should_CreateUserAsAdmin_When_DatabaseIsEmpty() {
        User user = createMockUser();

        Mockito.when(mockRepository.isDataBaseEmpty()).thenReturn(true);

        userService.registerUser(user);

        Assertions.assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    public void registerUser_Should_ReactivateUser_When_IsSameUser() {
        User user = createMockUser();
        User existingUser = createMockUser();
        existingUser.setDeleted(true);

        Mockito.when(mockRepository.getByUsernameFindUser(existingUser.getUsername()))
                .thenReturn(existingUser);
        userService.registerUser(user);

        Mockito.verify(mockRepository, Mockito.times(1))
                .reactivated(user);
    }

    @Test
    public void update_Should_CallRepository_When_UpdatingExistingUser() {
        User targetUser = createMockUser();
        User executingUser = createMockUser();

        userService.updateUser(targetUser, executingUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .updateUser(targetUser);
    }

    @Test
    public void update_Should_ThrowException_When_UsernameIsTaken() {
        User targetUser = createMockUser();
        User executingUser = createMockUser();
        executingUser.setUsername("DifferentUsername");

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.updateUser(targetUser, executingUser));
    }

    @Test
    public void update_Should_ThrowException_When_EmailIsTaken() {
        User targetUser = createMockUser();
        User executingUser = createMockUser();
        executingUser.setEmail("mock2@user.com");

        Mockito.when(mockRepository.getByEmailFindUser(targetUser.getEmail())).
                thenReturn(createMockUser());

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.updateUser(targetUser, executingUser));
    }

    @Test
    public void delete_Should_CallRepository_When_UserExist() {
        int userIdToDelete = 1;
        User executingUser = createMockUser();
        User userToDelete = createMockUser();
        Mockito.when(mockRepository.getById(userIdToDelete))
                .thenReturn(userToDelete);

        userService.deleteUser(userIdToDelete, executingUser);

        Mockito.verify(mockRepository, Mockito.times(1)).
                deleteUser(userIdToDelete);
    }

    @Test
    public void delete_Should_ThrowException_When_UserAlreadyDeleted() {
        int userIdToDelete = 1;
        User executingUser = createMockUser();
        User userToDelete = createMockUser();
        userToDelete.setDeleted(true);

        Mockito.when(mockRepository.getById(userIdToDelete)).
                thenReturn(userToDelete);

        Assertions.assertThrows(
                EntityAlreadyDeleteException.class,
                () -> userService.deleteUser(userIdToDelete, executingUser));
    }

    @Test
    void updateToAdmin_Should_CallRepository_When_UpdatingExistingUser() {
        User targetUser = createMockUser();
        targetUser.setRole(Role.USER);
        User executingUser = createMockUser();

        userService.updateToAdmin(targetUser, executingUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .updateUser(targetUser);
    }

    @Test
    void updateToAdmin_Should_ThrowException_When_TargetUserIsAlreadyAdmin() {
        User targetUser = createMockUser();
        User executingUser = createMockUser();

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.updateToAdmin(targetUser, executingUser));
    }

    @Test
    void blockUser_Should_CallRepository_When_BlockUserExist() {
        User admin = createMockUser();
        User blockUser = createMockUser();

        userService.blockUser(admin, blockUser);

        Mockito.verify(mockRepository, Mockito.times(1)).
                updateUser(blockUser);
    }

    @Test
    void blockUser_Should_ThrowException_When_UserAlreadyBlocked() {
        User admin = createMockUser();
        User blockUser = createMockUser();
        blockUser.setStatus(Status.BLOCKED);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.blockUser(admin, blockUser));
    }

    @Test
    void unblockUser_Should_CallRepository_When_UnBlockUserExist() {
        User admin = createMockUser();
        User unBlockUser = createMockUser();
        unBlockUser.setStatus(Status.BLOCKED);

        userService.unBlockUser(admin, unBlockUser);

        Mockito.verify(mockRepository, Mockito.times(1))
                .updateUser(unBlockUser);
    }

    @Test
    void unblockUser_Should_ThrowException_When_UserAlreadyUnBlocked() {
        User admin = createMockUser();
        User blockUser = createMockUser();

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.unBlockUser(admin, blockUser));
    }

    @Test
    void addPhoneNumberToAdmin_Should_CallRepository_When_PhoneNumberExist() {
        User admin = createMockUser();
        User userPhoneNumberToBeUpdate = createMockUser();
        userPhoneNumberToBeUpdate.setPhoneNumber("123456789");

        Mockito.when(mockRepository.existsByPhoneNumber(userPhoneNumberToBeUpdate))
                .thenReturn(false);

        userService.addPhoneNumberToAdmin(admin, userPhoneNumberToBeUpdate);

        Mockito.verify(mockRepository, Mockito.times(1)).
                updateUser(admin);
    }

    @Test
    void addPhoneNumberToAdmin_Should_ThrowException_When_PhoneNumberToAdminIsDuplicate() {
        User admin = createMockUser();
        User userPhoneNumberToBeUpdate = createMockUser();
        userPhoneNumberToBeUpdate.setPhoneNumber("123456789");

        Mockito.when(mockRepository.existsByPhoneNumber(userPhoneNumberToBeUpdate))
                .thenReturn(true);

        Assertions.assertThrows(
                DuplicateEntityException.class,
                () -> userService.addPhoneNumberToAdmin(admin, userPhoneNumberToBeUpdate));
    }

    @Test
    void addPhoneNumberToAdmin_Should_ThrowException_When_PhoneNumberIsMissing() {
        User admin = createMockUser();
        User userPhoneNumberToBeUpdate = createMockUser();
        userPhoneNumberToBeUpdate.setPhoneNumber(null);

        Assertions.assertThrows(
                EntityNotFoundException.class,
                () -> userService.addPhoneNumberToAdmin(admin, userPhoneNumberToBeUpdate));
    }

    @Test
    void deletePhoneNumber_Should_CallRepository_When_PhoneNumberExist() {
        User user = createMockUser();
        User userToDDelete = createMockUser();

        Mockito.when(mockRepository.getById(userToDDelete.getId()))
                .thenReturn(userToDDelete);

        userService.deletePhoneNumber(userToDDelete.getId(), user);

        Mockito.verify(mockRepository, Mockito.times(1))
                .updateUser(user);
    }
}


package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto create(UserDto userDto) {
        validateUserForCreate(userDto);

        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto update(Long userId, UserDto userDto) {
        User user = getUserOrThrow(userId);

        validateUserForUpdate(userDto, userId);

        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }

        return UserMapper.toUserDto(userRepository.save(user));
    }

    @Override
    public UserDto getById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("User not found"));

        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с id=" + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new NotFoundException("Пользователь с id=" + userId + " не найден"));
    }

    private void validateUserForCreate(UserDto dto) {
        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BadRequestException("Email не может быть пустым");
        }

        if (!dto.getEmail().contains("@")) {
            throw new BadRequestException("Некорректный email");
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new ConflictException("Email уже используется");
        }
    }

    private void validateUserForUpdate(UserDto dto, Long currentUserId) {

        if (dto.getEmail() == null) {
            return;
        }

        if (dto.getEmail().isBlank()) {
            throw new BadRequestException("Email не может быть пустым");
        }

        if (!dto.getEmail().contains("@")) {
            throw new BadRequestException("Некорректный email");
        }

        boolean emailExists = userRepository.findAll().stream()
                .anyMatch(user ->
                        user.getEmail().equals(dto.getEmail()) &&
                                !user.getId().equals(currentUserId)
                );

        if (emailExists) {
            throw new ConflictException("Email уже используется");
        }
    }
}
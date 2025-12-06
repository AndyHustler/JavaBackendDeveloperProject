package ru.project.jbd.security.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


@Schema(description = "Запрос на регистрацию")
public record SignupRequest (
    
    @Schema(description = "Имя пользователя", example = "Use User")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    String username,
 
    @Schema(description = "Адрес электронной почты", example = "user@greenatom.ru")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com")
    String email,

    @Schema(description = "Роль пользователя", example = "user")
    @Size(min = 4, max = 5, message = "Доступно только три роли: user, admin, guest")
    String role,
    
    @Schema(description = "Пароль", example = "useruser")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    String password
){}

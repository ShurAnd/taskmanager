package org.andrey.taskmanager.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String email = "";
    private String firstName = "";
    private String lastName = "";
    private String password = "";
}

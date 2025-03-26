package org.andrey.taskmanager.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUser {
    private String username = "";
    private String firstName = "";
    private String lastName = "";
}

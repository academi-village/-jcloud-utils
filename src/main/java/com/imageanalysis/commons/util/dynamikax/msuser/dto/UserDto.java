package com.imageanalysis.commons.util.dynamikax.msuser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.imageanalysis.commons.util.dynamikax.security.UserDetails;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserDto implements UserDetails {

    @Getter
    @EqualsAndHashCode.Include
    public Long id;

    @Getter
    @JsonProperty("userName")
    public String  username;
    public String  firstName;
    public String  lastName;
    public String  institution;
    public String  postalAddress;
    public String  emailAddress;
    public String  phone;
    public Boolean status;
    public Instant passwordUpdatedDate;
    public Long    loginAttempts;
    public Boolean twoStepAuth;
    public Instant lastLogin;
    public Instant previousLogin;

    @JsonIgnore
    public String getFullName() {
        return firstName + " " + lastName;
    }
}

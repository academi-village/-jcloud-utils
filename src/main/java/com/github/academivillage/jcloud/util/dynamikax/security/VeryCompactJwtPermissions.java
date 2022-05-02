package com.github.academivillage.jcloud.util.dynamikax.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * Encapsulates the JWT permissions in an encoded (compact) format to reduce the JWT token size.
 *
 * @author Younes Rahimi
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class VeryCompactJwtPermissions {

    /**
     * Used to encode/decode numbers to compact them.
     */
    protected static final AlphabetEncoder ALPHABET_ENCODER = new AlphabetEncoder();

    /**
     * Encapsulate the encoded admin permissions.
     * <br>
     * Example of serialized key-value: <br>
     * {@code {"glb":"V0;V1;t5;U7;upload.create.patient"}}
     */
    @JsonProperty("glb")
    protected String globals;

    /**
     * Encapsulate the encoded permissions per each project.
     * <br>
     * Example of serialized key-value: <br>
     * {@code {"prm": "F52|Jfn~60;N5;q1;upload.create.patient|JjA|JlX|K0v|K41|KGI~K;LYD;KbR;60;LbD;LQD|I27"}}
     */
    @JsonProperty("vcp")
    protected String permissions;

    public VeryCompactJwtPermissions(Map<String, String> map) {
        this.globals     = map.get("globals");
        this.permissions = map.get("activities");
    }

    /**
     * Decodes the encoded permissions of JWT token.
     *
     * @return A map containing the keys {@code "globals"} and {@code "activities"} which represents the decoded permissions of the user projects.
     */
    public Map<String, Object> decodeToMap() {
        List<String>              globals    = decodeGlobal();
        List<Map<String, Object>> activities = decodePermissionsToMap();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("globals", globals);
        result.put("activities", activities);

        return result;
    }

    /**
     * Decodes all admin encoded permissions.
     *
     * @return The list of the admin decoded permissions.
     */
    protected List<String> decodeGlobal() {
        return Arrays.stream(globals.split(";")).map(this::decodePermission).collect(toList());
    }

    /**
     * Decodes an encoded permission.
     *
     * @param encoded Represents the encoded permission. Examples: {@code "q1"}, {@code "KbR"}, {@code "upload.create.patient"}
     * @return the Decoded permission. Example: {@code "qc.reading.all"}
     */
    protected String decodePermission(String encoded) {
        return Permission.ofCode(encoded).map(Permission::getName).orElse(encoded);
    }

    /**
     * Decodes the encoded permissions of multiple projects.
     *
     * @return A list of multiple maps containing the keys {@code "projectId"} and {@code "activities"} which represents the decoded permissions of the project.
     */
    private List<Map<String, Object>> decodePermissionsToMap() {
        return Arrays.stream(permissions.split("\\|"))
                .map(this::decodePIdAndPermissionsToMap)
                .collect(toList());
    }

    /**
     * Decodes the encoded permissions of a project.
     *
     * @param encodedPerms Represents the encoded project ID and the permissions of that project.
     *                     Example: {@code "KGI~Sa;LYD;KbR;60;LbD;upload.create.patient"}
     * @return A map containing the keys {@code "projectId"} and {@code "activities"} which represents the decoded permissions of the project. Example: {@code {"project": 1643, "activities":["qc.reading.all","qc.images.update"]}}
     */
    private Map<String, Object> decodePIdAndPermissionsToMap(String encodedPerms) {
        String[]            split       = encodedPerms.split("~");
        Map<String, Object> jwtActivity = new LinkedHashMap<>();
        jwtActivity.put("projectId", ALPHABET_ENCODER.decode(split[0]));

        if (split.length == 1) {
            jwtActivity.put("activities", Collections.emptyList());
            return jwtActivity;
        }

        List<String> acts = Arrays.stream(split[1].split(";")).map(this::decodePermission).collect(toList());
        jwtActivity.put("activities", acts);

        return jwtActivity;
    }
}

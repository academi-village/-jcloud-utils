package com.imageanalysis.commons.util.dynamikax.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * Encapsulates the JWT permissions in an encoded (compact) format to reduce the JWT token size.
 *
 * @author Younes Rahimi
 */
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CompactJwtPermissions {

    /**
     * Used to encode/decode numbers to compact them.
     */
    protected static final AlphabetEncoder ALPHABET_ENCODER = new AlphabetEncoder();

    /**
     * Represents the dictionary of tokens used in permissions.
     * This will be used to decode the encoded values of the {@link #globals} and {@link #permissions}.
     * <br>
     * Example of serialized key-value:<br>  {@code {"dic": "access;all;batch;batches;central;clinicaltrial;configuration;create;create-edit;dashboard;data;discontinued;eDCF;eDTF;list;or;overall;patient;projects;qc;queries;read-details;reader;reading;readings;resolve;results;select;site;study;update;upload;uploaded;view;viewer;visit"}}
     */
    @JsonProperty("dic")
    protected String tokens;

    /**
     * Encapsulate the encoded admin permissions.
     * <br>
     * Example of serialized key-value: <br>
     * {@code {"glb":"V0;V1;t5;U7"}}
     */
    @JsonProperty("glb")
    protected String globals;

    /**
     * Encapsulate the encoded permissions per each project.
     * <br>
     * Example of serialized key-value: <br>
     * {@code {"perms": "F52|Jfn~60;N5;q1.7.U.Hc|JjA|JlX|K0v|K41|KGI~K.Sa.d;LYD;KbR;60;LbD;LQD|I27"}}
     */
    @JsonProperty("perms")
    protected String permissions;

    public CompactJwtPermissions(Map<String, String> map) {
        this.tokens      = map.get("tokens");
        this.globals     = map.get("globals");
        this.permissions = map.get("activities");
    }

    /**
     * Decodes the encoded permissions of JWT token.
     *
     * @return A map containing the keys {@code "globals"} and {@code "activities"} which represents the decoded permissions of the user projects.
     */
    public Map<String, Object> decodeToMap() {
        String[] tokens = this.tokens.split(";");
        Map<String, String> codeToTokenMap = IntStream.range(0, tokens.length)
                .boxed()
                .collect(toMap(i -> ALPHABET_ENCODER.encode(i), i -> tokens[i]));

        List<String>              globals    = decodeGlobal(codeToTokenMap);
        List<Map<String, Object>> activities = decodePermissionsToMap(codeToTokenMap);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("globals", globals);
        result.put("activities", activities);

        return result;
    }

    /**
     * Decodes all admin encoded permissions.
     *
     * @param codeToTokenMap A map from encoded representation of a token to the token itself. Example: {@code {"K":"qc", "b": "reading", "R": "all"}}
     * @return The list of the admin decoded permissions.
     */
    protected List<String> decodeGlobal(Map<String, String> codeToTokenMap) {
        return Arrays.stream(globals.split(";"))
                .map(perm -> decodePermission(perm, codeToTokenMap))
                .collect(toList());
    }

    /**
     * Decodes an encoded permission.
     *
     * @param encoded        Represents the encoded permission. Examples: {@code "q1.7.U.Hc"}, {@code "KbR"}
     *                       If all tokens are encoded to one character the dots are dropped. For example {@code "KbR"} is equal to {@code "K.b.R"}
     * @param codeToTokenMap A map from encoded representation of a token to the token itself. Example: {@code {"K":"qc", "b": "reading", "R": "all"}}
     * @return the Decoded permission. Example: {@code "qc.reading.all"}
     */
    protected String decodePermission(String encoded, Map<String, String> codeToTokenMap) {
        String[] codes = encoded.contains(".")
                         ? encoded.split("\\.")
                         : encoded.chars().mapToObj(ch -> Character.toString((char) ch)).toArray(String[]::new);
        return Arrays.stream(codes).map(codeToTokenMap::get).collect(joining("."));
    }

    /**
     * Decodes the encoded permissions of multiple projects.
     *
     * @param codeToTokenMap A map from encoded representation of a token to the token itself. Example: {@code {"K":"qc", "b": "reading", "R": "all"}}
     * @return A list of multiple maps containing the keys {@code "projectId"} and {@code "activities"} which represents the decoded permissions of the project.
     */
    private List<Map<String, Object>> decodePermissionsToMap(Map<String, String> codeToTokenMap) {
        return Arrays.stream(permissions.split("\\|"))
                .map(perm -> decodePIdAndPermissionsToMap(perm, codeToTokenMap))
                .collect(toList());
    }

    /**
     * Decodes the encoded permissions of a project.
     *
     * @param encodedPerms   Represents the encoded project ID and the permissions of that project.
     *                       Example: {@code "KGI~K.Sa.d;LYD;KbR;60;LbD;LQD"}
     * @param codeToTokenMap A map from encoded representation of a token to the token itself. Example: {@code {"K":"qc", "b": "reading", "R": "all"}}
     * @return A map containing the keys {@code "projectId"} and {@code "activities"} which represents the decoded permissions of the project. Example: {@code {"project": 1643, "activities":["qc.reading.all","qc.images.update"]}}
     */
    private Map<String, Object> decodePIdAndPermissionsToMap(String encodedPerms, Map<String, String> codeToTokenMap) {
        String[]            split       = encodedPerms.split("~");
        Map<String, Object> jwtActivity = new LinkedHashMap<>();
        jwtActivity.put("projectId", ALPHABET_ENCODER.decode(split[0]));

        if (split.length == 1) {
            jwtActivity.put("activities", Collections.emptyList());
            return jwtActivity;
        }

        List<String> acts = Arrays.stream(split[1].split(";"))
                .map(perm -> decodePermission(perm, codeToTokenMap))
                .collect(toList());
        jwtActivity.put("activities", acts);

        return jwtActivity;
    }
}

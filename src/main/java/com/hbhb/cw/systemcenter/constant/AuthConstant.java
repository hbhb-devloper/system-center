package com.hbhb.cw.systemcenter.constant;

/**
 * @author xiaokang
 * @since 2020-10-07
 */
public enum AuthConstant {
    /**
     * JWT存储权限前缀
     */
    AUTHORITY_PREFIX("ROLE_"),
    /**
     * JWT存储权限属性
     */
    AUTHORITY_CLAIM_NAME("authorities"),
    /**
     * 认证信息Http请求头
     */
    JWT_TOKEN_HEADER("Authorization"),
    /**
     * JWT令牌前缀
     */
    JWT_TOKEN_PREFIX("Bearer"),
    /**
     * JWT载体key
     */
    JWT_PAYLOAD_KEY("payload"),
    /**
     * Redis缓存权限规则key
     */
    RESOURCE_ROLES_KEY("auth:resourceRoles"),

    TOKEN_BLACKLIST_PREFIX("auth:token:blacklist:"),

    CLIENT_DETAILS_FIELDS("client_id, CONCAT('{noop}',client_secret) as client_secret, resource_ids, scope, "
            + "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
            + "refresh_token_validity, additional_information, autoapprove"),

    BASE_CLIENT_DETAILS_SQL("select " + CLIENT_DETAILS_FIELDS.value() + " from oauth_client_details"),

    FIND_CLIENT_DETAILS_SQL(BASE_CLIENT_DETAILS_SQL.value() + " order by client_id"),

    SELECT_CLIENT_DETAILS_SQL(BASE_CLIENT_DETAILS_SQL.value() + " where client_id = ?"),

    BCRYPT("{bcrypt}"),

    JWT_USER_ID_KEY("id"),

    JWT_CLIENT_ID_KEY("client_id"),
    ;

    private final String value;

    AuthConstant(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}

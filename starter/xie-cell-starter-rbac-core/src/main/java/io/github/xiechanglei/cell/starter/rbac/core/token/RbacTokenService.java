package io.github.xiechanglei.cell.starter.rbac.core.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.xiechanglei.cell.common.lang.secret.AESHelper;
import io.github.xiechanglei.cell.starter.rbac.core.config.RbacBaseConfigProperties;
import io.github.xiechanglei.cell.starter.rbac.core.entity.RbacUser;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfo;
import io.github.xiechanglei.cell.starter.rbac.core.provide.RbacTokenInfoBuilder;
import io.github.xiechanglei.cell.starter.web.utils.RequestHandler;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Token信息管理器类，用于管理和构建Token信息。
 * <p>
 * 该类负责加载TokenInfo的实现，并提供构建Token信息的方法。
 * 如果没有找到自定义的TokenInfo实现，则使用默认的DefaultTokenInfo实现。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class RbacTokenService {

    private final String REQUEST_TOKEN_ATTR_NAME = "tools-rbac-token";

    private final RbacBaseConfigProperties rbacBaseConfigProperties;

    /**
     * ObjectMapper用于将对象转换为JSON字符串和从JSON字符串反序列化对象
     */
    private final ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 存储TokenInfo的实例
     */
    @Autowired(required = false)
    private RbacTokenInfoBuilder rbacTokenInfoBuilder;

    @PostConstruct
    public void init() {
        if (rbacTokenInfoBuilder == null) {
            rbacTokenInfoBuilder = new DefaultTokenInfoBuilder();
        }
    }

    public Optional<RbacTokenInfo> getCurrentTokenInfo() {
        return Optional.ofNullable((RbacTokenInfo) RequestHandler.getCurrentRequest().getAttribute(REQUEST_TOKEN_ATTR_NAME));
    }

    public void setCurrentTokenInfo(RbacTokenInfo tokenInfo) {
        RequestHandler.getCurrentRequest().setAttribute(REQUEST_TOKEN_ATTR_NAME, tokenInfo);
    }

    /**
     * 构建一个 serial 模式的Token信息对象，并将其编码为字符串。
     */
    public String buildSerialTokenInfo(RbacUser user) {
        return encode(rbacTokenInfoBuilder.createWithSerial(user));
    }

    /**
     * 构建一个 feature 模式的Token信息对象，并将其编码为字符串。
     */
    public String buildFeatureTokenInfo(RbacUser user) {
        return encode(rbacTokenInfoBuilder.createWithFeature(user));
    }

    /**
     * 将 `TokenInfo` 对象编码为加密的字符串。
     * <p>
     * 该方法将 `TokenInfo` 对象序列化为JSON格式的字符串，然后使用AES加密算法对其进行加密。
     * </p>
     *
     * @param token 要编码的 `TokenInfo` 对象。
     * @return 加密后的字符串。
     * @throws RuntimeException 如果在序列化或加密过程中发生错误。
     */
    private String encode(RbacTokenInfo token) {
        try {
            return AESHelper.encode(objectMapper.writeValueAsString(token), rbacBaseConfigProperties.getTokenSecret(), rbacBaseConfigProperties.getTokenIv());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解码并解密一个加密的字符串，返回 `TokenInfo` 对象。
     * <p>
     * 该方法首先使用AES解密算法解密字符串，然后将解密后的JSON字符串反序列化为 `TokenInfo` 对象。
     * </p>
     *
     * @param token 加密的字符串。
     * @return 解密后的 `TokenInfo` 对象；如果解密或反序列化失败，则返回 `null`。
     */
    public RbacTokenInfo decode(String token) {
        if (token == null) {
            return null;
        }
        try {
            return objectMapper.readValue(AESHelper.decode(token, rbacBaseConfigProperties.getTokenSecret(), rbacBaseConfigProperties.getTokenIv()), rbacTokenInfoBuilder.getTokenClass());
        } catch (Exception e) {
            return null;
        }
    }
}

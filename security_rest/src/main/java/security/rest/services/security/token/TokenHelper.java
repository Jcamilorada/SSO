package security.rest.services.security.token;

import com.google.common.base.Preconditions;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *  Token helper, enable security token creation and retrieval.
 *
 * @author Juan Rada
 */
@Component
class TokenHelper
{
    private static final String TOKEN_SEPARATOR = ".";
    private static final String SECURITY_ALGORITHM = "HmacSHA256";
    private final Mac mac;

    static final String TOKEN_SPLITTER = "\\.";

    @Autowired
    TokenHelper(final @Value("${salt}") byte[] salt)
    {
        Preconditions.checkNotNull(salt);

        try
        {
            mac = Mac.getInstance(SECURITY_ALGORITHM);
            mac.init(new SecretKeySpec(salt, SECURITY_ALGORITHM));
        }

        catch (NoSuchAlgorithmException | InvalidKeyException ex)
        {
            throw new IllegalStateException(ex);
        }
    }

    public String getTokenFromTokenId(final String tokenId)
    {
        Preconditions.checkNotNull(tokenId);

        byte[] bytesTokenId = tokenId.getBytes();
        byte[] hash = getHmac(bytesTokenId);

        return toBase64(bytesTokenId) + TOKEN_SEPARATOR + toBase64(hash);
    }

    public Optional<String> getTokenIdFromToken(final String token)
    {
        Preconditions.checkNotNull(token);

        String[] parts = token.split(TOKEN_SPLITTER);
        Optional<String> tokenId = Optional.empty();

        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0)
        {
            byte[] tokenIdPart = fromBase64(parts[0]);
            byte[] bytesHash = fromBase64(parts[1]);
            boolean isValidHash = Arrays.equals(getHmac(tokenIdPart), bytesHash);
            if (isValidHash)
            {
                tokenId = tokenId.of(new String(tokenIdPart));
            }
        }

        return tokenId;
    }


    private byte[] getHmac(final byte[] data)
    {
        return mac.doFinal(data);
    }

    private String toBase64(final byte[] data)
    {
        return DatatypeConverter.printBase64Binary(data);
    }

    private byte[] fromBase64(final String data)
    {
        return DatatypeConverter.parseBase64Binary(data);
    }
}


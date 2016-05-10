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
    private final Mac algoritmoMac;

    static final String TOKEN_SPLITTER = "\\.";

    @Autowired
    TokenHelper(final @Value("${salt}") byte[] salt)
    {
        Preconditions.checkNotNull(salt);

        try
        {
            algoritmoMac = Mac.getInstance(SECURITY_ALGORITHM);
            algoritmoMac.init(new SecretKeySpec(salt, SECURITY_ALGORITHM));
        }

        catch (NoSuchAlgorithmException | InvalidKeyException ex)
        {
            throw new IllegalStateException(ex);
        }
    }

    public String getTokenFromTokenId(final String tokenId)
    {
        Preconditions.checkNotNull(tokenId);

        byte[] bytesUsuario = tokenId.getBytes();
        byte[] hash = crearHmac(bytesUsuario);

        return convertirABase64(bytesUsuario) + TOKEN_SEPARATOR + convertirABase64(hash);
    }

    public Optional<String> getTokenIdFromToken(final String token)
    {
        Preconditions.checkNotNull(token);

        String[] parts = token.split(TOKEN_SPLITTER);
        Optional<String> usuario = Optional.empty();

        if (parts.length == 2 && parts[0].length() > 0 && parts[1].length() > 0)
        {
            byte[] bytesUsuario = convertirDeBase64(parts[0]);
            byte[] bytesHash = convertirDeBase64(parts[1]);
            boolean hashValido = Arrays.equals(crearHmac(bytesUsuario), bytesHash);
            if (hashValido)
            {
                usuario = usuario.of(new String(bytesUsuario));
            }
        }

        return usuario;
    }


    private byte[] crearHmac(final byte[] datos)
    {
        return algoritmoMac.doFinal(datos);
    }

    private String convertirABase64(final byte[] datos)
    {
        return DatatypeConverter.printBase64Binary(datos);
    }

    private byte[] convertirDeBase64(final String datos)
    {
        return DatatypeConverter.parseBase64Binary(datos);
    }
}


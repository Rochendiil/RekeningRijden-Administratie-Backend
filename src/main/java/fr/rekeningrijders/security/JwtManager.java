package fr.rekeningrijders.security;


import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ken
 */
@ApplicationScoped
public class JwtManager
{
    static
    {
        char[] p4ssw0rd = "secret".toCharArray();
        String alias = "alias";
        PrivateKey pk = null;
        try
        {
            KeyStore ks = KeyStore.getInstance("JKS");
            String configDir = System.getProperty("jboss.server.config.dir");
            String keystorePath = configDir + File.separator + "jwt.keystore";

            try (FileInputStream fis = new FileInputStream(keystorePath))
            {
                ks.load(fis, p4ssw0rd);
                Key key = ks.getKey(alias, p4ssw0rd);
                if(key instanceof PrivateKey){
                    pk = (PrivateKey) key;
                }
            }
        }
        catch (NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException | IOException | KeyStoreException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to load jwt decryption key", e);
        }
        privateKey = pk;
    }

    private static final PrivateKey privateKey;
    private static final int TOKEN_VALIDITY = 14400;
    private static final String CLAIM_ROLES = "groups";
    private static final String ISSUER = "quickstart-jwt-issuer";
    private static final String AUDIENCE = "jwt-audience";

    public String createJwt(final String subject, final String role) throws JOSEException
    {
        JWSSigner signer = new RSASSASigner(privateKey);
        JsonArrayBuilder rolesBuilder = Json.createArrayBuilder();
        rolesBuilder.add(role);
        JsonObjectBuilder claimsBuilder = Json.createObjectBuilder()
                .add("sub", subject)
                .add("iss", ISSUER)
                .add("aud", AUDIENCE)
                .add(CLAIM_ROLES, rolesBuilder.build())
                .add("exp", ((System.currentTimeMillis() / 1000) + TOKEN_VALIDITY));

        JWSObject jwsObject = new JWSObject(new JWSHeader.Builder(JWSAlgorithm.RS256)
                .type(new JOSEObjectType("jwt")).build(),
                new Payload(claimsBuilder.build().toString()));

        jwsObject.sign(signer);

        return jwsObject.serialize();
    }
    public static final String BEARER = "Bearer";



}


package fr.hb.mlang.hotel.security;

import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtKeyManager {

  @Getter
  private Algorithm algorithm;

  @Value("${jwt.keys.location}")
  private Path keyLocation;

  @PostConstruct
  private void initialize() throws Exception {
    Path publicFile = keyLocation.resolve("public.key");
    Path privateFile = keyLocation.resolve("private.key");
    KeyPair keyPair;

    if (Files.notExists(publicFile) || Files.notExists(privateFile)) {
      KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
      keyPair = generator.generateKeyPair();

      Files.write(publicFile, keyPair.getPublic().getEncoded());
      Files.write(privateFile, keyPair.getPrivate().getEncoded());
    } else {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      keyPair = new KeyPair(
          keyFactory.generatePublic(new X509EncodedKeySpec(Files.readAllBytes(publicFile))),
          keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Files.readAllBytes(privateFile))));
    }

    algorithm = Algorithm.RSA256((RSAPublicKey) keyPair.getPublic(), (RSAPrivateKey) keyPair.getPrivate());
  }
}

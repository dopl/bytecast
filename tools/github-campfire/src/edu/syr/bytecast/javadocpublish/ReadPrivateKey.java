/*
 * This file is part of Bytecast.
 *
 * Bytecast is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Bytecast is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Bytecast.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package edu.syr.bytecast.javadocpublish;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.RSAPublicKeyStructure;

/**
 * Reads Java KeyPair from id_rsa and id_rsa.pub
 */
public class ReadPrivateKey {

  private RSAPrivateKey m_privateKey;
  private RSAPublicKey m_publicKey;
  
  private List<Byte> readFileBytes(String filename) throws Exception {
    List<Byte> bytes = new ArrayList<Byte>();
    InputStream fin = new FileInputStream(filename);
    while(true){
      byte[] buffer = new byte[4096];
      int len = fin.read(buffer);
      if(len == -1){
        break;
      }
      for(int i = 0; i < len; ++i){
        bytes.add(buffer[i]);
      }
    }
    fin.close();
    return bytes;
  }
  
  public void readPrivateKey(String filename) throws Exception {
    List<Byte> bytes = readFileBytes(filename);
    
    int start_length = "-----BEGIN RSA PRIVATE KEY-----\n".length();
    int end_length = "\n-----END RSA PRIVATE KEY-----".length();
    bytes = bytes.subList(start_length, bytes.size() - end_length);
    String b64encoded = "";
    for(Byte value : bytes){
      b64encoded += (char) value.byteValue();
    }
    byte [] asn1PrivateKeyBytes = org.apache.commons.codec.binary.Base64.decodeBase64(b64encoded.getBytes("US-ASCII"));
    RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(asn1PrivateKeyBytes));
    RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
    KeyFactory kf = KeyFactory.getInstance("RSA");
    m_privateKey = (RSAPrivateKey) kf.generatePrivate(rsaPrivKeySpec);
  }

  public void readPublicKey(String filename) throws Exception {
    List<Byte> bytes = readFileBytes(filename);
    
    
    int start_length = "ssh-rsa ".length();
    int end_length = " pcpratts@fortress".length();
    bytes = bytes.subList(start_length, bytes.size() - end_length);
    String b64encoded = "";
    for(Byte value : bytes){
      b64encoded += (char) value.byteValue();
    }
    byte [] key_bytes = org.apache.commons.codec.binary.Base64.decodeBase64(b64encoded.getBytes("US-ASCII"));
    int len_e = readInt(key_bytes, 0);
    byte[] e_bytes = subArray(key_bytes, 4, len_e);
    BigInteger e = new BigInteger(e_bytes);
    int len_m = readInt(key_bytes, 4 + len_e);
    byte[] m_bytes = subArray(key_bytes, 8 + len_e, len_m);
    BigInteger m = new BigInteger(m_bytes);
    
    RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(m, e);
    KeyFactory kf = KeyFactory.getInstance("RSA");
    m_publicKey = (RSAPublicKey) kf.generatePublic(rsaPublicKeySpec);
  }
  
  private int readInt(byte[] key_bytes, int index) {
    int ret = 0;
    ret |= key_bytes[index + 3];
    ret |= ((int) key_bytes[index + 2]) << 8;
    ret |= ((int) key_bytes[index + 1]) << 16;
    ret |= ((int) key_bytes[index + 0]) << 24;
    return ret;
  }
  
  private byte[] subArray(byte[] key_bytes, int index, int len) {
    byte[] ret = new byte[len];
    for(int i = 0; i < len; ++i){
      ret[i] = key_bytes[i+index];
    }
    return ret;
  }
  
  public PublicKey getPublicKey(){
    return m_publicKey;
  }
  
  public PrivateKey getPrivateKey(){
    return m_privateKey;
  }
  
  public static void main(String[] args){
    ReadPrivateKey reader = new ReadPrivateKey();
    try {
      reader.readPrivateKey("/home/pcpratts/.ssh/id_rsa_nopass");
      reader.readPublicKey("/home/pcpratts/.ssh/id_rsa.pub");
    } catch(Exception ex){
      ex.printStackTrace();
    }
  }


}

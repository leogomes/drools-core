package org.drools.core.util;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;

import junit.framework.TestCase;

import org.drools.core.util.KeyStoreHelper;

public class KeyStoreHelperTest extends TestCase {

    public void testSignDataWithPrivateKey() throws UnsupportedEncodingException,
                                            UnrecoverableKeyException,
                                            InvalidKeyException,
                                            KeyStoreException,
                                            NoSuchAlgorithmException,
                                            SignatureException {
        // The server signs the data with the private key
        
        // Set properties to simulate the server
        URL serverKeyStoreURL = getClass().getResource( "droolsServer.keystore" );
        System.setProperty( KeyStoreHelper.PROP_SIGN, "true" );
        System.setProperty( KeyStoreHelper.PROP_PVT_KS_URL, serverKeyStoreURL.toExternalForm() );
        System.setProperty( KeyStoreHelper.PROP_PVT_KS_PWD, "serverpwd" );
        System.setProperty( KeyStoreHelper.PROP_PVT_ALIAS, "droolsKey" );
        System.setProperty( KeyStoreHelper.PROP_PVT_PWD, "keypwd" );
        KeyStoreHelper serverHelper = new KeyStoreHelper();

        // get some data to sign
        byte[] data = "Hello World".getBytes( "UTF8" );

        // sign the data
        byte[] signature = serverHelper.signDataWithPrivateKey( data );

        // now, initialise the client helper
        
        // Set properties to simulate the client
        URL clientKeyStoreURL = getClass().getResource( "droolsClient.keystore" );
        System.setProperty( KeyStoreHelper.PROP_SIGN, "true" );
        System.setProperty( KeyStoreHelper.PROP_PUB_KS_URL, clientKeyStoreURL.toExternalForm() );
        System.setProperty( KeyStoreHelper.PROP_PUB_KS_PWD, "clientpwd" );
        // client needs no password to access the certificate and public key
        KeyStoreHelper clientHelper = new KeyStoreHelper( );

        // check the signature against the data
        assertTrue( clientHelper.checkDataWithPublicKey( "droolsKey",
                                                         data,
                                                         signature ) );

        // check some fake data
        assertFalse( clientHelper.checkDataWithPublicKey( "droolsKey",
                                                          "fake".getBytes( "UTF8" ), 
                                                          signature ) );
    }

}

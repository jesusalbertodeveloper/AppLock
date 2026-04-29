package com.jarm.applock;
import android.content.Context;
//import android.security.identity.PACESecurityInfo;
import android.util.Base64;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
public class PasswordHashing {
        private static final int ITERATIONS = 10000;
        private static final int KEY_LENGTH_BITS = 256;

        public static void setupPasswordHash(Context context, String password) {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            byte[] hash = pbkdf2(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);

            try {
                MasterKey masterKey = new MasterKey.Builder(context)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build();

                EncryptedSharedPreferences prefs = (EncryptedSharedPreferences)
                        EncryptedSharedPreferences.create(
                                context,
                                "secure_prefs",
                                masterKey,
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                        );

                prefs.edit()
                        .putString("password_hash", Base64.encodeToString(hash, Base64.NO_WRAP))
                        .putString("password_salt", Base64.encodeToString(salt, Base64.NO_WRAP))
                        .apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private static byte[] pbkdf2(char[] password, byte[] salt, int iterations, int keyLength) {
            try {
                SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
                PBEKeySpec spec = new PBEKeySpec(password, salt, iterations, keyLength);
                return factory.generateSecret(spec).getEncoded();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    public static boolean verifyPassword(Context context, String enteredPassword) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            EncryptedSharedPreferences prefs = (EncryptedSharedPreferences)
                    EncryptedSharedPreferences.create(
                            context,
                            "secure_prefs",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );

            String storedHashBase64 = prefs.getString("password_hash", null);
            String saltBase64 = prefs.getString("password_salt", null);

            if (storedHashBase64 == null || saltBase64 == null) {
                return false;
            }

            byte[] salt = Base64.decode(saltBase64, Base64.NO_WRAP);
            byte[] expectedHash = Base64.decode(storedHashBase64, Base64.NO_WRAP);
            byte[] inputHash = pbkdf2(enteredPassword.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);
            return java.util.Arrays.equals(inputHash, expectedHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isPasswordSet(Context context) {
        try {
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            EncryptedSharedPreferences prefs = (EncryptedSharedPreferences)
                    EncryptedSharedPreferences.create(
                            context,
                            "secure_prefs",
                            masterKey,
                            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                    );

            String storedHashBase64 = prefs.getString("password_hash", null);
            String saltBase64 = prefs.getString("password_salt", null);

            if (storedHashBase64 == null || saltBase64 == null) {
                return false;
            }
            else if (storedHashBase64 != null && saltBase64 != null) {
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    }

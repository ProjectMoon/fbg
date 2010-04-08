/*
* This file is part of the Factbook Generator.
* 
* The Factbook Generator is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* 
* The Factbook Generator is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
*
* Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang
*/
package edu.uara.db.profiles;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import edu.uara.FBGConstants;
import edu.uara.config.Config;

/**
 * Manages the serialization and de-serialization of DatabaseProfile objects. The
 * class also contains several useful constants, such as the path of the user's
 * configuration file location. This class will probably be changing as encryption
 * no longer appears to be necessary.
 * @author jeff
 */
public class ProfileManager {
    public static DatabaseProfile CURRENT_PROFILE = null;
    public static final String KEY_FILE = "nullkey";
    public static final String PROFILE_FILE = "db.profile";
    
    private static String password = getKey();
    
    public ProfileManager() {}
    
    /**
     * This method gets the secret key for serialization from the file, or
     * makes a new one.
     * @return The secret key.
     */
    private static String getKey() {
        String keyFileName = getAbsoluteKeyPath();
        File keyFile = new File(keyFileName);
        //if the key file doesn't exist we need to generate a key and write it to
        //the file
        String key = "";
        if (!keyFile.exists()) {
            key = generateKey(keyFileName);
        }
        else {
            //the key exists, load it from a file
            try {
                BufferedReader br = new BufferedReader(new FileReader(keyFile));
                key = br.readLine();
                //maybe the file was empty?
                if (key == null) key = generateKey(keyFileName);
            }
            catch (Exception e) { 
                //there was something wrong; generate a new key
                key = generateKey(keyFileName);
            }
        }
        return key;
    }
    
    /**
     * Helper method to generate the secret key.
     * @param fileName - The name of the key file to write. Always equal to KEY_FILE.
     * @return The secret key.
     */
    private static String generateKey(String fileName) {
        Random r = new Random(System.nanoTime());
        String key = "";
        for (int c = 0; c < 200; c++)
            key += Integer.toHexString(r.nextInt());
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
            bw.write(key);
            bw.close();
        }
        catch (IOException e) {
            System.err.println("There was an I/O error when generating the key.");
        }
        return key;
    }
    
    /**
     * Saves the specified profile to a file.
     * @param fileName - The filename.
     * @param profile - The database profile to save.
     * @return True on success, false otherwise.
     */
    public static boolean saveProfile (String fileName, DatabaseProfile profile) {
        fileName = FBGConstants.PROFILE_PATH + fileName;
        //Set the config property and save it        
        Config.setProperty("defaultProfile", fileName);
        Config.saveProperties();
        
        System.out.println("Attempting to save profile " + fileName);
        try {
            /*
            // Create Key
            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // Create Cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Create stream
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            CipherOutputStream cos = new CipherOutputStream(bos, desCipher);
            ObjectOutputStream oos = new ObjectOutputStream(cos);
            */

            // Write objects
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
            oos.writeObject(profile);
            oos.flush();
            oos.close();            
        }
        catch(Exception e) { System.err.println("There was an error saving the profile."); return false; }
        return true;
    }
    
    /**
     * Attempts to load a database profile from the given filename.
     * @param fileName - The file containing encrypted profile data.
     * @return the DatabaseProfile if successful, null otherwise.
     */
    public static DatabaseProfile loadProfile(String fileName) {
        //fileName = getAbsoluteProfilePath() + fileName;
        DatabaseProfile indbp = null;
        try {
            /*
            // Create Key
            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // Create Cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Create stream
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            CipherInputStream cis = new CipherInputStream(bis, desCipher);
            ObjectInputStream ois = new ObjectInputStream(cis);
            */

            // Read objects
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
            indbp = (DatabaseProfile)ois.readObject();
            ois.close();
            System.out.println("Loaded " + indbp);
            return indbp;
        }
        catch(FileNotFoundException e) { System.err.println("Could not find a profile to load!"); return null; }
        catch(IOException e) { System.err.println("There was an error loading the profile.. Please create a new profile."); return null; }
        catch(ClassNotFoundException e) { System.err.println("Couldn't find the class to de-serialize!"); }
        return indbp;
    }
    
    /**
     * Attempts to load a database profile from the given filename.
     * @param fileName - The file containing encrypted profile data.
     * @return the DatabaseProfile if successful, null otherwise.
     */
    public static DatabaseProfile loadProfile(File f) {
        DatabaseProfile indbp = null;
        try {
            /*
            // Create Key
            byte key[] = password.getBytes();
            DESKeySpec desKeySpec = new DESKeySpec(key);
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey secretKey = keyFactory.generateSecret(desKeySpec);

            // Create Cipher
            Cipher desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            desCipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Create stream
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            CipherInputStream cis = new CipherInputStream(bis, desCipher);
            ObjectInputStream ois = new ObjectInputStream(cis);
            */

            // Read objects
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            indbp = (DatabaseProfile)ois.readObject();
            ois.close();
            return indbp;
        }
        catch(FileNotFoundException e) { return null; }
        catch(IOException e) { System.err.println("Invalid profile decryption key. Please create a new profile."); return null; }
        catch(ClassNotFoundException e) { System.err.println("Couldn't find the class to de-serialize!"); }
        return indbp;
    }    
    
    public static ProfileTemplate loadTemplate(String fileName) {
        ProfileTemplate template = null;
        try {
            // Create stream
            FileInputStream fis = new FileInputStream(fileName);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            // Read objects
            template = (ProfileTemplate)ois.readObject();
            ois.close();
            bis.close();
            fis.close();
            return template;
        }
        catch(FileNotFoundException e) { System.out.println("Template file not found!"); return null; }
        catch(IOException e) { System.err.println("Invalid profile decryption key. Please create a new profile."); return null; }
        catch(ClassNotFoundException e) { System.err.println("Couldn't find the class to de-serialize!"); }
        return null;
    }
    
    public static ProfileTemplate loadTemplate(File f) {
        ProfileTemplate template = null;
        try {
            // Create stream
            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);

            // Read objects
            template = (ProfileTemplate)ois.readObject();
            ois.close();
            bis.close();
            fis.close();
            return template;
        }
        catch(FileNotFoundException e) { System.out.println("Template file not found!"); return null; }
        catch(IOException e) { System.err.println("Invalid profile decryption key. Please create a new profile."); return null; }
        catch(ClassNotFoundException e) { System.err.println("Couldn't find the class to de-serialize!"); }
        return null;
    }    
    
    /**
     * Gets the absolute file path to the key file.
     * @return The file path.
     */
    public static String getAbsoluteKeyPath() {
        String keyName = FBGConstants.PROFILE_PATH + KEY_FILE;
        return keyName;
    }
}

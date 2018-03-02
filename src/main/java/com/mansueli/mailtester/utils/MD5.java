/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mansueli.emailtoolbox.utils;


import java.security.MessageDigest;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
/**
 *
 * @author Mansueli
 */
public class MD5 {
        public static String getHash(String email) {
        MessageDigest md5;
        email = email.toLowerCase();
        try {
            md5 = MessageDigest.getInstance("MD5");
            String hash = (new HexBinaryAdapter()).marshal(md5.digest(email.getBytes())).toLowerCase();
            return hash;
        }catch(Exception e){
            return "";
        }
    }
}

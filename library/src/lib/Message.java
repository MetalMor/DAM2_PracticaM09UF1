/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lib;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author m0r
 */
public class Message implements Serializable {
    
    private final String _senderName;
    private byte[] _content;
    private boolean _encrypted;
    
    public Message(String sender) {
        _senderName = sender;
    }
    
    public String getSender() {
        return _senderName;
    }
    
    public byte[] getContent() {
        return _content;
    }
    
    public void setContent(byte[] content) {
        _content = content;
    }
    
    public String getStringContent() {
        return new String(_content, StandardCharsets.UTF_8);
    }
    
    public boolean isEncrypted() {
        return _encrypted;
    }
    
    public void setEncrypted(boolean encrypted) {
        _encrypted = encrypted;
    }
    
}

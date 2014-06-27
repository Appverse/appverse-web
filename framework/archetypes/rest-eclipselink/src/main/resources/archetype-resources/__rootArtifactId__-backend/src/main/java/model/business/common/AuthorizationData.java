#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.model.business.common;

/**
 * Created by MCRZ on 3/03/14.
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author maps
 */
public class AuthorizationData extends ResultData {

    /**
     * Coordinates to authorize the transfer.
     */
    private String keyCoordinate;

    /**
     * Security Token for authorization.
     */
    private String seed;

    /**
     * An array containing the positions to use to create the buttons for a virtual keyboard.
     */
    private String[] virtualKeyboardPosition;


    /**
     * Constructor
     */
    public AuthorizationData() {
    }

    /**
     * @return the keyCoordinate
     */
    public String getKeyCoordinate() {
        return keyCoordinate;
    }

    /**
     * @param keyCoordinate the keyCoordinate to set
     */
    public void setKeyCoordinate(String keyCoordinate) {
        this.keyCoordinate = keyCoordinate;
    }

    /**
     * @return the seed
     */
    public String getSeed() {
        return seed;
    }

    /**
     * @param seed the seed to set
     */
    public void setSeed(String seed) {
        this.seed = seed;
    }

    /**
     * @return the virtualKeyboardPosition
     */
    public String[] getVirtualKeyboardPosition() {
        return virtualKeyboardPosition;
    }

    /**
     * @param virtualKeyboardPosition the virtualKeyboardPosition to set
     */
    public void setVirtualKeyboardPosition(String[] virtualKeyboardPosition) {
        this.virtualKeyboardPosition = virtualKeyboardPosition;
    }


}


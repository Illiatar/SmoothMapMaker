/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mapmaker;

import com.google.gson.*;

/**
 *
 * @author tucker
 */
interface Saveable {
    public JsonElement pack();
}

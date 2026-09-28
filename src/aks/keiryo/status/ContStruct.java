/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aks.keiryo.status;

/**
 *
 * @author tss
 */
public class ContStruct extends CommonStruct {

    public ContStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}

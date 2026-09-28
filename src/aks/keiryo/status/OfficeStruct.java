/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aks.keiryo.status;

/**
 *
 * @author tss
 */
public class OfficeStruct extends CommonStruct {

    public OfficeStruct() {
        Clear();
    }

    public void Clear() {
        mValue.clear();

        putEx("id", "", TYPE_STRING);
        putEx("name", "", TYPE_STRING);
    }
}

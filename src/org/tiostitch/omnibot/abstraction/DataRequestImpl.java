package org.tiostitch.omnibot.abstraction;

public interface DataRequestImpl {

    Userdata loadUserdata(String key, long discId);

    Userdata updateUserdata(String key, Userdata userdata);
    Userdata updateUserdataById(long key, Userdata userdata);

    void deleteUserdata(String key);
    void deleteUserdataById(long key);

    boolean existUser(String key);
    boolean existUserById(long key);

    Userdata getUserdata(String key);
    Userdata getUserdataById(long key);

}

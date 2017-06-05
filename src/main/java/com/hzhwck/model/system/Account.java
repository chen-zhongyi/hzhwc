package com.hzhwck.model.system;

import com.jfinal.plugin.activerecord.Model;

/**
 * Created by 陈忠意 on 2017/6/4.
 */
public class Account extends Model<Account> {
    public static final Account dao = new Account().dao();
    private static final String tableName = "hzhwc.sys_accounts";

    public Account add(Account account){
        if(account.save())  return account;
        return null;
    }

    public Account modify(Account account){
        if(account.update())    return account;
        return null;
    }

    public boolean delete(String id){
        return Account.dao.deleteById(id);
    }

    public Account findByUserName(String userName){
        return Account.dao.findFirst("select * from " + tableName + " where userName = " + userName);
    }
}

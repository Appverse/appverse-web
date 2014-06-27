#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.services.business;

import ${package}.model.business.customer.AccountsData;
import ${package}.model.business.customer.AccountsRequestData;
import ${package}.model.business.customer.User;

/**
 * Created by MCRZ on 10/03/14.
 */
public interface CustomerService {

    User getUserInfo(String username)throws Exception ;
    User updateUserInfo(String username, User user)throws Exception ;
    AccountsData getGlobalPosition(String username)throws Exception ;
    AccountsData getAccounts(AccountsRequestData request, String username)throws Exception;
    AccountsData getCreditCards(String username) throws  Exception;
}

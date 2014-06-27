#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
/*
 Copyright (c) 2012 GFT Appverse, S.L., Sociedad Unipersonal.

 This Source Code Form is subject to the terms of the Appverse Public License
 Version 2.0 (“APL v2.0”). If a copy of the APL was not distributed with this
 file, You can obtain one at http://www.appverse.mobi/licenses/apl_v2.0.pdf. [^]

 Redistribution and use in source and binary forms, with or without modification,
 are permitted provided that the conditions of the AppVerse Public License v2.0
 are met.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. EXCEPT IN CASE OF WILLFUL MISCONDUCT OR GROSS NEGLIGENCE, IN NO EVENT
 SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 */

package ${package}.services.business.impl.live;


import ${package}.converters.b2i.UserB2IBeanConverter;
import ${package}.helpers.ErrorCodes;
import ${package}.model.business.customer.*;
import ${package}.model.integration.jpa.AccountDTO;
import ${package}.model.integration.jpa.UserDTO;
import ${package}.services.business.CustomerService;
import ${package}.services.integration.AccountRepository;
import ${package}.services.integration.UserRepository;
import org.appverse.web.framework.backend.api.converters.ConversionType;
import org.appverse.web.framework.backend.api.converters.IB2IBeanConverter;
import org.appverse.web.framework.backend.api.helpers.log.AutowiredLogger;
import org.appverse.web.framework.backend.api.services.business.AbstractBusinessService;
import org.appverse.web.framework.backend.api.services.business.BusinessException;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("customerService")
public class CustomerServiceImpl extends AbstractBusinessService implements
        CustomerService {
    @AutowiredLogger
    private  Logger logger;

    @Autowired
    protected ErrorCodes errorCodes;

    @Autowired
    protected AccountRepository accountRepository;

    @Autowired
    protected UserRepository userRepositoryRepository;

    @Autowired
    protected IB2IBeanConverter<Account,AccountDTO> accountB2IBeanConverter;

    @Autowired
    protected UserB2IBeanConverter userB2IBeanConverter;

    /**
     * Obtains the current global position of the user
     *
     * @param username
     * @return AccountsData
     */
    public AccountsData getGlobalPosition(String username) throws Exception{
        AccountsData global = new AccountsData();
        //accounts
        List<AccountDTO> accountsDTO = accountRepository.loadAccountsByUsername(username);
        global = convertAccountListToAccountsData(accountsDTO);

        return global;
    }

    /**
     * Obtain account contents based on filter options
     *
     * @param request filter options
     * @param username username logged
     * @return list of accounts data filtered
     */
    public AccountsData getAccounts(AccountsRequestData request, String username) throws Exception{
        AccountsData accountsData = new AccountsData();
        String accountType = request.getFamilyName().toString();
        List<AccountDTO> accountsDTO = accountRepository.loadAccountsByOperationAndTypeAndUsername(accountType, username);
        accountsData = convertAccountListToAccountsData(accountsDTO);

        return accountsData;
    }

    /**
     * Obtain list of credit cards accounts
     *
     * @param username
     * @return list of accounts
     * @throws Exception
     */
    public AccountsData getCreditCards(String username) throws  Exception {
        AccountsData accountsData = new AccountsData();
        List<AccountDTO> accountsDTO = accountRepository.loadAccountsByOperationAndTypeAndUsername(accountRepository.CREDIT_CARDS_CODE, username);
        accountsData = convertAccountListToAccountsData(accountsDTO);
        return accountsData;
    }

    /**
     * obtains user info data
     * @param username
     * @return
     * @throws Exception
     */
    public User getUserInfo(String username)throws Exception {
        UserDTO userDTO = userRepositoryRepository.loadUserByUsername(username);
        User user = userB2IBeanConverter.convert(userDTO);
        return user;
    }

    /**
     * Persist user info data
     *
     * @param username username
     * @param user user
     * @return updated data
     * @throws Exception when something when wrong
     */
    public User updateUserInfo(String username, User user)throws Exception {
        UserDTO userDTO = userRepositoryRepository.loadUserByUsername(username);
        if (username!=null && userDTO!=null && username.equals(userDTO.getUsername())){
            userB2IBeanConverter.convert(user,userDTO);
            userRepositoryRepository.persist(userDTO);
        }else{
            throw new BusinessException(ErrorCodes.ERROR_CODE_badrequest_missingdata);
        }

        return user;
    }



    /**
     * Convert a accountsList to a AccountsData with aggregation information
     *
     * @param accountsDTO
     * @return
     * @throws Exception
     */
    private AccountsData convertAccountListToAccountsData(List<AccountDTO> accountsDTO)throws Exception{

        AccountsData accountsData = new AccountsData();

        if (accountsDTO.size()==0){
            //no results found
            accountsData.setError(errorCodes.getError(ErrorCodes.ERROR_CODE_noresultsfound));
        }else{
            accountsData.setError(errorCodes.getError(ErrorCodes.SUCCESS_CODE));
            //convert to accountsVO
            List<Account> accounts = new ArrayList<Account>();
            Map<String,Family> familiesMap = new HashMap<String,Family>();
            for(AccountDTO item : accountsDTO ){
                //dozer
                Account account = accountB2IBeanConverter.convert(item, ConversionType.WithoutDependencies);
                //calculates the accounts aggregation
                addFamilyCodeFromDTO(familiesMap, item);
                //add new account to list
                accounts.add(account);
            }
            accountsData.setResult(accounts.toArray(new Account[0]));
            //Construct families with accounts
            accountsData.setFamilies(obtainFamilies(familiesMap).toArray(new Family[0]));
        }
        return accountsData;
    }

    /**
     * Calculate an aggregation of the accounts of the customer
     *
     * @param familiesMap aggregation map
     * @param accountDTO current account
     */
    private void addFamilyCodeFromDTO(Map<String,Family> familiesMap, AccountDTO accountDTO){
        String familyCode = accountDTO.getAccountType().getCode();
        Family family = familiesMap.get(familyCode);
        if (family == null){
            //adds new family
            family = new Family();
            family.setCode(familyCode);
            family.setDescription(accountDTO.getAccountType().getDescription());
            family.setAggregatedBalanceCurrency(accountDTO.getBalanceCurrency());
            family.setAggregatedBalance(accountDTO.getBalance());
            familiesMap.put(familyCode,family);
        }else{
            //adds to agregated Balance
            double agregatedBalance = accountDTO.getBalance();
            agregatedBalance+= family.getAggregatedBalance();
            family.setAggregatedBalance(agregatedBalance);
        }
    }

    /**
     * Generates a list of values of the map
     *
     * @param familiesMap
     * @return List of families
     */
    private List<Family> obtainFamilies(Map<String,Family> familiesMap){
        //creates a value list from map
        List<Family> list = new ArrayList<Family>();
        for (Map.Entry<String, Family> entry : familiesMap.entrySet())
        {
            Family family = entry.getValue();
            list.add(family);
        }
        return list;
    }



}

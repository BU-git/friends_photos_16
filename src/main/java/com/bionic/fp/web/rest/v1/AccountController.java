package com.bionic.fp.web.rest.v1;

import com.bionic.fp.Constants.RoleConstants;
import com.bionic.fp.domain.Account;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.AccountService;
import com.bionic.fp.web.security.spring.infrastructure.filter.AuthenticationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * This is REST web-service that handles account-related requests
 *
 * @author Sergiy Gabriel
 */
@RestController
@RequestMapping(API+V1+ACCOUNTS)
public class AccountController {

    @Autowired private AccountService accountService;
    @Autowired private AccountEventService accountEventService;
    @Autowired private MethodSecurityService methodSecurityService;
    @Autowired private AuthenticationStrategy authenticationStrategy;


    //***************************************
    //                 @GET
    //***************************************


    /**
     * Returns an account
     * @param accountId the account id
     *
     * @return an account
     */
    @RequestMapping(value = ACCOUNT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public AccountInfo getAccount(@PathVariable(ACCOUNT.ID) final Long accountId) {
        Account account = ofNullable(accountService.get(accountId))
                .orElseThrow(() -> new NotFoundException(accountId, "account"));
        return new AccountInfo(account);
    }

    /**
     * Returns the user, the user must be authenticated
     *
     * @return the user
     */
    @RequestMapping(value = SELF, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public AccountInfo getUser() {
        Account user = methodSecurityService.getUser();
        AccountInfo body = new AccountInfo(user);
        body.setEmail(user.getEmail());
        return body;
    }

    /**
     * Returns a list of accounts belonging to this event
     *
     * @param eventId the event id
     * @return a list of accounts
     */
    @RequestMapping(value = EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public EntityInfoLists getAccounts(@PathVariable(EVENT.ID) final Long eventId) {
        EntityInfoLists body = new EntityInfoLists();
        body.setAccounts(this.accountEventService.getAccounts(eventId).stream().parallel()
                .map(AccountInfo::new).collect(toList()));
        return body;
    }

    /**
     * Returns a list of account ids belonging to this event
     *
     * @param eventId the event id
     * @return a list of account ids
     */
    @RequestMapping(value = ID+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdLists getAccountIds(@PathVariable(EVENT.ID) final Long eventId) {
        IdLists body = new IdLists();
        body.setAccounts(this.accountEventService.getAccounts(eventId).stream().parallel()
                .map(Account::getId).collect(toList()));
        return body;
    }

    /**
     * Returns the owner of the event
     *
     * @param eventId the event id
     * @return the owner
     */
    @RequestMapping(value = OWNER+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public AccountInfo getEventOwner(@PathVariable(EVENT.ID) final Long eventId) {
        List<Account> accounts = this.accountEventService.getAccounts(eventId, RoleConstants.OWNER);
        if(accounts.isEmpty()) {
            throw new NotFoundException(eventId, "event");
        }
        return new AccountInfo(accounts.get(0));
    }

    /**
     * Returns the owner id of the event
     *
     * @param eventId the event id
     * @return the owner id
     */
    @RequestMapping(value = ID+OWNER+EVENTS+EVENT_ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public IdInfo getEventOwnerId(@PathVariable(EVENT.ID) final Long eventId) {
        List<Account> accounts = this.accountEventService.getAccounts(eventId, RoleConstants.OWNER);
        if(accounts.isEmpty()) {
            throw new NotFoundException(eventId, "event");
        }
        return new IdInfo(accounts.get(0).getId());
    }


    //***************************************
    //                 @POST
    //***************************************


//    /**
//     * Endpoint used to register user by FP.
//     */
//    @RequestMapping(value = REGISTER, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(CREATED)
//    @ResponseBody
//    public final IdInfo registerByFP(@RequestBody final AccountInput user, final HttpSession session) {
//        Long userId = this.accountService.registerByFP(user.getEmail(), user.getPassword(), null);
//        SessionUtils.setUserId(session, userId);
//        return new IdInfo(userId);
//    }
//
//    /**
//     * Endpoint used to login by FP.
//     */
//	@RequestMapping(value = LOGIN, method = POST, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
//    @ResponseStatus(OK)
//    @ResponseBody
//    public final IdInfo loginByFP(@RequestBody final AccountInput user, final HttpSession session) {
//        Long userId = this.accountService.loginByFP(user.getEmail(), user.getPassword());
//        SessionUtils.setUserId(session, userId);
//        return new IdInfo(userId);
//    }
//
//    @RequestMapping(value = LOGOUT, method = POST)
//    @ResponseStatus(OK)
//    public final void logout(final HttpSession session) {
//        SessionUtils.logout(session);
//    }



    //***************************************
    //                 @DELETE
    //***************************************


    /**
     * Deletes the user
     */
    @RequestMapping(value = SELF, method = DELETE)
    @ResponseStatus(NO_CONTENT)
    public void deleteUser(final HttpServletRequest request, final HttpServletResponse response) {
        this.accountService.softDelete(this.methodSecurityService.getUserId());
        this.authenticationStrategy.removeAuthentication(request, response);
    }


    //***************************************
    //                 PRIVATE
    //***************************************

}
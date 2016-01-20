package com.bionic.fp.web.rest;

import com.bionic.fp.Constants.RoleConstants;
import com.bionic.fp.domain.Account;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.rest.NotFoundException;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.service.MethodSecurityService;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;


/**
 * Created by boubdyk on 15.11.2015.
 */
@RestController
@RequestMapping(API+ACCOUNTS)
public class AccountController {

	@Autowired private AccountService accountService;
    @Autowired private AccountEventService accountEventService;
    @Autowired private PhotoService photoService;
    @Autowired private MethodSecurityService methodSecurityService;


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
    public final AccountInfo getAccount(@PathVariable(ACCOUNT.ID) final Long accountId) {
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
    public final AccountInfo getUser() {
        return new AccountInfo(methodSecurityService.getUser());
    }

    /**
     * Returns all events where the account is involved
     *
     * @param accountId the account id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getAccountEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return getEvents(accountId);
    }

    /**
     * Returns a list of events where the user is involved.
     * The user must be authenticated
     *
     * @return a list of events
     */
    @RequestMapping(value = SELF+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId);
    }

    /**
     * Returns a list of events owned by the account
     *
     * @param accountId the account id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEvents(accountId, RoleConstants.OWNER);
    }

    /**
     * Returns a list of events owned by the user.
     * The user must be authenticated
     *
     * @return a list of events
     */
    @RequestMapping(value = SELF+EVENTS+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId, RoleConstants.OWNER);
    }

    /**
     * Returns a list of events where the account has the specified role
     *
     * @param accountId the account id
     * @param roleId the role id
     * @return a list of events
     */
    @RequestMapping(value = ACCOUNT_ID+ROLES+ROLE_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getAccountEvents(@PathVariable(ACCOUNT.ID) final Long accountId,
                                                  @PathVariable(ROLE.ID) final Long roleId) {
        return this.getEvents(accountId, roleId);
    }

    /**
     * Returns a list of events where the user has the specified role.
     * The user must be authenticated
     *
     * @param roleId the role id
     * @return a list of events
     */
    @RequestMapping(value = SELF+ROLES+ROLE_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(@PathVariable(ROLE.ID) final Long roleId) {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEvents(userId, roleId);
    }

    /**
     * Return a list of event ids of the account
     *
     * @param accountId the account id
     * @return a list of event ids
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getAccountEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId);
    }

    /**
     * Return a list of event ids of the user.
     * The user must be authenticated
     *
     * @return a list of event ids
     */
    @RequestMapping(value = SELF+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId);
    }

    /**
     * Return a list of event ids owned by the account
     *
     * @param accountId - account ID
     * @return a list of event ids
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS+ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId, RoleConstants.OWNER);
    }

    /**
     * Return a list of event ids owned by the user.
     * The user must be authenticated
     *
     * @return a list of event ids
     */
    @RequestMapping(value = SELF+EVENTS+ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId, RoleConstants.OWNER);
    }

    /**
     * Returns a list of event ids where the account has the specified role
     *
     * @param accountId the account id
     * @param roleId the role id
     * @return a list of event ids
     */
    @RequestMapping(value = ACCOUNT_ID+ROLES+ROLE_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getAccountEventIds(@PathVariable(ACCOUNT.ID) final Long accountId,
                                            @PathVariable(ROLE.ID) final Long roleId) {
        return this.getEventIds(accountId, roleId);
    }

    /**
     * Returns a list of event ids where the user has the specified role.
     * The user must be authenticated
     *
     * @param roleId the role id
     * @return a list of event ids
     */
    @RequestMapping(value = SELF+ROLES+ROLE_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(@PathVariable(ROLE.ID) final Long roleId) {
        Long userId = this.methodSecurityService.getUserId();
        return this.getEventIds(userId, roleId);
    }

    /**
     * Returns a list of photos of the owner
     *
     * @param ownerId the owner id
     * @return a list of photos
     */
    @RequestMapping(value = ACCOUNT_ID+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getAccountPhotos(@PathVariable(ACCOUNT.ID) final Long ownerId) {
        return this.getPhotos(ownerId);
    }

    /**
     * Returns a list of photo ids of the owner
     *
     * @param ownerId the owner id
     * @return a list of photo ids
     */
    @RequestMapping(value = ACCOUNT_ID+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getAccountPhotoIds(@PathVariable(ACCOUNT.ID) final Long ownerId) {
        return this.getPhotoIds(ownerId);
    }

    /**
     * Returns a list of photos of the user.
     * The user must be authenticated
     *
     * @return a list of photos
     */
    @RequestMapping(value = SELF+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserPhotos() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getPhotos(userId);
    }

    /**
     * Returns a list of photo ids of the user.
     * The user must be authenticated
     *
     * @return a list of photo ids
     */
    @RequestMapping(value = SELF+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserPhotoIds() {
        Long userId = this.methodSecurityService.getUserId();
        return this.getPhotoIds(userId);
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
    //                 PRIVATE
    //***************************************


    private EntityInfoLists getEvents(final Long accountId) {
        return this.getEvents(accountId, null);
    }

    private IdLists getEventIds(final Long accountId) {
        return this.getEventIds(accountId, null);
    }

    private EntityInfoLists getEvents(final Long accountId, final Long roleId) {
        List<Event> events = roleId == null ?
                this.accountService.getEvents(accountId) :
                this.accountEventService.getEvents(accountId, roleId);
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(events.stream().parallel()
                .map(EventInfo::new)
                .collect(Collectors.toList()));
        return body;
    }

    private IdLists getEventIds(final Long accountId, final Long roleId) {
        List<Long> events = roleId == null ?
                this.accountService.getEventIds(accountId) :
                this.accountEventService.getEventIds(accountId, roleId);
        IdLists body = new IdLists();
        body.setEvents(events);
        return body;
    }

    private EntityInfoLists getPhotos(final Long accountId) {
        List<Photo> photos = this.photoService.getPhotosByOwnerId(accountId);
        EntityInfoLists body = new EntityInfoLists();
        body.setPhotos(photos.stream().parallel()
                .map(PhotoInfo::new)
                .collect(Collectors.toList()));
        return body;
    }

    private IdLists getPhotoIds(final Long accountId) {
        List<Long> photos = this.photoService.getPhotoIdsByOwnerId(accountId);
        IdLists body = new IdLists();
        body.setPhotos(photos);
        return body;
    }
}

package com.bionic.fp.web.rest;

import com.bionic.fp.Constants.RoleConstants;
import com.bionic.fp.domain.Event;
import com.bionic.fp.domain.Photo;
import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.EmptyPasswordException;
import com.bionic.fp.exception.auth.impl.UserNameAlreadyExistException;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.service.AccountEventService;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.rest.dto.*;
import com.bionic.fp.web.security.SessionUtils;
import com.bionic.fp.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.Constants.RestConstants.*;
import static com.bionic.fp.Constants.RestConstants.PATH.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


/**
 * Created by boubdyk on 15.11.2015.
 */
@RestController
@RequestMapping(ACCOUNTS)
public class AccountController {

	@Autowired
    private AccountService accountService;
    @Autowired
    private AccountEventService accountEventService;
    @Autowired
    private PhotoService photoService;
	@Autowired
    private FromJSONParser parser;


    //***************************************
    //                 @GET
    //***************************************

    /**
     * All events where the user is involved
     *
     * @param accountId - account ID
     * @return - List of IDs events where the user is involved
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return getEvents(accountId);
    }

    @RequestMapping(value = SELF+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEvents(userId);
    }

    @RequestMapping(value = ACCOUNT_ID+EVENTS+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEvents(accountId, RoleConstants.OWNER);
    }

    @RequestMapping(value = SELF+EVENTS+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getOwnerEvents(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEvents(userId, RoleConstants.OWNER);
    }

    @RequestMapping(value = ACCOUNT_ID+ROLES+ROLE_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(@PathVariable(ACCOUNT.ID) final Long accountId,
                                               @PathVariable(ROLE.ID) final Integer roleId) {
        return this.getEvents(accountId, roleId);
    }

    @RequestMapping(value = SELF+ROLES+ROLE_ID+EVENTS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserEvents(final HttpSession session,
                                               @PathVariable(ROLE.ID) final Integer roleId) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEvents(userId, roleId);
    }

    @RequestMapping(value = ACCOUNT_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId);
    }

    @RequestMapping(value = SELF+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEventIds(userId);
    }

    /**
     * All events where the user is owner
     *
     * @param accountId - account ID
     * @return - List of IDs events where the user is owner
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS+ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds(@PathVariable(ACCOUNT.ID) final Long accountId) {
        return this.getEventIds(accountId, RoleConstants.OWNER);
    }

    @RequestMapping(value = SELF+EVENTS+ID+OWNER, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getOwnerEventIds(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEventIds(userId, RoleConstants.OWNER);
    }

    @RequestMapping(value = ACCOUNT_ID+ROLES+ROLE_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(@PathVariable(ACCOUNT.ID) final Long accountId,
                                         @PathVariable(ROLE.ID) final Integer roleId) {
        return this.getEventIds(accountId, roleId);
    }

    @RequestMapping(value = SELF+ROLES+ROLE_ID+EVENTS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserEventIds(final HttpSession session, @PathVariable(ROLE.ID) final Integer roleId) {
        Long userId = SessionUtils.getUserId(session);
        return this.getEventIds(userId, roleId);
    }

    @RequestMapping(value = ACCOUNT_ID+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserPhotos(@PathVariable(ACCOUNT.ID) final Long ownerId) {
        return this.getPhotos(ownerId);
    }

    @RequestMapping(value = ACCOUNT_ID+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserPhotoIds(@PathVariable(ACCOUNT.ID) final Long ownerId) {
        return this.getPhotoIds(ownerId);
    }

    @RequestMapping(value = SELF+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final EntityInfoLists getUserPhotos(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getPhotos(userId);
    }

    @RequestMapping(value = SELF+PHOTOS+ID, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    @ResponseBody
    public final IdLists getUserPhotoIds(final HttpSession session) {
        Long userId = SessionUtils.getUserId(session);
        return this.getPhotoIds(userId);
    }


    //***************************************
    //                 @POST
    //***************************************


    /**
     * Endpoint used to register user by FP.
     * @param input input JSON.
     * @return error code and JSON.
     */
    @RequestMapping(value = REGISTER, method = POST, consumes = APPLICATION_JSON_VALUE)
    public final ResponseEntity<String> registerByFP(@RequestBody final String input, final HttpSession session) {
        JSONObject inputObject = parser.parse(input);
        JSONObject returnObject;
        String email = (String)inputObject.get("email");
        String userName = (String)inputObject.get("userName");
        String password = (String)inputObject.get("password");
        try {
            returnObject = new JSONObject();
            Long userId = accountService.registerByFP(email, userName, password);
            returnObject.put("userID", userId);
            SessionUtils.setUserId(session, userId);
            return new ResponseEntity<String>("userCreated", OK);
        } catch (UserNameAlreadyExistException ex) {
            return new ResponseEntity<String>("User already exist", BAD_REQUEST);
        } catch (EmailAlreadyExistException ex) {
            return new ResponseEntity<String>("Email already exist", BAD_REQUEST);
        } catch (EmptyPasswordException ex) {
            return new ResponseEntity<String>("Password is empty", BAD_REQUEST);
        }
    }

    /**
     * Endpoint used to login by FP.
     * @param userName user name.
     * @param password password.
     * @return error code and JSON.
     */
	@RequestMapping(value = LOGIN, method = POST, consumes = APPLICATION_JSON_VALUE)
    public final ResponseEntity<String> loginByFP(@RequestParam("userName") final String userName,
                                                  @RequestParam("password") final String password,
                                                  final HttpSession session) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject();
            Long userId = accountService.loginByFP(userName, password);
            returnObject.put("userID", userId);
            SessionUtils.setUserId(session, userId);
            return new ResponseEntity<String>(returnObject.toJSONString(), OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Wrong user name or password", BAD_REQUEST);
        }
    }

    @RequestMapping(value = LOGOUT, method = POST)
    @ResponseStatus(OK)
    public final void logout(final HttpSession session) {
        SessionUtils.logout(session);
    }


    //***************************************
    //                 PRIVATE
    //***************************************


    /**
     * Used to wrap error to JSON.
     * @param msg error message.
     * @return JSONOnject.
     */
    private JSONObject errorMsgToJson(String msg) {
        JSONObject returnObject = new JSONObject();
        returnObject.put("errorCode", 304);
        returnObject.put("message", msg);
        return returnObject;
    }

    private EntityInfoLists getEvents(final Long accountId) {
        return this.getEvents(accountId, null);
    }

    private IdLists getEventIds(final Long accountId) {
        return this.getEventIds(accountId, null);
    }

    private EntityInfoLists getEvents(final Long accountId, final Integer roleId) {
        List<Event> events = roleId == null ?
                this.accountService.getEvents(accountId) :
                this.accountEventService.getEvents(accountId, roleId);
        EntityInfoLists body = new EntityInfoLists();
        body.setEvents(events.stream().parallel()
                .map(EventInfo::new)
                .collect(Collectors.toList()));
        return body;
    }

    private IdLists getEventIds(final Long accountId, final Integer roleId) {
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
                .map(PhotoInfoDTO::new)
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

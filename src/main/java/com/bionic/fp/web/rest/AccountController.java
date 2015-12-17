package com.bionic.fp.web.rest;

import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.EmptyPasswordException;
import com.bionic.fp.exception.auth.impl.UserNameAlreadyExistException;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.service.PhotoService;
import com.bionic.fp.web.rest.dto.EntityInfoListsDTO;
import com.bionic.fp.web.rest.dto.EventsIDsList;
import com.bionic.fp.web.rest.dto.PhotoInfoDTO;
import com.bionic.fp.web.security.SessionUtils;
import com.bionic.fp.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import static com.bionic.fp.web.rest.RestConstants.*;
import static com.bionic.fp.web.rest.RestConstants.PATH.*;
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
    @RequestMapping(value = ACCOUNT_ID+EVENTS, method = GET)
    public final ResponseEntity<EventsIDsList> getUserEvents(@PathVariable(ACCOUNT.ID) final Long accountId) {
        List<Long> events = accountService.getEventsIDs(accountId);
        EventsIDsList eventsIDsList = new EventsIDsList(events);
        return new ResponseEntity<>(eventsIDsList, OK);
    }

    /**
     * All events where the user is owner
     *
     * @param accountId - account ID
     * @return - List of IDs events where the user is owner
     */
    @RequestMapping(value = ACCOUNT_ID+EVENTS+OWNER, method = GET)
    public final ResponseEntity<EventsIDsList> getUserEventsWhereRoleOwner(@PathVariable(ACCOUNT.ID) final Long accountId) {
        List<Long> events = accountService.getEventsIDsWhereRoleOwner(accountId);
        EventsIDsList eventsIDsList = new EventsIDsList(events);
        return new ResponseEntity<>(eventsIDsList, OK);
    }

    @RequestMapping(value = ACCOUNT_ID+PHOTOS, method = GET, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public @ResponseBody EntityInfoListsDTO getPhotosByOwnerId(@PathVariable(ACCOUNT.ID) final Long ownerId) {
        EntityInfoListsDTO body = new EntityInfoListsDTO();
        body.setPhotos(this.photoService.getPhotosByOwnerId(ownerId).stream().parallel()
                .map(PhotoInfoDTO::new)
                .collect(Collectors.toList()));
        return body;
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
}

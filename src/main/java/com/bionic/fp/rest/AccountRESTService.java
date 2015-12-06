package com.bionic.fp.rest;

import com.bionic.fp.exception.auth.impl.EmailAlreadyExistException;
import com.bionic.fp.exception.auth.impl.EmptyPasswordException;
import com.bionic.fp.exception.auth.impl.UserNameAlreadyExistException;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.security.SessionUtils;
import com.bionic.fp.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;


/**
 * Created by boubdyk on 15.11.2015.
 */
@RestController
@RequestMapping("/account")
public class AccountRESTService {

	@Autowired
    private AccountService accountService;
	@Autowired
    private FromJSONParser parser;

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

    /**
     * Endpoint used to register user by FP.
     * @param input input JSON.
     * @return error code and JSON.
     */
    @RequestMapping(value = "/register", method = POST, consumes = APPLICATION_JSON_VALUE)
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
	@RequestMapping(value = "/login", method = POST, consumes = APPLICATION_JSON_VALUE)
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

    @RequestMapping(value = "/logout", method = PUT)
    @ResponseStatus(OK)
    public final void logout(final HttpSession session) {
        session.invalidate();
    }
}

package com.bionic.fp.rest;

import com.bionic.fp.exception.EmailAlreadyExistException;
import com.bionic.fp.exception.EmptyPasswordException;
import com.bionic.fp.exception.UserNameAlreadyExistException;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<String> registerByFP(@RequestBody final String input) {
        JSONObject inputObject = parser.parse(input);
        JSONObject returnObject;
        String email = (String)inputObject.get("email");
        String userName = (String)inputObject.get("userName");
        String password = (String)inputObject.get("password");
        try {
            returnObject = new JSONObject();
            returnObject.put("userID", accountService.registerByFP(email, userName, password));
            return new ResponseEntity<String>("userCreated", HttpStatus.OK);
        } catch (UserNameAlreadyExistException ex) {
            return new ResponseEntity<String>("User already exist", HttpStatus.BAD_REQUEST);
        } catch (EmailAlreadyExistException ex) {
            return new ResponseEntity<String>("Email already exist", HttpStatus.BAD_REQUEST);
        } catch (EmptyPasswordException ex) {
            return new ResponseEntity<String>("Password is empty", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Endpoint used to login by FP.
     * @param userName user name.
     * @param password password.
     * @return error code and JSON.
     */
	@RequestMapping(value = "/login", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE)
    public final ResponseEntity<String> loginByFP(@RequestParam("userName") final String userName,
									@RequestParam("password") final String password) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject();
            returnObject.put("userID", accountService.loginByFP(userName, password));
            return new ResponseEntity<String>(returnObject.toJSONString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Wrong user name or password", HttpStatus.BAD_REQUEST);
        }
    }
}

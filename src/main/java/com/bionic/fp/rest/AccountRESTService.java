package com.bionic.fp.rest;

import com.bionic.fp.exception.*;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.service.AccountService;
import org.apache.commons.lang3.StringUtils;

import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by boubdyk on 15.11.2015.
 */
@RestController("/account")
public class AccountRESTService {

    private static ApplicationContext context;
    private static AccountService accountService;
    private static FromJSONParser parser;

    static {
        context = new ClassPathXmlApplicationContext("beans.xml");
        accountService = context.getBean(AccountService.class);
        parser = context.getBean(FromJSONParser.class);
    }

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

    //TODO generate gson error report. (CODE NOT MODIFIED)
    /**
     * Endpoint used to register user by Facebook.
     * @param input input JSON object.
     * @return error code and JSON.
     */
	@RequestMapping(
			value = "/fb",
			method = RequestMethod.POST,
			consumes  = "application/json",
			produces = "application/json")
    public final ResponseEntity loginByFB(@RequestBody final String input) {
        JSONObject inputObject = parser.parse(input);
        JSONObject returnObject;
        String fbID = (String)inputObject.get("fbID");
        if (StringUtils.isEmpty(fbID)) {
			return new ResponseEntity<String>("Facebook id is empty or null", HttpStatus.BAD_REQUEST);
        }
        String fbProfile = (String)inputObject.get("fbProfile");
        if (StringUtils.isEmpty(fbProfile)) {
			return new ResponseEntity<String>("Facebook profile URL is empty or null", HttpStatus.BAD_REQUEST);
        }
        String fbToken = (String)inputObject.get("fbToken");
        if (StringUtils.isEmpty(fbToken)) {
			return new ResponseEntity<String>("Facebook token is empty or null", HttpStatus.BAD_REQUEST);
        }
        String userName = (String)inputObject.get("userName");
        if (StringUtils.isEmpty(userName)) {
			return new ResponseEntity<String>("Facebook userName is empty or null", HttpStatus.BAD_REQUEST);
        }
		try {
			Long userID = accountService.loginByFB(fbID, fbProfile, fbToken, userName);
			return new ResponseEntity<String>("{userID: \"" + userID + "\"}", HttpStatus.CREATED);
		}
		catch (Exception e) { // FIXME use more narrow exception type
			return new ResponseEntity<String>("null", HttpStatus.BAD_REQUEST);
		}
    }

    //TODO generate gson error report. (CODE NOT MODIFIED)
    /**
     * Endpoint used to register user by VK.
     * @param input input JSON object.
     * @return error code and JSON.
     */
    @POST
    @Path("/vk")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public final Response loginByVK(final String input) {
        JSONObject inputObject = parser.parse(input);
        JSONObject returnObject;
        String vkID = (String)inputObject.get("vkID");
        if (StringUtils.isEmpty(vkID)) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson("VK id is empty or null")).build();
        }
        String vkProfile = (String)inputObject.get("vkProfile");
        if (StringUtils.isEmpty(vkProfile)) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson("VK profile URL is empty or null")).build();
        }
        String vkToken = (String)inputObject.get("vkToken");
        if (StringUtils.isEmpty(vkToken)) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson("VK token is empty or null")).build();
        }
        String userName = (String)inputObject.get("userName");
        if (StringUtils.isEmpty(userName)) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson("VK user name is empty or null")).build();
        }
        Long userID = accountService.loginByFB(vkID, vkProfile, vkToken, userName);
        returnObject = new JSONObject();
        returnObject.put("userID", userID);
        return Response.status(Constants.CODE_CREATED).entity(returnObject).build();
    }

    /**
     * Endpoint used to register user by FP.
     * @param input input JSON.
     * @return error code and JSON.
     */
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public final Response registerByFP(final String input) {
        JSONObject inputObject = parser.parse(input);
        JSONObject returnObject;
        String email = (String)inputObject.get("email");
        String userName = (String)inputObject.get("userName");
        String password = (String)inputObject.get("password");
        try {
            returnObject = new JSONObject();
            returnObject.put("userID", accountService.registerByFP(email, userName, password));
            return Response.status(Constants.CODE_CREATED).entity(returnObject).build();
        } catch (UserNameAlreadyExistException ex) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        } catch (EmailAlreadyExistException ex) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        } catch (EmptyPasswordException ex) {
            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        }
    }

    /**
     * Endpoint used to login by FP.
     * @param userName user name.
     * @param password password.
     * @return error code and JSON.
     */
    @GET
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public final Response loginByFP(@QueryParam("userName") final String userName,
                                    @QueryParam("password") final String password) {
        JSONObject returnObject;
        try {
            returnObject = new JSONObject();
            returnObject.put("userID", accountService.loginByFP(userName, password));
            return Response.status(Constants.CODE_CREATED).entity(returnObject).build();
        } catch (UserDoesntExistException ex) {
            return Response.status(Constants.CODE_NOT_FOUND).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        } catch (IncorrectPasswordException ex) {
            return Response.status(Constants.CODE_FORBIDDEN).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        } catch (EmptyPasswordException ex) {
            return Response.status(Constants.CODE_FORBIDDEN).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
        }
    }
}

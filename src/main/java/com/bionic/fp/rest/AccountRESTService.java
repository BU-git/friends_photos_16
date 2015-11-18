package com.bionic.fp.rest;

import com.bionic.fp.exception.*;
import com.bionic.fp.jsonhelper.FromJSONParser;
import com.bionic.fp.service.AccountService;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by boubdyk on 15.11.2015.
 */
//@RestController("/account")
public class AccountRESTService {

//    private static ApplicationContext context;
//    private static AccountService accountService;
//    private static FromJSONParser parser;
//
//    static {
//        context = new ClassPathXmlApplicationContext("beans.xml");
//        accountService = context.getBean(AccountService.class);
//        parser = context.getBean(FromJSONParser.class);
//    }
//
//    /**
//     * Used to wrap error to JSON.
//     * @param msg error message.
//     * @return JSONOnject.
//     */
//    private JSONObject errorMsgToJson(String msg) {
//        JSONObject returnObject = new JSONObject();
//        returnObject.put("errorCode", 304);
//        returnObject.put("message", msg);
//        return returnObject;
//    }
//
//    /**
//     * Endpoint used to register user by FP.
//     * @param input input JSON.
//     * @return error code and JSON.
//     */
//    @POST
//    @Path("/register")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    public final Response registerByFP(final String input) {
//        JSONObject inputObject = parser.parse(input);
//        JSONObject returnObject;
//        String email = (String)inputObject.get("email");
//        String userName = (String)inputObject.get("userName");
//        String password = (String)inputObject.get("password");
//        try {
//            returnObject = new JSONObject();
//            returnObject.put("userID", accountService.registerByFP(email, userName, password));
//            return Response.status(Constants.CODE_CREATED).entity(returnObject).build();
//        } catch (UserNameAlreadyExistException ex) {
//            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        } catch (EmailAlreadyExistException ex) {
//            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        } catch (EmptyPasswordException ex) {
//            return Response.status(Constants.CODE_NOT_MODIFIED).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        }
//    }
//
//    /**
//     * Endpoint used to login by FP.
//     * @param userName user name.
//     * @param password password.
//     * @return error code and JSON.
//     */
//    @GET
//    @Path("/login")
//    @Produces(MediaType.APPLICATION_JSON)
//    public final Response loginByFP(@QueryParam("userName") final String userName,
//                                    @QueryParam("password") final String password) {
//        JSONObject returnObject;
//        try {
//            returnObject = new JSONObject();
//            returnObject.put("userID", accountService.loginByFP(userName, password));
//            return Response.status(Constants.CODE_CREATED).entity(returnObject).build();
//        } catch (UserDoesntExistException ex) {
//            return Response.status(Constants.CODE_NOT_FOUND).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        } catch (IncorrectPasswordException ex) {
//            return Response.status(Constants.CODE_FORBIDDEN).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        } catch (EmptyPasswordException ex) {
//            return Response.status(Constants.CODE_FORBIDDEN).entity(errorMsgToJson(ex.getLocalizedMessage())).build();
//        }
//    }
}

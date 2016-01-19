# FriendsPhotos REST API

* ## Authentication
	* ### Authentication using email
		
        #### @POST	
        #### /api/auth
        
        Header | Value
		------ | -----
		Content-Type | application/json
		
        ##### Body:
        
			{
    			"email": "email@gmail.com",
    			"password": "secret"
			}
			
        Param Name | Required | Description
		---------- | -------- | -----------
        email | true | the user email
		password | true | the user password
		
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        400 | Bad Request
		
			{
    			"user_id": 4,
    			"token": "A#BCDE"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        user_id | false | the user id
		token | true | the user token for future requests       
        
	* ### Register using email

        #### @POST	
        #### /api/auth/register
        
        Header | Value
		------ | -----
		Content-Type | application/json
		
        ##### Body:
        
			{
    			"email": "email@gmail.com",
    			"password": "secret",
                "username": "Nagibator"
			}
			
        Param Name | Required | Description
		---------- | -------- | -----------
        email | true | the user email
		password | true | the user password
        username | false | the username
		
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        400 | Bad Request
		
			{
    			"user_id": 4,
    			"token": "A#BCDE"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        user_id | false | the user id
		token | true | the user token for future requests

	* ### Authentication via facebook

  		#### @POST	
        #### /api/auth/fb
        
        ##### Body:
        
        Key | Value
		--- | -----
        token | A#BCDE 
     	<p/>		
        Param Name | Required | Description
		---------- | -------- | -----------
        token | true | the facebook token
		
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        400 | Bad Request
		
			{
    			"user_id": 4,
    			"token": "A#BCDE"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        user_id | false | the user id
		token | true | the user token for future requests

	* ### ~~Authentication via vkontakte~~

	* ### Logout
	
		#### @POST	
        #### /api/auth/logout

        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        
* ## Interaction with account

	* ### Get an account
	
    	#### @GET
        #### /api/accounts/{account_id}
		
		##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | username,image_url
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
    			"account_id": 4,
    			"username": "Nagibator",
				"image_url": "http://nagibator2016.jpg"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        account_id | true | the account id
		username | true | the username
        image_url | true | the user image url
        
    * ### Get the user info
	
    	#### @GET
        #### /api/accounts/self
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | image_url,email
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
    			"account_id": 4,
    			"username": "Nagibator",
				"image_url": "http://nagibator2016.jpg",
                "email": "email@gmail.com"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        account_id | true | the account id
		username | true | the username
        image_url | true | the user image url
        email | true | the user email
		
	* ### Get a list of events of the account
	
    	#### @GET
        #### /api/accounts/{account_id}/events
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
        
    * ### Get a list of events of the user
	
    	#### @GET
        #### /api/accounts/self/events
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
	
	* ### Get a list of events owned by the account

    	#### @GET
        #### /api/accounts/{account_id}/events/owner
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
        
    * ### Get a list of events owned by the user
	
    	#### @GET
        #### /api/accounts/self/events/owner
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
		
	* ### Get a list of events where the account has the specified role

    	#### @GET
        #### /api/accounts/{account_id}/roles/{role_id}/events
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        role_id | 2
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        role_id | true | the role id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
        
    * ### Get a list of events where the user has the specified role
	
    	#### @GET
        #### /api/accounts/self/roles/{role_id}/events
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        role_id | 2
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        role_id | true | the role id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
		
	* ### Get a list of event ids of the account
	
    	#### @GET
        #### /api/accounts/{account_id}/events/id
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
           
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
        
    * ### Get a list of event ids of the user
	
    	#### @GET
        #### /api/accounts/self/events/id
   
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
	
	* ### Get a list of event ids owned by the account

    	#### @GET
        #### /api/accounts/{account_id}/events/id/owner
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
      
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
        
    * ### Get a list of event ids owned by the user
	
    	#### @GET
        #### /api/accounts/self/events/id/owner
       
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
		
	* ### Get a list of event ids where the account has the specified role

    	#### @GET
        #### /api/accounts/{account_id}/roles/{role_id}/events/id
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        role_id | 2
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        role_id | true | the role id
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
        
    * ### Get a list of event ids where the user has the specified role
	
    	#### @GET
        #### /api/accounts/self/roles/{role_id}/events/id
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        role_id | 2
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        role_id | true | the role id
       
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"events": [13, 54, 23, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of event ids
		
	* ### Get a list of photos of the account
	
    	#### @GET
        #### /api/accounts/{account_id}/photos
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | url,name,event_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"photos": [
                	{
                        "name": "NY 2016",
						"url": "http://ny2016.jpg",  	
  						"event_id": 56,
  						"owner_id": 78
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photos
        name | true | the photo name
		url | true | the photo url
        event_id | true | the id of the event where there is the photo
        owner_id | true | the id of the owner of the photo
        
    * ### Get a list of photos of the user
	
    	#### @GET
        #### /api/accounts/self/photos
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | url,name,event_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"photos": [
                	{
                        "name": "NY 2016",
						"url": "http://ny2016.jpg",  	
  						"event_id": 56,
  						"owner_id": 78
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photos
        name | true | the photo name
		url | true | the photo url
        event_id | true | the id of the event where there is the photo
        owner_id | true | the id of the owner of the photo
		
	* ### Get a list of photo ids of the account
	
    	#### @GET
        #### /api/accounts/{account_id}/photos/id
        
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        account_id | true | the account id
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
            	"photos": [15, 4, 84, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photo ids
        
    * ### Get a list of photo ids of the user
	
    	#### @GET
        #### /api/accounts/self/photos/id
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
		
			{
            	"photos": [15, 4, 84, ...]
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photo ids
		
* ## Interaction with event

	* ### Get an event
	
    	#### @GET
        #### /api/events/{event_id}
		
		##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
			{
                "event_id": 16,
                "name": "NY 2016",
                "description": "Happy New Year!",
                "date": "2015-11-21 17:41:03",
                "expire_date": "2015-12-21 17:41:03",
                "type_id": 1,
                "lat": 10.0,
                "lng": 20.0,
                "radius": 0.1,
                "geo": false
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
		
	* ### Get a list of events
	
    	#### @GET
        #### /api/events
		
        ##### Params:
        
        Key | Value
		--- | -----
        name | NY 2016
        description | enjoy
        ~~fields~~ | event_id,name,type_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        name | false | the event name
        description | false | the event description
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
        
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"events": [
                	{
                    	"event_id": 16,
                        "name": "NY 2016",
  						"description": "Happy New Year!",
  						"date": "2015-11-21 17:41:03",
                        "expire_date": "2015-12-21 17:41:03",
  						"type_id": 1,
  						"lat": 10.0,
  						"lng": 20.0,
                        "radius": 0.1,
  						"geo": false
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
        event_id | true | the event id
		name | true | the event name
        description | true | the event description
        date | true | the create date of the event
        expire_date | true | the expire date of the event
        type_id | true | the event type id
        lat | true | the event latitude
        lng | true | the event longitude
        radius | true | the event radius
        geo | true | is geolocation enabled for the event?
		
	* ### Get a list of event ids
	
    	#### @GET
        #### /api/events/id
		
        ##### Params:
        
        Key | Value
		--- | -----
        name | NY 2016
        description | enjoy
       
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        name | false | the event name
        description | false | the event description
                
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"events": [15, 16, 17, ...]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        events | false | the list of events
		
	* ### Get a list of id of the participants of the event
	
    	#### @GET
        #### /api/events/{event_id}/accounts/id
		
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
                
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"accounts": [15, 16, 17, ...]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        accounts | false | the list of account ids
		
	* ### Get a list of photos of the event
	
    	#### @GET
        #### /api/events/{event_id}/photos

        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | url,name,event_id
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
                
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"photos": [
                	{
                        "name": "NY 2016",
						"url": "http://ny2016.jpg",  	
  						"event_id": 56,
  						"owner_id": 78
                    }, ...
                ]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photos
        name | true | the photo name
		url | true | the photo url
        event_id | true | the id of the event where there is the photo
        owner_id | true | the id of the owner of the photo
		
	* ### Get a list of photo ids of the event
	
    	#### @GET
        #### /api/events/{event_id}/photos/id

        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
               
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"photos": [15, 23, 8, ...]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        photos | false | the list of photo ids
		
	* ### Get a list of comment ids of the event
	
    	#### @GET
        #### /api/events/{event_id}/comments/id

        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
               
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"comments": [15, 23, 8, ...]    			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        comments | false | the list of comment ids
		
	* ### ~~Get the owner of the event~~
	
    	#### @GET
        #### /api/events/{event_id}/owner

        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
        
        ##### Params:
        
        Key | Value
		--- | -----
        ~~fields~~ | username,image_url
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        ~~fields~~ | false | what fields should return the request, separated by commas, the order of insertion is not important
               
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"account_id": 4,
    			"username": "Nagibator",
				"image_url": "http://nagibator2016.jpg"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        account_id | true | the owner id of the event
		username | true | the owner username of the event
        image_url | true | the owner image url of the event
		
	* ### Get the owner id of the event
	
    	#### @GET
        #### /api/events/{event_id}/owner/id

        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
               
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        404 | NOT FOUND
		
        	{
            	"id": 8   			
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        id | false | the owner id of the event
		
	* ### Create an event
	
    	#### @POST
        #### /api/events/
        
       	Header | Value
		------ | -----
		Content-Type | application/json
		
        ##### Body:
        
			{
    			"name": "NY 2016",
  				"description": "Happy New Year!",
                "type_id": 1,
  				"visible": false,
  				"lat": 12.3,
  				"lng": 45.46,
                "radius": 78.9,
                "geo": true,
                "private": true,
                "password": "secret"
			}
			
        Param Name | Required | Description
		---------- | -------- | -----------
        name | true | the event name
        description | true | the event description
        type_id | true | the event type id
        visible | true | is the event visible
        private | true | is the event private
        password | false | the event password, **it will be required if the event is private**
        geo | false | is geolocation enabled for the event?
        lat | false | the event latitude, **it will be required if the geo is true**
        lng | false | the event longitude, **it will be required if the geo is true**
        radius | false | the event radius, **it will be required if the geo is true**
               
        ##### Response:
        
        Status | Description
		------ | -----------
        201 | CREATED
        400 | BAD REQUEST
		
        	{
            	"id": 4    		
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        id | false | the event id
		
	* ### Add the user to the event
	
    	#### @POST
        #### /api/events/{event_id}/accounts
       
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
      
        ##### Params:
        
        Key | Value
		--- | -----
        password | secret
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        password | false | the event password, **it will be required if the event is private**
               
        ##### Response:
        
        Status | Description
		------ | -----------
        201 | CREATED
        400 | BAD REQUEST
		
	* ### Add a comment to the event
	
    	#### @POST
        #### /api/events/{event_id}/comments
        
        Header | Value
		------ | -----
		Content-Type | application/json
       
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
      
        ##### Body:
        
			{
    			"comment_text": "some text"
			}
			
        Param Name | Required | Description
		---------- | -------- | -----------
        comment_text | true | some text in the comment
               
        ##### Response:
        
        Status | Description
		------ | -----------
        201 | CREATED
        400 | BAD REQUEST
        403 | FORBIDDEN
		
	* ### Update the event
	
    	#### @PUT
        #### /api/events/{event_id}
        
        Header | Value
		------ | -----
		Content-Type | application/json
       
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
      
        ##### Body:
        
			{
    			"name": "NY 2016",
  				"description": "Happy New Year!",
                "type_id": 1,
  				"visible": false,
  				"lat": 12.3,
  				"lng": 45.46,
                "radius": 78.9,
                "geo": true,
                "private": true,
                "password": "secret"
			}
			
        Param Name | Required | Description
		---------- | -------- | -----------
        name | false | the event name
        description | false | the event description
        type_id | false | the event type id
        visible | false | is the event visible
        private | false | is the event private
        password | false | the event password, **it will be required if the event is private**
        geo | false | is geolocation enabled for the event?
        lat | false | the event latitude, **it will be required if the geo is true**
        lng | false | the event longitude, **it will be required if the geo is true**
        radius | false | the event radius, **it will be required if the geo is true**
               
        ##### Response:
        
        Status | Description
		------ | -----------
        201 | CREATED
        400 | BAD REQUEST
        403 | FORBIDDEN
        404 | NOT FOUND
		
	* ### Сhange the role of the participant in the event
	
    	#### @PUT
        #### /api/events/{event_id}/accounts/{account_id}
       
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
        account_id | 14
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
        account_id | true | the account id
      
        ##### Params:
        
        Key | Value
		--- | -----
        role_id | 2
        password | secret
        
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        role_id | true | the new role id for the account
        password | false | the event password, **it will be required if the event is private**
	       
        ##### Response:
        
        Status | Description
		------ | -----------
        200 | OK
        400 | BAD REQUEST
        403 | FORBIDDEN
        404 | NOT FOUND
		
	* ### Delete the event
	
    	#### @DELETE
        #### /api/events/{event_id}
       
        ##### Path Params:
        
        Path Key | Value
		-------- | -----
        event_id | 14
      
        <p/>
        
        Param Name | Required | Description
		---------- | -------- | -----------
        event_id | true | the event id
       
        ##### Response:
        
        Status | Description
		------ | -----------
        204 | NO CONTENT
        403 | FORBIDDEN
        404 | NOT FOUND
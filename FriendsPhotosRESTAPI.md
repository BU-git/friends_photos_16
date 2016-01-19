# FriendsPhotos REST API

* ## Authentication
	* ## Authentication using email
		
        #### @POST	
        #### /api/auth/register
        
        Header | Value
		------------- | -------------
		Content-Type  | application/json
		
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
        
        Status |  Description
		---------- | --------
        200 | OK
        400 | Bad Request
		
			{
    			"user_id": 4,
    			"token": "A#BCDE"
			}
			
        Param Name | Nullable | Description
		---------- | -------- | -----------
        user_id | false | the user id
		token | true | the user token for future requsets       
        
	* ## Register using email
	* ## Authentication via facebook
	* ## Authentication via vkontakte
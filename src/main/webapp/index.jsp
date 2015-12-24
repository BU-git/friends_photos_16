<!DOCTYPE html>
<html lang="en" ng-app="friends_photos">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <title>Friends Photos</title>

    <!-- css -->
    <!-- google -->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!-- local -->
    <link href="bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <link href="bower_components/angular-material/angular-material.css" rel="stylesheet">
    <link href="bower_components/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="css/friends-photos.css" rel="stylesheet">
    <link href="css/account.css" rel="stylesheet">
    <link href="css/login.css" rel="stylesheet">
    <link href="css/header.css" rel="stylesheet">
    <link href="css/event-preview.css" rel="stylesheet">
    <link href="css/events-create-edit.css" rel="stylesheet">
    <link href="css/carousel.css" rel="stylesheet">

</head>
<body>
    <div ui-view="header"></div>
    <div ui-view="content"></div>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<!-- Libs -->
<script src="bower_components/angular/angular.js"></script>
<script src="bower_components/angular-animate/angular-animate.js"></script>
<script src="bower_components/angular-aria/angular-aria.js"></script>
<script src="bower_components/angular-material/angular-material.js"></script>
<script src="bower_components/angular-messages/angular-messages.js"></script>
<script src="bower_components/angular-ui-router/release/angular-ui-router.js"></script>
<!-- app -->
<script src="app/friends-photos.module.js"></script>
<script src="app/components/account.js"></script>
<script src="app/components/events.js"></script>
<script src="app/components/eventsCreateEdir.js"></script>
<script src="app/components/login.js"></script>
<script src="app/config/decorator.js"></script>
<script src="app/config/states.js"></script>
<script src="app/config/unauthorizedInterceptor.js"></script>
<script src="app/controllers/AuthController.js"></script>
<script src="app/controllers/EventsController.js"></script>
<script src="app/directives/carouselDirective.js"></script>
<script src="app/directives/dropzoneDirective.js"></script>
<script src="app/directives/eventPreviewDirective.js"></script>
<script src="app/directives/navbarDirective.js"></script>
<script src="app/services/authService.js"></script>
<script src="app/services/eventsService.js"></script>
<script src="app/services/facebookService.js"></script>
<script src="app/services/photosService.js"></script>

</body>
</html>

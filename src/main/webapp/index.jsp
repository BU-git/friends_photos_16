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
    <!-- bower -->
    <link href="bower_components/bootstrap/dist/css/bootstrap.css" rel="stylesheet">
    <link href="bower_components/angular-material/angular-material.css" rel="stylesheet">
    <link href="bower_components/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="bower_components/angular-loading-bar/build/loading-bar.css" rel="stylesheet">
    <!-- app -->
    <link href="dist/app.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet">

</head>
<body>
    <div ui-view="header"></div>
    <div ui-view="content"></div>

<!-- Include all compiled plugins (below), or include individual files as needed -->
<!-- Libs -->
<script src='//maps.googleapis.com/maps/api/js?sensor=false'></script>
<!-- app -->
<script src='dist/app.min.js'></script>
</body>
</html>

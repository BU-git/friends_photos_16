(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/account.html',
    '<div class="account-box">\n' +
    '    <div ui-view="login">\n' +
    '        <div class="account-btn-box">\n' +
    '            <section layout="row">\n' +
    '                <md-button class="md-raised" ui-sref="account.login">Login to your account</md-button>\n' +
    '            </section>\n' +
    '            <section layout="row">\n' +
    '                <md-button class="md-raised" ui-sref="account.create">Create new account</md-button>\n' +
    '            </section>\n' +
    '        </div>\n' +
    '    </div>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/addto.html',
    '  <!DOCTYPE html>\n' +
    '  <html>\n' +
    '    <head>  \n' +
    '      <title>Friends photos</title>\n' +
    '      <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>  \n' +
    '      <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css"  media="screen,projection"/>\n' +
    '      <link type="text/css" rel="stylesheet" href="css/style.css">\n' +
    '      <link type="text/css" rel="stylesheet" href="css/flaticon.css">\n' +
    '      <meta charset="utf-8"/>\n' +
    '      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>\n' +
    '    </head>\n' +
    '    <body>\n' +
    '      <section class="hide-on-med-and-up">\n' +
    '        <section class="row">\n' +
    '          <a href="#" class="col s3 logo-small"></a>\n' +
    '          <section class="col s9">\n' +
    '            <p class="main-title-small">friends photos</p>\n' +
    '            <p class="motto-small">easiest way to share common photos with friends</p>\n' +
    '          </section>\n' +
    '        </section>\n' +
    '        <section class="row">\n' +
    '          <a href="#" target="_blank" class="col s6 google-play-btn"></a>\n' +
    '          <a href="#" target="_blank" class="col s6 app-store-btn"></a>\n' +
    '        </section>\n' +
    '      </section>\n' +
    '      <section class="user-menu-container">\n' +
    '           <section class="row">\n' +
    '             <section class="row">\n' +
    '               <section class="container row">\n' +
    '               <div class="pre-form col s12 m4 l3">\n' +
    '                 <section class="col s12 user-menu-form">\n' +
    '                    <div class="row">\n' +
    '                      <p class="user-name">Yevhen Semenov</p>\n' +
    '                      <ul class="user-menu col s12">\n' +
    '                         <li><a href="/events.html">My events</a></li>\n' +
    '                         <li><a href="/new.html">New event</a></li>\n' +
    '                         <li><a href="/search.html">Search</a></li>\n' +
    '                         <li><a href="/invite.html">Invite friends</a></li>\n' +
    '                         <li><a href="/index.html">Sign out</a></li> \n' +
    '                      </ul>\n' +
    '                    </div>             \n' +
    '                 </section>\n' +
    '                 </div>\n' +
    '                 <div class="hide-on-small-only col m6 l9">\n' +
    '                 <div class="logo col l6"></div>\n' +
    '                   <div class="applications col l6">\n' +
    '                      <div class="row">\n' +
    '                        <a href="#" target="_blank" class="google-play-btn col s12 l4"></a>\n' +
    '                        <a href="#" target="_blank" class="app-store-btn col s12 l4"></a>\n' +
    '                      </div>\n' +
    '                      <div class="row">\n' +
    '                        <p class="main-title col l12">friends photos</p>\n' +
    '                        <p class="motto col l12">easiest way to share common photos with friends</p>\n' +
    '                      </div>\n' +
    '                   </div>\n' +
    '                 </div>\n' +
    '               </section>\n' +
    '             </section>\n' +
    '           </section>\n' +
    '      </section>\n' +
    '      <section class="col l12 white-bg">\n' +
    '        <div class="container row">\n' +
    '          <section class="col s12 l4">\n' +
    '            <section class="col s12">\n' +
    '              <p class="search-label">search</p>\n' +
    '            </section>\n' +
    '            <section class="input-field col s12">\n' +
    '              <input id="search-box" type="search" class="search-field">\n' +
    '              <label for="search-box">Пошук по ключовим словам або тегам</label>\n' +
    '            </section>\n' +
    '          </section>\n' +
    '          <section class="col s12 l7">            \n' +
    '            <ul class="tabs sorting-menu">\n' +
    '              <li class="tab col s2 disabled"><a href="#">Або шукати по:</a></li>\n' +
    '              <li class="tab col s2"><a href="#" class="active">популярним</a></li>\n' +
    '              <li class="tab col s2"><a href="#" >друзям</a></li>\n' +
    '              <li class="tab col s2"><a href="#">найближчим</a></li>\n' +
    '            </ul>\n' +
    '          </section>\n' +
    '        </div>\n' +
    '        <div class="container row">\n' +
    '          <section class="event-box col s12 m6 eff effect">\n' +
    '            <img src="img/event.jpg" alt="event" class="event">\n' +
    '            <div class="status-bar-up row">\n' +
    '              <p class="timestamp col s10">28.02.15 - 320 photos, 51 people 51</p>\n' +
    '              <p class="status col s2">Open</p>\n' +
    '            </div>           \n' +
    '            <div class="status-bar-down row">\n' +
    '              <p class="timestamp col s12">Boombox 10 years concert</p>\n' +
    '            </div>\n' +
    '              <div class="caption row">\n' +
    '                  <h4 class="col s12">приєднатися до події</h4>\n' +
    '                  <a class="add-btn" href="#" title="Add to"></a>\n' +
    '                  <p class="col s12">Boombox 10 years concert</p>\n' +
    '                  <p class="col s12">Вже 28 червня рівняни зможуть сповна насолодитися творчістю гурту "Бумбокс", який цього року відзначає 10-річний ювілей. Музиканти обіцяють, що концерт у Рівному буде особливим і з сюрпризом. Це стане справжнім святом для усіх фанатів гурту і тих, хто обожнює "живі" виступи.</p>\n' +
    '              </div>      \n' +
    '          </section>\n' +
    '         <section class="event-box col s12 m6 eff effect">\n' +
    '            <img src="img/event.jpg" alt="event" class="event">\n' +
    '            <div class="status-bar-up row">\n' +
    '              <p class="timestamp col s10">28.02.15 - 320 photos, 51 people 51</p>\n' +
    '              <p class="status col s2">Open</p>\n' +
    '            </div>           \n' +
    '            <div class="status-bar-down row">\n' +
    '              <p class="timestamp col s12">Boombox 10 years concert</p>\n' +
    '            </div>\n' +
    '              <div class="caption row">\n' +
    '                  <h4 class="col s12">приєднатися до події</h4>\n' +
    '                  <a class="add-btn" href="#" title="Add to"></a>\n' +
    '                  <p class="col s12">Boombox 10 years concert</p>\n' +
    '                  <p class="col s12">Вже 28 червня рівняни зможуть сповна насолодитися творчістю гурту "Бумбокс", який цього року відзначає 10-річний ювілей. Музиканти обіцяють, що концерт у Рівному буде особливим і з сюрпризом. Це стане справжнім святом для усіх фанатів гурту і тих, хто обожнює "живі" виступи.</p>\n' +
    '              </div>      \n' +
    '          </section>\n' +
    '         <section class="event-box col s12 m6 eff effect">\n' +
    '            <img src="img/event.jpg" alt="event" class="event">\n' +
    '            <div class="status-bar-up row">\n' +
    '              <p class="timestamp col s10">28.02.15 - 320 photos, 51 people 51</p>\n' +
    '              <p class="status col s2">Open</p>\n' +
    '            </div>           \n' +
    '            <div class="status-bar-down row">\n' +
    '              <p class="timestamp col s12">Boombox 10 years concert</p>\n' +
    '            </div>\n' +
    '              <div class="caption row">\n' +
    '                  <h4 class="col s12">приєднатися до події</h4>\n' +
    '                  <a class="add-btn" href="#" title="Add to"></a>\n' +
    '                  <p class="col s12">Boombox 10 years concert</p>\n' +
    '                  <p class="col s12">Вже 28 червня рівняни зможуть сповна насолодитися творчістю гурту "Бумбокс", який цього року відзначає 10-річний ювілей. Музиканти обіцяють, що концерт у Рівному буде особливим і з сюрпризом. Це стане справжнім святом для усіх фанатів гурту і тих, хто обожнює "живі" виступи.</p>\n' +
    '              </div>      \n' +
    '          </section>\n' +
    '         <section class="event-box col s12 m6 eff effect">\n' +
    '            <img src="img/event.jpg" alt="event" class="event">\n' +
    '            <div class="status-bar-up row">\n' +
    '              <p class="timestamp col s10">28.02.15 - 320 photos, 51 people 51</p>\n' +
    '              <p class="status col s2">Open</p>\n' +
    '            </div>           \n' +
    '            <div class="status-bar-down row">\n' +
    '              <p class="timestamp col s12">Boombox 10 years concert</p>\n' +
    '            </div>\n' +
    '              <div class="caption row">\n' +
    '                  <h4 class="col s12">приєднатися до події</h4>\n' +
    '                  <a class="add-btn" href="#" title="Add to"></a>\n' +
    '                  <p class="col s12">Boombox 10 years concert</p>\n' +
    '                  <p class="col s12">Вже 28 червня рівняни зможуть сповна насолодитися творчістю гурту "Бумбокс", який цього року відзначає 10-річний ювілей. Музиканти обіцяють, що концерт у Рівному буде особливим і з сюрпризом. Це стане справжнім святом для усіх фанатів гурту і тих, хто обожнює "живі" виступи.</p>\n' +
    '              </div>      \n' +
    '          </section>\n' +
    '        </div>\n' +
    '         </section>\n' +
    '      <footer>\n' +
    '        <section class="row container">\n' +
    '          <section class="col m4 l6">\n' +
    '            <a href="#" target="_blank">popular events</a>\n' +
    '          </section>\n' +
    '          <section class="col l2">\n' +
    '            <a href="#" target="_blank">api documentation</a>\n' +
    '          </section>\n' +
    '          <section class="col l2">\n' +
    '            <a href="#" target="_blank">developers team</a>\n' +
    '          </section>\n' +
    '          <section class="col s12 l2 logo-bu-box">\n' +
    '            <a href="#" target="_blank" class="logo-bu"></a>\n' +
    '          </section>\n' +
    '        </section>\n' +
    '      </footer>   \n' +
    '      <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>\n' +
    '      <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>\n' +
    '    </body>\n' +
    '  </html>\n' +
    '        ');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/choose-location-map.html',
    '<md-dialog class="modal-fullsize" aria-label="Choose location">\n' +
    '    <md-dialog-content>\n' +
    '        <ui-gmap-google-map center="map.center" zoom="map.zoom" draggable="true">\n' +
    '            <ui-gmap-marker idkey="marker.id"\n' +
    '                            coords="marker.coords"\n' +
    '                            options="marker.options">\n' +
    '            </ui-gmap-marker>\n' +
    '        </ui-gmap-google-map>\n' +
    '    </md-dialog-content>\n' +
    '    <md-dialog-actions>\n' +
    '        <md-button ng-click="ok()" class="md-primary">Ok</md-button>\n' +
    '        <md-button ng-click="closeDialog()" class="md-primary">Close Dialog</md-button>\n' +
    '    </md-dialog-actions>\n' +
    '</md-dialog>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/events-create-edit.html',
    '<!-- @namespace EventsController -->\n' +
    '<div class="container">\n' +
    '    <div class="row">\n' +
    '        <div class="col-lg-8 col-xs-12">\n' +
    '            <form name="eventForm" ng-submit="ctrl.save(ctrl.event)">\n' +
    '                <div layout="row">\n' +
    '                    <md-input-container class="md-block" flex>\n' +
    '                        <label>Name</label>\n' +
    '                        <input ng-model="ctrl.event.name" type="text">\n' +
    '                    </md-input-container>\n' +
    '                    <div class="md-datepicker-container">\n' +
    '                        <md-datepicker ng-model="event.date" md-placeholder="Enter date" class="pull-right">\n' +
    '                        </md-datepicker>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '                <md-input-container class="md-block">\n' +
    '                    <label>Description</label>\n' +
    '                    <input ng-model="ctrl.event.description" type="text">\n' +
    '                </md-input-container>\n' +
    '\n' +
    '                <div layout="row">\n' +
    '                    <md-button class="md-icon-button md-primary"\n' +
    '                               aria-label="Chose location"\n' +
    '                               ng-click="ctrl.chooseLocation($event)">\n' +
    '                        <md-icon md-font-set="material-icons">location_on</md-icon>\n' +
    '                    </md-button>\n' +
    '                    <md-input-container class="md-block" flex>\n' +
    '                        <label>Lat</label>\n' +
    '                        <input ng-model="ctrl.event.lat" type="text">\n' +
    '                    </md-input-container>\n' +
    '\n' +
    '                    <md-input-container class="md-block" flex>\n' +
    '                        <label>Lng</label>\n' +
    '                        <input ng-model="ctrl.event.lng" type="text">\n' +
    '                    </md-input-container>\n' +
    '\n' +
    '                    <md-input-container>\n' +
    '                        <label>Type</label>\n' +
    '                        <md-select ng-model="ctrl.event.type_id">\n' +
    '                            <md-option ng-repeat="type in [1, 2]" value="{{type}}">\n' +
    '                                {{type}}\n' +
    '                            </md-option>\n' +
    '                        </md-select>\n' +
    '                    </md-input-container>\n' +
    '                </div>\n' +
    '\n' +
    '                <div class="clearfix">\n' +
    '                    <md-button class="md-raised md-primary pull-right" type="submit">Save</md-button>\n' +
    '                </div>\n' +
    '\n' +
    '            </form>\n' +
    '\n' +
    '            <div class="alert alert-success" type="alert" ng-show="ctrl.uploadedPhotos">\n' +
    '                Uploaded <strong>{{ctrl.uploadedPhotos}}/{{ctrl.photos.length}}</strong> photos\n' +
    '            </div>\n' +
    '\n' +
    '        </div>\n' +
    '\n' +
    '        <div class="col-lg-4 col-xs-12">\n' +
    '            <div style="min-height: 325px; height: 100%; border: dotted 2px lightgrey; position: relative; border-radius: 5px;">\n' +
    '                <dropzone style="position: absolute; width: 100%; min-height: 100%; color: lightgray;"\n' +
    '                          ng-model="ctrl.photos">\n' +
    '                    <h3 style="line-height: 292px; text-align: center">Drag and drop files here</h3>\n' +
    '                </dropzone>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '    </div>\n' +
    '\n' +
    '    <md-divider></md-divider>\n' +
    '\n' +
    '    <edit-photos-list photos-list="ctrl.photos"></edit-photos-list>\n' +
    '\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/events.html',
    '<!-- namespace EventsController -->\n' +
    '<section class="col l12 white-bg">\n' +
    '    <div class="container row">\n' +
    '        <section class="col s12 l4">\n' +
    '            <section class="col s12">\n' +
    '                <p class="search-label">my events</p>\n' +
    '            </section>\n' +
    '            <section class="input-field col s12">\n' +
    '                <input id="search-box" type="search" class="search-field">\n' +
    '                <label for="search-box">Пошук по ключовим словам або тегам</label>\n' +
    '            </section>\n' +
    '        </section>\n' +
    '        <section class="col s12 l7">\n' +
    '            <ul class="tabs sorting-menu">\n' +
    '                <li class="tab col s2 disabled"><a href="#">відсортувати по:</a></li>\n' +
    '                <li class="tab col s2"><a href="#" class="active">даті створення</a></li>\n' +
    '                <li class="tab col s2"><a href="#">кількості фото</a></li>\n' +
    '                <li class="tab col s2"><a href="#">кількості учасників</a></li>\n' +
    '                <li class="tab col s2"><a href="#">алфавіту</a></li>\n' +
    '            </ul>\n' +
    '        </section>\n' +
    '    </div>\n' +
    '    <div class="container row">\n' +
    '        <section class="event-box col s12 m6 eff effect" ng-repeat="event in ctrl.events">\n' +
    '            <img ng-src="api/v1/photos/{{event.photos[0]}}/file" alt="event" class="event">\n' +
    '\n' +
    '            <div class="status-bar-up row">\n' +
    '                <p class="timestamp col s10">28.02.15 - {{::event.photos.length}} photos, 51 people 51</p>\n' +
    '\n' +
    '                <p class="status col s2">Open</p>\n' +
    '            </div>\n' +
    '            <div class="status-bar-down row">\n' +
    '                <p class="timestamp col s12">{{::event.description}}</p>\n' +
    '            </div>\n' +
    '            <div class="caption row">\n' +
    '                <h4 class="col s12">Переглянути фотоальбом</h4>\n' +
    '                <a class="view-more-btn" href="#" title="View More"></a>\n' +
    '\n' +
    '                <p class="col s12">Boombox 10 years concert</p>\n' +
    '\n' +
    '                <p class="col s12">Вже 28 червня рівняни зможуть сповна насолодитися творчістю гурту "Бумбокс", який\n' +
    '                    цього року відзначає 10-річний ювілей. Музиканти обіцяють, що концерт у Рівному буде особливим і з\n' +
    '                    сюрпризом. Це стане справжнім святом для усіх фанатів гурту і тих, хто обожнює "живі" виступи.</p>\n' +
    '            </div>\n' +
    '        </section>\n' +
    '    </div>\n' +
    '</section>\n' +
    '\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/footer.html',
    '<footer>\n' +
    '    <section class="row container">\n' +
    '        <section class="col m4 l6">\n' +
    '            <a href="#" target="_blank">popular events</a>\n' +
    '        </section>\n' +
    '        <section class="col l2">\n' +
    '            <a href="#" target="_blank">api documentation</a>\n' +
    '        </section>\n' +
    '        <section class="col l2">\n' +
    '            <a href="#" target="_blank">developers team</a>\n' +
    '        </section>\n' +
    '        <section class="col s12 l2 logo-bu-box">\n' +
    '            <a href="#" target="_blank" class="logo-bu"></a>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '</footer>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/header.html',
    '<section class="hide-on-med-and-up">\n' +
    '    <section class="row">\n' +
    '        <a href="#" class="col s3 logo-small"></a>\n' +
    '        <section class="col s9">\n' +
    '            <p class="main-title-small">friends photos</p>\n' +
    '            <p class="motto-small slogan">easiest way to share common photos with friends</p>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '    <section class="row">\n' +
    '        <a href="#" target="_blank" class="col s6 google-play-btn"></a>\n' +
    '        <a href="#" target="_blank" class="col s6 app-store-btn"></a>\n' +
    '    </section>\n' +
    '</section>\n' +
    '\n' +
    '<section class="user-menu-container">\n' +
    '    <section class="row">\n' +
    '        <section class="row">\n' +
    '            <section class="container row">\n' +
    '                <div ui-view="usermenu">\n' +
    '                    <signin></signin>\n' +
    '                </div>\n' +
    '                <div class="hide-on-small-only col m6 l9">\n' +
    '                    <div class="logo col l6"></div>\n' +
    '                    <div class="applications col l6">\n' +
    '                        <div class="row">\n' +
    '                            <a href="#" target="_blank" class="google-play-btn col s12 l4"></a>\n' +
    '                            <a href="#" target="_blank" class="app-store-btn col s12 l4"></a>\n' +
    '                        </div>\n' +
    '                        <div class="row">\n' +
    '                            <p class="main-title col l12">friends photos</p>\n' +
    '                            <p class="motto col l12">easiest way to share common photos with friends</p>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </section>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '</section>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/index.html',
    '<section class="col l12 white-bg">\n' +
    '    <section class="row container">\n' +
    '        <section class="col l4 ">\n' +
    '            <img src="img/message.png" alt="" class="message-box z-depth-2">\n' +
    '        </section>\n' +
    '        <section class="col l8">\n' +
    '            <h4 class="text-caption">Більше не потрібно скидати фото</h4>\n' +
    '\n' +
    '            <p class="text-body">Вам знайома ситуація, коли на наступний день після якоїсь події вам приходить купа\n' +
    '                повідомлень від друзів з проханням скинути фото? Або навпаки, хтось з ваших друзів зробив круту фотку з\n' +
    '                вами і не скидає її вам? Завантажте FriendsPhotos і забудьте про це!</p>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '    <section class="container row">\n' +
    '        <section class="col l6">\n' +
    '            <section class="col m6 l6">\n' +
    '                <img src="img/phone.png" alt="phone">\n' +
    '            </section>\n' +
    '            <section class="col m6 l6 description">\n' +
    '                <h4 class="text-caption">1. Завантаж</h4>\n' +
    '\n' +
    '                <p class="text-body">Зараз friends phones доступний лише для Android, але ми активно працюємо над\n' +
    '                    версією для iPhone</p>\n' +
    '            </section>\n' +
    '        </section>\n' +
    '        <section class="col l6">\n' +
    '            <section class="col m6 l6">\n' +
    '                <img src="img/phone.png" alt="phone">\n' +
    '            </section>\n' +
    '            <section class="col m6 l6 description">\n' +
    '                <h4 class="text-caption">2. Створи подію</h4>\n' +
    '\n' +
    '                <p class="text-body">Ми подбали про захист ваших фото! Різні рівні приватності дозволять поділитися\n' +
    '                    вашими фото лише з тими, з ким ви хочете</p>\n' +
    '            </section>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '    <section class="container row">\n' +
    '        <section class="col l6">\n' +
    '            <section class="col m6 l6 description">\n' +
    '                <h4 class="text-caption">3. Запроси друзів</h4>\n' +
    '\n' +
    '                <p class="text-body">Запросити друзів можна декількома способами: сказати їм ID події, назву події, або\n' +
    '                    ж просто запросити через додаток</p>\n' +
    '            </section>\n' +
    '            <section class="col m6 l2">\n' +
    '                <img src="img/phone.png" alt="phone">\n' +
    '            </section>\n' +
    '        </section>\n' +
    '        <section class="col l6">\n' +
    '            <section class="col m6 l6 description">\n' +
    '                <h4 class="text-caption">4. Фотографуй :)</h4>\n' +
    '\n' +
    '                <p class="text-body">Додавай фотографії до події і вони автоматично з\'являться у друзів! Також фото\n' +
    '                    можна переглянути та завантажити з сайту</p>\n' +
    '            </section>\n' +
    '            <section class="col m6 l2">\n' +
    '                <img src="img/phone.png" alt="phone">\n' +
    '            </section>\n' +
    '        </section>\n' +
    '    </section>\n' +
    '</section>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/login.html',
    '<div class="login-box">\n' +
    '    <h3>Login to your account:</h3>\n' +
    '\n' +
    '    <form name="loginForm" class="login-form" ng-submit="ctrl.login(user)">\n' +
    '        <md-input-container class="md-block">\n' +
    '            <label>E-mail</label>\n' +
    '            <input ng-model="user.address" type="text">\n' +
    '        </md-input-container>\n' +
    '        <md-input-container class="md-block">\n' +
    '            <label>Password</label>\n' +
    '            <input ng-model="user.password" type="password">\n' +
    '        </md-input-container>\n' +
    '\n' +
    '        <p>Or use socials</p>\n' +
    '\n' +
    '        <!--<fb:login-button show-faces="false" max-rows="1" size="large"></fb:login-button>-->\n' +
    '        <div layout="row">\n' +
    '            <md-button class="md-raised md-default">\n' +
    '                G+\n' +
    '            </md-button>\n' +
    '            <md-button class="md-raised md-primary" ng-click="ctrl.login(\'facebook\')" type="button">\n' +
    '                FB\n' +
    '            </md-button>\n' +
    '        </div>\n' +
    '\n' +
    '        <div layout="row">\n' +
    '            <md-button class="md-raised md-primary">\n' +
    '                Login\n' +
    '            </md-button>\n' +
    '        </div>\n' +
    '\n' +
    '    </form>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/maps.html',
    '  <!DOCTYPE html>\n' +
    '  <html>\n' +
    '    <head>  \n' +
    '      <title>Friends photos</title> \n' +
    '      <link href="http://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet"/>  \n' +
    '      <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/css/materialize.min.css"  media="screen,projection"/>\n' +
    '      <link type="text/css" rel="stylesheet" href="css/style.css">\n' +
    '      <link type="text/css" rel="stylesheet" href="css/flaticon.css">\n' +
    '      <meta charset="utf-8"/>\n' +
    '      <meta name="viewport" content="width=device-width, initial-scale=1.0"/>\n' +
    '    </head>\n' +
    '    <body>\n' +
    '      <section class="hide-on-med-and-up">\n' +
    '        <section class="row">\n' +
    '          <a href="#" class="col s3 logo-small"></a>\n' +
    '          <section class="col s9">\n' +
    '            <p class="main-title-small">friends photos</p>\n' +
    '            <p class="motto-small">easiest way to share common photos with friends</p>\n' +
    '          </section>\n' +
    '        </section>\n' +
    '        <section class="row">\n' +
    '          <a href="#" target="_blank" class="col s6 google-play-btn"></a>\n' +
    '          <a href="#" target="_blank" class="col s6 app-store-btn"></a>\n' +
    '        </section>\n' +
    '      </section>\n' +
    '      <section class="user-menu-container">\n' +
    '           <section class="row">\n' +
    '             <section class="row">\n' +
    '               <section class="container row">\n' +
    '               <div class="pre-form col s12 m4 l3">\n' +
    '                 <section class="col s12 user-menu-form">\n' +
    '                    <div class="row">\n' +
    '                      <p class="user-name">Yevhen Semenov</p>\n' +
    '                      <ul class="user-menu col s12">\n' +
    '                         <li><a href="/events.html">My events</a></li>\n' +
    '                         <li><a href="/new.html">New event</a></li>\n' +
    '                         <li><a href="/search.html">Search</a></li>\n' +
    '                         <li><a href="/invite.html">Invite friends</a></li>\n' +
    '                         <li><a href="/index.html">Sign out</a></li> \n' +
    '                      </ul>\n' +
    '                    </div>             \n' +
    '                 </section>\n' +
    '                 </div>\n' +
    '                 <div class="hide-on-small-only col m6 l9">\n' +
    '                 <div class="logo col l5"></div>\n' +
    '                   <div class="applications col l7">\n' +
    '                      <div class="row">\n' +
    '                        <a href="#" target="_blank" class="google-play-btn col s12 l4"></a>\n' +
    '                        <a href="#" target="_blank" class="app-store-btn col s12 l4"></a>\n' +
    '                      </div>\n' +
    '                      <div class="row">\n' +
    '                        <p class="main-title col l12">friends photos</p>\n' +
    '                        <p class="motto col l12">easiest way to share common photos with friends</p>\n' +
    '                      </div>\n' +
    '                   </div>\n' +
    '                 </div>\n' +
    '               </section>\n' +
    '             </section>\n' +
    '           </section>\n' +
    '      </section>\n' +
    '      <section class="col l12 white-bg">\n' +
    '        <div class="container row">\n' +
    '          <section class="col s12 l4">\n' +
    '            <section class="col s12">\n' +
    '              <p class="search-label">search</p>\n' +
    '            </section>\n' +
    '            <section class="input-field col s12">\n' +
    '              <input id="search-box" type="search" class="search-field">\n' +
    '              <label for="search-box">Пошук по ключовим словам або тегам</label>\n' +
    '            </section>\n' +
    '          </section>\n' +
    '          <section class="col s12 l7">            \n' +
    '            <ul class="tabs sorting-menu">\n' +
    '              <li class="tab col s2 disabled"><a href="#">Або шукати по:</a></li>\n' +
    '              <li class="tab col s2"><a href="#" class="active">популярним</a></li>\n' +
    '              <li class="tab col s2"><a href="#" >друзям</a></li>\n' +
    '              <li class="tab col s2"><a href="#">найближчим</a></li>\n' +
    '            </ul>\n' +
    '          </section>\n' +
    '        </div>\n' +
    '        <div class="container row">\n' +
    '          <section class="col s12">\n' +
    '              <iframe class="col s12" src="https://www.google.com/maps/embed?pb=!1m14!1m12!1m3!1d36212.79863077946!2d30.5270858940232!3d50.45100381081887!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!5e0!3m2!1suk!2sua!4v1452804207985" frameborder="0" style="border:0" height="500" allowfullscreen></iframe>\n' +
    '          </section>\n' +
    '        </div>\n' +
    '         </section>\n' +
    '      <footer>\n' +
    '        <section class="row container">\n' +
    '          <section class="col m4 l6">\n' +
    '            <a href="#" target="_blank">popular events</a>\n' +
    '          </section>\n' +
    '          <section class="col l2">\n' +
    '            <a href="#" target="_blank">api documentation</a>\n' +
    '          </section>\n' +
    '          <section class="col l2">\n' +
    '            <a href="#" target="_blank">developers team</a>\n' +
    '          </section>\n' +
    '          <section class="col s12 l2 logo-bu-box">\n' +
    '            <a href="#" target="_blank" class="logo-bu"></a>\n' +
    '          </section>\n' +
    '        </section>\n' +
    '      </footer>   \n' +
    '      <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>\n' +
    '      <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.97.5/js/materialize.min.js"></script>\n' +
    '    </body>\n' +
    '  </html>\n' +
    '        ');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/signin.html',
    '<!-- @namespace AuthController -->\n' +
    '<div class="pre-form col l3">\n' +
    '    <form name="signinForm" ng-submit="authCtrl.login(user)" class="col l12 user-menu-form">\n' +
    '        <div class="row user-menu-form-fields">\n' +
    '            <p class="sing-in-caption">sing in</p>\n' +
    '            <div class="input-field col s12">\n' +
    '                <input id="email" type="email" class="validate" ng-model="user.email">\n' +
    '                <label for="email">E-mail</label>\n' +
    '            </div>\n' +
    '            <div class="input-field col s12">\n' +
    '                <input id="password" type="password" class="validate" ng-model="user.password">\n' +
    '                <label for="password">Password</label>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '        <!-- submit button -->\n' +
    '        <button class="btn waves-effect waves-light" type="submit" name="action">\n' +
    '            Sing in with <span>friends photos</span>\n' +
    '        </button>\n' +
    '\n' +
    '        <p class="sing-social-caption">or use social</p>\n' +
    '        <div class="row">\n' +
    '            <div class="col l6">\n' +
    '                <a class="btn waves-effect waves-light btn-fb flaticon-facebook55"\n' +
    '                   ng-click="authCtrl.login(\'facebook\')">Sing in with</a>\n' +
    '            </div>\n' +
    '            <div class="col l6">\n' +
    '                <a class="btn waves-effect waves-light btn-vk flaticon-vk3">Sing in with</a>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '    </form>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/views/usermenu.html',
    '<div class="pre-form col s12 m4 l3">\n' +
    '    <section class="col s12 user-menu-form">\n' +
    '        <div class="row">\n' +
    '            <p class="user-name" ng-bind="$root.user.username"></p>\n' +
    '            <ul class="user-menu col s12">\n' +
    '                <li><a ui-sref="home.events">My events</a></li>\n' +
    '                <li><a href="/new.html">New event</a></li>\n' +
    '                <li><a href="/search.html">Search</a></li>\n' +
    '                <li><a href="/invite.html">Invite friends</a></li>\n' +
    '                <li><a href="/index.html">Sign out</a></li>\n' +
    '            </ul>\n' +
    '        </div>\n' +
    '    </section>\n' +
    '</div>\n' +
    '');
}]);
})();

(function(module) {
try {
  module = angular.module('friends_photos');
} catch (e) {
  module = angular.module('friends_photos', []);
}
module.run(['$templateCache', function($templateCache) {
  $templateCache.put('app/directives/templates/event-preview-directive.html',
    '<img photo="event.photos[0]" ng-click="showCarousel($event, event, event.photos, 0)">\n' +
    '<div class="event-name">\n' +
    '    <span ng-bind="event.name"></span>\n' +
    '    <a class="pull-right" ui-sref="home.events.edit({id: event.event_id})">\n' +
    '        <i class="fa fa-edit"></i>\n' +
    '    </a>\n' +
    '</div>\n' +
    '<div class="event-info">\n' +
    '    <i class="fa fa-users"></i> <span ng-bind="event.photos.length"></span>\n' +
    '    <i class="fa fa-calendar"></i> <span ng-bind="event.date | date"></span>\n' +
    '    <div class="pull-right">\n' +
    '        <i class="fa fa-user-plus"></i>\n' +
    '    </div>\n' +
    '</div>\n' +
    '');
}]);
})();

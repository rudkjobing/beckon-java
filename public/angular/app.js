/**
 * Created by steffen on 22/12/14.
 */
(function() {
    var app = angular.module("beckon", []);
    app.controller("SecurityController", ['$http', function($http){
        var context = this;
        this.loggedIn = false;
        this.phoneNumber = "";
        this.user = {};
        this.securityContext;

        this.signInAction = function(){
            $http.post('/api/signIn', this.securityContext).success(function(data){
                context.loggedIn = true;
            });
        };

        this.signUpAction = function(){
            $http.post('/api/signUp', this.user).success(function(data){
                context.securityContext = data;
            });
        };

        this.requestAccessAction = function(){
            $http.post('/api/getSecurityContext', {phoneNumber: this.phoneNumber}).success(function(data){
                context.securityContext = data;
            });
        };

    }]);
})();
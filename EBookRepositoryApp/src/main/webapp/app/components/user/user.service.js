(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .service("userService", UserService);

    UserService.$inject = ["$http", "localStorageService"];
    function UserService($http, localStorageService) {
        this.$http = $http;
        this.localStorageService = localStorageService;
    }

    UserService.prototype.getJwt = function (username, password) {
        var request = {
            method: "GET",
            url: "/api/authentications/jwt",
            headers: {
                "X-Auth-Username": username,
                "X-Auth-Password": password
            }
        };

        return this.$http(request);
    };

    UserService.prototype.getUserById = function (userId) {
        var request = {
            method: "GET",
            url: "/api/users/" + userId
        };

        return this.$http(request);
    };

    UserService.prototype.getUserIdFromLocalStorage = function () {
        return this.localStorageService.get("user.id");
    };

    UserService.prototype.saveUserIdToLocalStorage = function (userId) {
        this.localStorageService.add("user.id", userId);
    };

    UserService.prototype.deleteUserIdFromLocalStorage = function () {
        this.localStorageService.remove("user.id");
    };

    UserService.prototype.getJwtFromLocalStorage = function () {
        return this.localStorageService.get("user.jwt");
    };

    UserService.prototype.saveJwtToLocalStorage = function (jwt) {
        this.localStorageService.add("user.jwt", jwt);
    };

    UserService.prototype.deleteJwtFromLocalStorage = function () {
        this.localStorageService.remove("user.jwt");
    };

    UserService.prototype.login = function (username, password, successCallback, errorCallback) {
        var thisUserService = this;

        thisUserService
            .getJwt(username, password)
            .then(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    var jwt = data.token;
                    var userId = data.userId;

                    thisUserService.saveJwtToLocalStorage(jwt);
                    thisUserService.saveUserIdToLocalStorage(userId);
                    successCallback(response);
                }
            }, function (response) {
                errorCallback();
            });
    };

    UserService.prototype.logout = function () {
        this.deleteJwtFromLocalStorage();
        this.deleteUserIdFromLocalStorage();
    };

    UserService.prototype.isLoggedIn = function () {
        var loggedIn = false;
        var userId = this.getUserIdFromLocalStorage();
        var jwt = this.getJwtFromLocalStorage();

        if (userId && jwt) {
            loggedIn = true;
        }

        return loggedIn;
    };
} (angular));

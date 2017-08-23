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

    UserService.prototype.getJwt = function (username, password, successCallback, errorCallback) {
        var request = {
            method: "GET",
            url: "/api/authentications/jwt",
            headers: {
                "Accept": "text/plain",
                "X-Auth-Username": username,
                "X-Auth-Password": password
            }
        };

        this.$http(request).then(successCallback, errorCallback);
    };

    UserService.prototype.getJwtFromLocalStorage = function () {
        return this.localStorageService.get("user.jwt");
    };

    UserService.prototype.saveJwtToLocalStorage = function (jwt) {
        this.localStorageService.add("user.jwt", jwt);
    };

    UserService.prototype.removeJwtFromLocalStorage = function () {
        this.localStorageService.remove("user.jwt");
    };

    UserService.prototype.login = function (username, password, successCallback, errorCallback) {
        var thisUserService = this;

        thisUserService.getJwt(username,
            password,
            function (response) {
                if (response.status == 200) {
                    var jwt = response.data;

                    thisUserService.saveJwtToLocalStorage(jwt);
                    thisUserService.$http.defaults.headers.common["X-Auth-Jwt"] = jwt;
                    successCallback(response);
                }
            },
            function (response) {
                errorCallback(response);
            });
    };

    UserService.prototype.logout = function () {
        this.removeJwtFromLocalStorage();
    };
} (angular));

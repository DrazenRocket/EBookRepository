(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .config(configRoute);

    configRoute.$inject = ["$stateProvider"];
    function configRoute($stateProvider) {
        $stateProvider
            .state("user-login", {
                url: "/user-login",
                views: {
                    mainView: {
                        templateUrl: "app/components/user/user-login/user-login.html",
                        controller: "UserLoginController",
                        controllerAs: "userLoginViewModel"
                    }
                }
            })
            .state("user-profile", {
                url: "/user-profile/:userId",
                views: {
                    mainView: {
                        templateUrl: "app/components/user/user-profile/user-profile.html",
                        controller: "UserProfileController",
                        controllerAs: "userProfileViewModel"
                    }
                }
            });
    }
} (angular));

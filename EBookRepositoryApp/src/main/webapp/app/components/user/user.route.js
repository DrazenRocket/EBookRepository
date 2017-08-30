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
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/user/user-profile/user-profile.html",
                        controller: "UserProfileController",
                        controllerAs: "userProfileViewModel"
                    }
                }
            })
            .state("user-list", {
                url: "/user-list",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/user/user-list/user-list.html",
                        controller: "UserListController",
                        controllerAs: "userListViewModel"
                    }
                }
            })
            .state("user-add", {
                url: "/user-add",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/user/user-add/user-add.html",
                        controller: "UserAddController",
                        controllerAs: "userAddViewModel"
                    }
                }
            })
            .state("user-edit-info", {
                url: "/user-edit-info/:userId",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/user/user-edit/user-edit-info/user-edit-info.html",
                        controller: "UserEditInfoController",
                        controllerAs: "userEditInfoViewModel"
                    }
                }
            })
            .state("user-edit-password", {
                url: "/user-edit-password/:userId",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/user/user-edit/user-edit-password/user-edit-password.html",
                        controller: "UserEditPasswordController",
                        controllerAs: "userEditPasswordViewModel"
                    }
                }
            });
    }
} (angular));

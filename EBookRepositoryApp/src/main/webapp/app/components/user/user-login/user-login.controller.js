(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserLoginController", UserLoginController);

    UserLoginController.$inject = ["userService", "$state"];
    function UserLoginController(userService, $state) {
        var viewModel = this;

        viewModel.username = "";
        viewModel.password = "";
        viewModel.login = login;
        viewModel.unsuccessfulLogin = false;

        function login(isValid) {
            if (isValid) {
                userService.login(viewModel.username,
                    viewModel.password,
                    function (response) {
                        $state.go("e-book-search");
                    },
                    function () {
                        viewModel.unsuccessfulLogin = true;
                    });
            }
        }
    }
} (angular));

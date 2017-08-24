(function (angular) {
    "use strict";

    angular
        .module("app.user")
        .controller("UserLoginController", UserLoginController);

    UserLoginController.$inject = ["userService"];
    function UserLoginController(userService) {
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
                        // TODO: Redirect
                        viewModel.unsuccessfulLogin = false;
                        console.log(response.data);
                    },
                    function () {
                        viewModel.unsuccessfulLogin = true;
                    });
            }
        }
    }
} (angular));

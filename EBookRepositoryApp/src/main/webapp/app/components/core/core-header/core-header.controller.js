(function (angular) {
    "use strict";

    angular
        .module("app.core")
        .controller("CoreHeaderController", CoreHeaderController);

    CoreHeaderController.$inject = ["userService", "$state"];
    function CoreHeaderController(userService, $state) {
        var viewModel = this;

        viewModel.loggedIn = userService.isLoggedIn();
        viewModel.userId = -1;
        viewModel.userDetails = {};
        viewModel.logout = logout;

        if (viewModel.loggedIn) {
            viewModel.userId = userService.getUserIdFromLocalStorage();
            userService
                .getUserById(viewModel.userId)
                .then(function (response) {
                    if (response.status == 200) {
                        viewModel.userDetails = response.data;
                    }
                });
        }

        function logout() {
            userService.logout();
            $state.go("user-login");
        }
    }
} (angular));
